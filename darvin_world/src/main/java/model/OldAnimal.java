package model;

import java.util.List;

public class OldAnimal extends Animal{
    public OldAnimal(MapDirection direction, Vector2d position, List<Integer> genes, int energyLevel) {
        super(direction, position, genes, energyLevel);
    }

    @Override
    public void move(MapDirection direction, WorldMap map) {
        Vector2d potential_new_position = map.newPosition(this.position.add(direction.toUnitVector()));
    }
}
