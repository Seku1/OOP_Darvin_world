package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlantTest {
    Plant plant = new Plant(new Vector2d(1,1));

    @Test
    void gettingPosition() {
        assertEquals(new Vector2d(1,1), plant.getPosition());
    }
}