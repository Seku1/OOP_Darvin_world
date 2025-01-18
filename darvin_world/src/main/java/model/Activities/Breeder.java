package model.Activities;

import model.MapElements.Animal.Animal;
import model.MapElements.Animal.OldAnimal;
import model.MapElements.Animal.RegularAnimal;
import java.util.Random;

public class Breeder {
    private final int minToBreed;

    public Breeder(int minToBreed) {
        this.minToBreed = minToBreed;
    }

    public Breeder() {
        this.minToBreed = 100;
    }

    public Animal breed(Animal animal1, Animal animal2) {
        if (animal1 instanceof RegularAnimal && animal2 instanceof RegularAnimal) {
            return new RegularAnimal(
                    animal1.getDirection(),
                    animal1.getPosition(),
                    createGenome(animal1, animal2),
                    splitEnergy(animal1, animal2)
            );
        } else if (animal1 instanceof OldAnimal && animal2 instanceof OldAnimal) {
            return new OldAnimal(
                    animal1.getDirection(),
                    animal1.getPosition(),
                    createGenome(animal1, animal2),
                    splitEnergy(animal1, animal2)
            );
        } else {
            throw new IllegalArgumentException("Animals must be of the same type to breed.");
        }
    }

    private int[] createGenome(Animal animal1, Animal animal2) {
        Random random = new Random();
        if (random.nextBoolean()) {
            Animal temp = animal1;
            animal1 = animal2;
            animal2 = temp;
        }
        int[] parent1Genes = animal1.getGenes();
        int[] parent2Genes = animal2.getGenes();
        int totalEnergy = animal1.getEnergyLevel() + animal2.getEnergyLevel();
        int split = Math.round((float) parent1Genes.length * animal1.getEnergyLevel() / totalEnergy);
        int[] genome = new int[parent1Genes.length];
        System.arraycopy(parent1Genes, 0, genome, 0, split);
        System.arraycopy(parent2Genes, split, genome, split, parent2Genes.length - split);
        return genome;
    }

    private int splitEnergy(Animal animal1, Animal animal2) {
        int energyForDescendant = Math.round((float) animal1.getEnergyLevel() / 3);
        animal1.setEnergyLevel(animal1.getEnergyLevel() - energyForDescendant);
        int energyLoss = Math.round((float) animal2.getEnergyLevel() / 3);
        energyForDescendant += energyLoss;
        animal2.setEnergyLevel(animal2.getEnergyLevel() - energyLoss);
        return energyForDescendant;
    }

    public boolean canBreed(Animal animal1, Animal animal2) {
        return animal1.getEnergyLevel() >= minToBreed && animal2.getEnergyLevel() >= minToBreed;
    }
}
