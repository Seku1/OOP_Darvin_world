package model.MapElements.Animal;

import model.MapElements.MapElement;
import model.Maps.AbstractWorldMap;
import model.Others.MapDirection;
import model.Others.Vector2d;
import model.Maps.WorldMap;
import java.util.Random;


public abstract class Animal implements MapElement {
    private MapDirection direction;
    Vector2d position;
    private int [] genes;
    private int energyLevel;
    private int activeGenom;
    private int eatenPlants = 0;
    private int children = 0;
    private int descendant = 0;
    private int livedDays = 0;
    private int dayOfDeath;

    public Animal(MapDirection direction, Vector2d position, int [] genes, int energyLevel) {
        this.direction = direction;
        this.position = position;
        this.energyLevel = energyLevel;
        this.genes = genes;
    }

    public Animal(Vector2d position, int [] genes, int energyLevel) {
        this.direction = MapDirection.NORTH;
        this.position = position;
        this.energyLevel = energyLevel;
        this.genes = genes;
    }

    public Animal(int energyLevel, int livedDays) {
        this.energyLevel = energyLevel;
        this.livedDays = livedDays;
    }

    public Animal(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public Animal(int energyLevel, int livedDays, int children) {
        this.energyLevel = energyLevel;
        this.livedDays = livedDays;
        this.children = children;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public int [] getGenes() {
        return this.genes;
    }

    public int getEnergyLevel() {
        return this.energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public void setActiveGenom(int genomSize) {
        Random random = new Random();
        this.activeGenom = random.nextInt(genomSize + 1);
    }

    public void incrementActiveGenom() {
        this.activeGenom = (this.activeGenom + 1) % this.genes.length;
    }

    public void setNewDirection(int turn) {
        this.direction = this.direction.afterTurnDirection(turn);
    }

    public int getChildren() {
        return this.children;
    }

    public int getDescendant() {
        return this.descendant;
    }

    public int getLiveDays() {
        return this.livedDays;
    }

    public int getDayOfDeath() {
        return this.dayOfDeath;
    }

    public void setDayOfDeath(int dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
    }

    public int getActiveGenom() {
        return this.activeGenom;
    }

    public int getEatenPlants() {
        return this.eatenPlants;
    }

    public void incrementEatenPlants() {
        this.eatenPlants++;
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void addEnergy(int amount) {
        this.energyLevel += amount;
    }

    public void move(MapDirection direction, AbstractWorldMap map) {
        Vector2d potential_new_position = this.position.add(this.direction.toUnitVector());
        if(map.canMoveTo(potential_new_position)) {
            position = potential_new_position;
        }
    }

    public String toString() {
        return "*";
    }
}
