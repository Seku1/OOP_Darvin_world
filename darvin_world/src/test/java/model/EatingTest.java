package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EatingTest {
    private Eating eating;
    private Map<Vector2d, ArrayList<Animal>> animals;

    @BeforeEach
    void setUp() {
        eating = new Eating(10);
        animals = new HashMap<>();
    }

    @Test
    void testBestEnergy() {
        Vector2d position = new Vector2d(2, 3);
        Animal animal1 = new Animal(70);
        Animal animal2 = new Animal(70);
        Animal animal3 = new Animal(50);
        animals.put(position, new ArrayList<>(List.of(animal1, animal2, animal3)));

        ArrayList<Animal> result = eating.BestEnergy(position, animals);

        assertEquals(2, result.size());
        assertTrue(result.contains(animal1));
        assertTrue(result.contains(animal2));
        assertFalse(result.contains(animal3));
    }

    @Test
    void testBestEnergyEmptyPosition() {
        Vector2d position = new Vector2d(2, 3);
        ArrayList<Animal> result = eating.BestEnergy(position, animals);
        assertTrue(result.isEmpty());
    }

    @Test
    void testOldest() {
        Animal animal1 = new Animal(50, 10);
        Animal animal2 = new Animal(50, 20);
        Animal animal3 = new Animal(50, 20);
        ArrayList<Animal> animalList = new ArrayList<>(List.of(animal1, animal2, animal3));

        ArrayList<Animal> result = eating.Oldest(animalList);

        assertEquals(2, result.size());
        assertTrue(result.contains(animal2));
        assertTrue(result.contains(animal3));
    }

    @Test
    void testChildrenCount() {
        Animal animal1 = new Animal(50, 10, 2);
        Animal animal2 = new Animal(70, 20, 5);
        Animal animal3 = new Animal(30, 15, 5);
        ArrayList<Animal> animalList = new ArrayList<>(List.of(animal1, animal2, animal3));

        ArrayList<Animal> result = eating.ChildrenCount(animalList);

        assertEquals(2, result.size());
        assertTrue(result.contains(animal2));
        assertTrue(result.contains(animal3));
    }

    @Test
    void testFirstToEatSingleAnimal() {
        Vector2d position = new Vector2d(2, 3);
        Animal animal = new Animal(50);
        animals.put(position, new ArrayList<>(List.of(animal)));

        eating.FirstToEat(animals);

        assertEquals(60, animal.getEnergyLevel());
    }

    @Test
    void testFirstToEatMultipleAnimals() {
        Vector2d position = new Vector2d(2, 3);
        Animal animal1 = new Animal(50, 20, 2);
        Animal animal2 = new Animal(50, 20, 3);
        animals.put(position, new ArrayList<>(List.of(animal1, animal2)));

        eating.FirstToEat(animals);

        assertEquals(50, animal1.getEnergyLevel());
        assertEquals(60, animal2.getEnergyLevel());
    }

    @Test
    void testFirstToEatTieBreaker() {
        Vector2d position = new Vector2d(2, 3);
        Animal animal1 = new Animal(70, 10, 2);
        Animal animal2 = new Animal(70, 10, 2);
        animals.put(position, new ArrayList<>(List.of(animal1, animal2)));

        eating.FirstToEat(animals);

        assertTrue(animal1.getEnergyLevel() == 80 || animal2.getEnergyLevel() == 80);
        assertTrue(animal1.getEnergyLevel() == 70 || animal2.getEnergyLevel() == 70);
    }
}
