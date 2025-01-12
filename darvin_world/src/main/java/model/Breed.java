package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Breed {
    public Animal breed(Animal animal1, Animal animal2) {
        return new Animal(animal1.getPosition(), createGenome(animal1, animal2), splitEnergy(animal1, animal2));
    }

    private ArrayList<Integer> createGenome(Animal animal1, Animal animal2) {

        Random random = new Random();
        if (random.nextBoolean()) {
            Animal temp = animal1;
            animal1 = animal2;
            animal2 = temp;
        }

        int split = Math.round((float) animal1.getGenes().size() * animal1.getEnergyLevel() / (animal1.getEnergyLevel() + animal2.getEnergyLevel()));

        ArrayList<Integer> genome =  new ArrayList<>(IntStream.range(0, split)
                .mapToObj(animal1.getGenes()::get)
                .toList());

        genome.addAll(IntStream.range(split, animal2.getGenes().size()).mapToObj(animal2.getGenes()::get).toList());
        return genome;
    }

    private int splitEnergy(Animal animal1, Animal animal2) {
        int descendant = Math.round((float) animal1.getEnergyLevel() / 3);
        animal1.setEnergyLevel(animal1.getEnergyLevel() - descendant);
        int energyLoss = Math.round((float) animal2.getEnergyLevel() / 3);
        descendant += energyLoss;
        animal2.setEnergyLevel(animal2.getEnergyLevel() - energyLoss);
        return descendant;
    }
}
