package model.MapElements.Animal;

import model.Maps.AbstractWorldMap;
import model.Maps.GlobeMap;
import model.Others.MapDirection;
import model.Others.Vector2d;

import java.util.Arrays;

public class WorldAnimal {
    public static void main(String[] args) {
        Animal animal = new RegularAnimal(MapDirection.NORTH,new Vector2d(1,1), new int[] {1, 2, 3, 4, 5, 6, 7},20);
        MapDirection direction = animal.getDirection();
        Vector2d position = animal.getPosition();
        int [] genes = animal.getGenes();
        int energyLevel = animal.getEnergyLevel();
        System.out.println("Animal parameters:");
        System.out.println("direction: " + direction);
        System.out.println("position: " + position);
        System.out.println("genes: " + Arrays.toString(genes));
        System.out.println("energyLevel: " + energyLevel + "/n");
        animal.setEnergyLevel(50);
        System.out.println("After setting energy to 50: " + animal.getEnergyLevel());
        animal.setActiveGenom(genes.length);
        System.out.println("After setting activeGenom to " + animal.getActiveGenom());
  //      animal.setNewDirection(animal.getDirection(),animal.getGenes()[animal.getActiveGenom()]);
        System.out.println("New direction after turn: " + animal.getDirection());
        animal.incrementActiveGenom();
        System.out.println("After incrementing activeGenom to " + animal.getActiveGenom());
        animal.addEnergy(20);
        System.out.println("After adding 20 energy: " + animal.getEnergyLevel());
        AbstractWorldMap map = new GlobeMap(10,10,5);
        animal.move(animal.getDirection(),map);
        System.out.println("After moving " + animal.getDirection() + " to " + animal.getPosition());
    }
}
