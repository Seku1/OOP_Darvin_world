package model;

import model.util.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class AbstractWorldMapTest {
    @Test
    public void testIfMapWorks(){
        WorldMap map = new AbstractWorldMap(10, 5);
        Animal animal = new Animal(MapDirection.SOUTH, new Vector2d(2,3), List.of(1,1), 100);
        try {
            map.place(animal);
        } catch (IncorrectPositionException e) {
            fail("unexpected exception: " + e.getMessage());
        }
        assertEquals(new Vector2d(2, 3), animal.getPosition());
    }

    @Test
    public void testCanMoveTo(){
        WorldMap map = new AbstractWorldMap(10, 5);

        assertTrue(map.canMoveTo(new Vector2d(2, 3)));
        assertFalse(map.canMoveTo(new Vector2d(20, 2)));
    }

    @Test
    public void testPlace(){
        WorldMap map = new AbstractWorldMap(10, 5);
        Animal animal1 = new Animal(MapDirection.SOUTH, new Vector2d(2,3), List.of(1,1), 100);
        Animal animal2 = new Animal(MapDirection.SOUTH, new Vector2d(2,30), List.of(1,1), 100);
        try {
            assertTrue(map.place(animal1));
        }catch (IncorrectPositionException e){
            fail("unexpected exception: " + e.getMessage());
        }
        assertThrows(IncorrectPositionException.class, () -> map.place(animal2));
    }

    @Test
    public void testMove(){
        WorldMap map = new AbstractWorldMap(10, 5);
        Animal animal1 = new Animal(MapDirection.NORTH, new Vector2d(0,0), List.of(1,1), 100);
        Animal animal2 = new Animal(MapDirection.SOUTH, new Vector2d(2,3), List.of(1,1), 100);
        try {
            map.place(animal1);
            map.place(animal2);
        }catch (IncorrectPositionException e){
            fail("unexpected exception: " + e.getMessage());
        }

        map.move(animal1, MapDirection.SOUTH);
        map.move(animal2, MapDirection.SOUTH);

        assertEquals(new Vector2d(0, 10), animal1.getPosition());
        assertEquals(new Vector2d(2,2), animal2.getPosition());
        assertEquals(0, animal1.getEnergyLevel());
    }


    @Test
    public void testIsOccupied(){
        WorldMap map = new AbstractWorldMap(10, 5);
        Animal animal1 = new Animal(MapDirection.SOUTH, new Vector2d(0,0), List.of(1,1), 100);
        try {
            map.place(animal1);
        }catch (IncorrectPositionException e){
            fail("unexpected exception: " + e.getMessage());
        }
        assertTrue(map.isOccupied(new Vector2d(0, 0)));
        assertFalse(map.isOccupied(new Vector2d(2, 2)));
    }

    @Test
    public void testObjectAt(){
        WorldMap map = new AbstractWorldMap(10, 5);
        Animal animal1 = new Animal(MapDirection.SOUTH, new Vector2d(0,0), List.of(1,1), 100);
        Animal animal2 = new Animal(MapDirection.SOUTH, new Vector2d(2,3), List.of(1,1), 100);
        try {
            map.place(animal1);
            map.place(animal2);
        }catch (IncorrectPositionException e){
            fail("unexpected exception: " + e.getMessage());
        }
        assertEquals(List.of(animal1), map.objectAt(new Vector2d(0, 0)));
        assertEquals(List.of(animal2), map.objectAt(new Vector2d(2, 3)));
    }
  
}