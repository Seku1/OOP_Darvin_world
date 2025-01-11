package model;

import java.util.ArrayList;
import java.util.List;

public class Animal implements Livings {
    private MapDirection direction;
    private Vector2d position;
    private List<Integer> genes = new ArrayList<>();
    private int energyLevel;

    public Animal(MapDirection direction, Vector2d position, List<Integer> genes, int energyLevel) {
        this.direction = direction;
        this.position = position;
        this.energyLevel = energyLevel;
        this.genes.addAll(genes);
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

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void eat(int plantEnergy) {
        energyLevel += plantEnergy;
    }

    public void coopulate(int energyLoss) {
        energyLevel -= energyLoss;
    }

    public void move(MapDirection direction, WorldMap map) {
        Vector2d potential_new_position = map.newPosition(position.add(direction.toUnitVector()));
        if(map.canMoveTo(potential_new_position)) {
            position = potential_new_position;
        }
    }

}