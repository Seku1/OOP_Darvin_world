package model.MapElements.Animal;

import model.Others.MapDirection;
import model.Others.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTest {
    MapDirection direction = MapDirection.NORTH;
    Vector2d position = new Vector2d(1,1);
    int [] genes = {1,2,3,4,5,6,7};
    int energyLevel = 10;
    Animal animal1 = new RegularAnimal(direction,position,genes,energyLevel);

    @Test
    public void testAnimalConstructor() {
        assertEquals(direction,animal1.getDirection());
        assertEquals(position,animal1.getPosition());
        assertEquals(genes,animal1.getGenes());
        assertEquals(energyLevel,animal1.getEnergyLevel());
    }

    @Test
    public void setActiveGenomTest() {
        Random random = new Random();
        animal1.setActiveGenom(genes[random.nextInt(genes.length)]);
        assertEquals(genes[random.nextInt(genes.length)],animal1.getActiveGenom());
    }
}
