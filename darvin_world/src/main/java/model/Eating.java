package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Eating {
    private final int energyPerPlant;

    public Eating(int energyPerPlant) {
        this.energyPerPlant = energyPerPlant;
    }

    public Eating() {
        this.energyPerPlant = 10;
    }

    public int getEnergyPerPlant() {
        return energyPerPlant;
    }

    public ArrayList<Animal> BestEnergy(Vector2d position, Map<Vector2d, ArrayList<Animal>> animals) {
        if (!animals.containsKey(position) || animals.get(position).isEmpty()) {
            return new ArrayList<>();
        }
        int bestEnergy = animals.get(position).get(0).getEnergyLevel();
        ArrayList<Animal> animalsBestEnergy = new ArrayList<>();
        for (Animal animal : animals.get(position)) {
            int energy = animal.getEnergyLevel();
            if (energy == bestEnergy) {
                animalsBestEnergy.add(animal);
            } else {
                break;
            }
        }
        return animalsBestEnergy;
    }

    public ArrayList<Animal> Oldest(ArrayList<Animal> animals) {
        ArrayList<Animal> animalsOldest = new ArrayList<>();
        int maxAge = 0;

        for (Animal animal : animals) {
            if (animal.getLiveDays() > maxAge) {
                maxAge = animal.getLiveDays();
                animalsOldest.clear();
                animalsOldest.add(animal);
            } else if (animal.getLiveDays() == maxAge) {
                animalsOldest.add(animal);
            }
        }

        return animalsOldest;
    }

    public ArrayList<Animal> ChildrenCount(ArrayList<Animal> animals) {
        ArrayList<Animal> animalsChildrenCount = new ArrayList<>();
        int maxChildrenCount = 0;

        for (Animal animal : animals) {
            if (animal.getChildren() > maxChildrenCount) {
                maxChildrenCount = animal.getChildren();
                animalsChildrenCount.clear();
                animalsChildrenCount.add(animal);
            } else if (animal.getChildren() == maxChildrenCount) {
                animalsChildrenCount.add(animal);
            }
        }

        return animalsChildrenCount;
    }

    public void FirstToEat(Map<Vector2d, ArrayList<Animal>> animals) {
        for (Vector2d position : animals.keySet()) {
            ArrayList<Animal> animalsEat = BestEnergy(position, animals);
            if (animalsEat.isEmpty()) continue;

            if (animalsEat.size() == 1) {
                animalsEat.get(0).addEnergy(energyPerPlant);
            } else {
                animalsEat = Oldest(animalsEat);
                if (animalsEat.size() == 1) {
                    animalsEat.get(0).addEnergy(energyPerPlant);
                } else {
                    animalsEat = ChildrenCount(animalsEat);
                    if (animalsEat.size() == 1) {
                        animalsEat.get(0).addEnergy(energyPerPlant);
                    } else {
                        Collections.shuffle(animalsEat);
                        animalsEat.get(0).addEnergy(energyPerPlant);
                    }
                }
            }
        }
    }
}
