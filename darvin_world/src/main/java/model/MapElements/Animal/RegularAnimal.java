package model.MapElements.Animal;

import model.Others.MapDirection;
import model.Others.Vector2d;

public class RegularAnimal extends Animal {
    public RegularAnimal(MapDirection direction, Vector2d position, int [] genes, int energyLevel) {
        super(direction, position, genes, energyLevel);
    }
}
