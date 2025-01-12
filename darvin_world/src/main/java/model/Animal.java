package model;

import java.util.ArrayList;
import java.util.List;

public class Animal implements Livings {
    private MapDirection direction;
    private Vector2d position;
    private List<Integer> genes = new ArrayList<>();
    private int energyLevel;
    private int activeGenom;
    private int eatenPlants = 0;
    private int children = 0;
    private int descendant = 0;
    private int livedDays = 0;
    private int dayOfDeath;

    public Animal(MapDirection direction, Vector2d position, List<Integer> genes, int energyLevel) {
        this.direction = direction;
        this.position = position;
        this.energyLevel = energyLevel;
        this.genes.addAll(genes);
    }

    public Animal(Vector2d position, List<Integer> genes, int energyLevel) {
        this.direction = MapDirection.NORTH;
        this.position = position;
        this.energyLevel = energyLevel;
        this.genes.addAll(genes);
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

    public List<Integer> getGenes() {
        return this.genes;
    }

    public int getEnergyLevel() {
        return this.energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
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

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void addEnergy(int amount) {
        this.energyLevel += amount;
    }

    public void move(MapDirection direction, WorldMap map) {
        Vector2d potential_new_position = map.newPosition(position.add(direction.toUnitVector()));
        if(map.canMoveTo(potential_new_position)) {
            position = potential_new_position;
        }
    }

}
