package model;

import model.util.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PoleMapTest {
    @Test
    void moveTestEquatorEven() {
        Animal animal = new Animal(MapDirection.SOUTH, new Vector2d(2,5), List.of(1,1), 100);
        WorldMap map = new PoleMap(10, 5, 100);
        try {
            map.place(animal);
        } catch (IncorrectPositionException e) {
            throw new RuntimeException(e);
        }

        map.move(animal, MapDirection.EAST);

        assertEquals(animal.getEnergyLevel(), 0);
    }
    @Test
    void moveTestPoleEven() {
        Animal animal = new Animal(MapDirection.SOUTH, new Vector2d(2,10), List.of(1,1), 300);
        Animal animal1 = new Animal(MapDirection.SOUTH, new Vector2d(2,0), List.of(1,1), 300);
        WorldMap map = new PoleMap(10, 5, 100);
        try {
            map.place(animal);
        } catch (IncorrectPositionException e) {
            throw new RuntimeException(e);
        }

        map.move(animal, MapDirection.EAST);
        map.move(animal1, MapDirection.EAST);

        assertEquals(animal.getEnergyLevel(), 0);
        assertEquals(animal1.getEnergyLevel(), 0);
    }

    @Test
    void moveTestPoleOdd() {
        Animal animal = new Animal(MapDirection.SOUTH, new Vector2d(2,11), List.of(1,1), 300);
        Animal animal1 = new Animal(MapDirection.SOUTH, new Vector2d(2,0), List.of(1,1), 300);
        WorldMap map = new PoleMap(11, 5, 100);
        try {
            map.place(animal);
        } catch (IncorrectPositionException e) {
            throw new RuntimeException(e);
        }

        map.move(animal, MapDirection.NORTH);
        map.move(animal1, MapDirection.SOUTH);

        assertEquals(animal.getEnergyLevel(), 0);
        assertEquals(animal1.getEnergyLevel(), 0);
    }
}