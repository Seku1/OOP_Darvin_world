package model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BreedTest {
    @Test
    void splitGenomeTest(){
        Animal animal1 = new Animal(new Vector2d(1,1), List.of(1,2,3,4,5,6), 120);
        Animal animal2 = new Animal(new Vector2d(1,1), List.of(5,6,4,3,2,1), 180);

        Breed breed = new Breed();

        Animal animal3 = breed.breed(animal1, animal2);

        assertEquals(80, animal1.getEnergyLevel());
        assertEquals(120, animal2.getEnergyLevel());
        assertEquals(100, animal3.getEnergyLevel());
    }

    @Test
    void createGenomeTest1(){
        Animal animal1 = new Animal(new Vector2d(1,1), List.of(1,2,3,4,5,6), 100);
        Animal animal2 = new Animal(new Vector2d(1,1), List.of(5,6,4,3,2,1), 100);

        Breed breed = new Breed();

        Animal animal3 = breed.breed(animal1, animal2);

        System.out.println(Arrays.toString(animal3.getGenes().toArray()));
    }

    @Test
    void createGenomeTest2(){
        Animal animal1 = new Animal(new Vector2d(1,1), List.of(1,2,3,4,5,6), 450);
        Animal animal2 = new Animal(new Vector2d(1,1), List.of(5,6,4,3,2,1), 100);

        Breed breed = new Breed();

        Animal animal3 = breed.breed(animal1, animal2);

        System.out.println(Arrays.toString(animal3.getGenes().toArray()));
    }
}