package model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PoleMapTest {

    @Test
    void moveTest() {
        Animal animal = new Animal(MapDirection.SOUTH, new Vector2d(2,3), List.of(1,1), 100);
        WorldMap map = new AbstractWorldMap(9, 5);

    }
}