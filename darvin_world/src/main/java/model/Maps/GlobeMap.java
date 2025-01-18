package model.Maps;

import model.MapElements.Animal.Animal;
import model.MapElements.Plant.Plant;
import model.Others.MapDirection;
import model.Others.Vector2d;
import java.util.ArrayList;
import java.util.Map;

public class GlobeMap extends AbstractWorldMap {
    public GlobeMap(int height, int width, int cost) {
        super(height, width, cost);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(this.lowerLeft) && position.precedes(this.upperRight);
    }

    @Override
    public void move(Animal animal, MapDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        animal.setNewDirection(animal.getActiveGenom());
        Vector2d newPosition = oldPosition.addOnTheGlobe(direction.toUnitVector(), width);
        moveHelper(animal, direction, oldPosition, newPosition);
    }

    @Override
    public Map<Vector2d,Plant> getPlantMap() {
        return plants;
    }
}
