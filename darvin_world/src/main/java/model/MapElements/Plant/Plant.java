package model.MapElements.Plant;

import model.MapElements.MapElement;
import model.Others.Vector2d;

public class Plant implements MapElement {
    private final Vector2d position;

    public Plant(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "#";
    }

    public String getName() {
        return "grass.png";
    }
}
