package model.Maps;

import model.MapElements.Animal.Animal;
import model.Others.MapDirection;
import model.Others.Vector2d;

public class PoleMap extends AbstractWorldMap {
    private final float a;

    public PoleMap(int height, int width, int cost) {
        super(height, width, cost);
        this.a = (float) (4 * cost) / (height);
    }

    private int calculateCost(int height){
        return Math.round(this.a * Math.abs(height - (float) this.height / 2));
    }

    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        animal.setNewDirection(animal.getActiveGenom());
        MapDirection direction = animal.getDirection();
        Vector2d newPosition = newPosition(oldPosition,direction.toUnitVector());
        if (canMoveTo(newPosition)) {
            if (animal.getEnergyLevel() - calculateCost(animal.getPosition().getY()) >= 0) {
                moveHelper(animal, direction, oldPosition, newPosition);
            } else {
                animal.setEnergyLevel(0);
            }
        }
    }
}
