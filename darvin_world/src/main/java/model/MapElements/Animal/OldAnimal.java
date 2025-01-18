package model.MapElements.Animal;

import model.Maps.AbstractWorldMap;
import model.Others.MapDirection;
import model.Others.Vector2d;
import model.Maps.WorldMap;

import java.util.List;
import java.util.Random;

public class OldAnimal extends Animal {
    private Random random = new Random();
    public OldAnimal(MapDirection direction, Vector2d position, int [] genes, int energyLevel) {
        super(direction, position, genes, energyLevel);
    }

    @Override
    public void move(MapDirection direction, AbstractWorldMap map) {
        double chanceToSkip = Math.min(0.8, getLiveDays() * 0.01);
        if (random.nextDouble() < chanceToSkip) {
            return;
        }
        Vector2d potential_new_position = this.position.add(super.getDirection().toUnitVector());
        if(map.canMoveTo(potential_new_position)) {
            super.position = potential_new_position;
        }
    }
}
