package model.Activities;

import model.MapElements.Animal.Animal;
import model.Others.Vector2d;

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

    public void firstToEat(Map<Vector2d, ArrayList<Animal>> animals) {
        animals.forEach((position, animalList) -> {
            if (animalList.isEmpty()) return;
            Animal eater = animalList.get(0);
            eater.addEnergy(energyPerPlant);
        });
    }
}
