package model.Simulations;

import model.Activities.Breeder;
import model.MapElements.Animal.Animal;
import model.MapElements.Plant.AbstractPlantCreator;
import model.MapElements.Animal.AbstractAnimalCreator;
import model.Maps.AbstractWorldMap;
import model.Others.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final AbstractWorldMap map;
    private final AbstractPlantCreator plantCreator;
    private final AbstractAnimalCreator animalCreator;
    private final Breeder breeding;
    private final int startingAnimalCount;
    private final int startingPlantCount;
    private final int energyPerPlant;
    private int dayNumber = 0;
    private boolean running = true;
    private int threadSleepTime = 700;

    public Simulation(AbstractWorldMap map, AbstractPlantCreator plantCreator, Breeder breeding, AbstractAnimalCreator animalCreator, int startingAnimalCount, int startingPlantCount, int energyPerPlant) {
        this.map = map;
        this.plantCreator = plantCreator;
        this.animalCreator = animalCreator;
        this.breeding = breeding;
        this.startingAnimalCount = startingAnimalCount;
        this.startingPlantCount = startingPlantCount;
        this.energyPerPlant = energyPerPlant;
        initializeMap();
    }

    private void initializeMap() {
        plantCreator.addPlantsAtTheBeginning(startingPlantCount);
        animalCreator.addAnimalsAtTheBeginning(startingAnimalCount);
    }

    public void pause() {
        running = false;
    }

    public void play() {
        running = true;
    }

    public void setThreadSleep(int time) {
        threadSleepTime = time;
    }

    @Override
    public void run() {
        try {
            while (!map.getAnimals().isEmpty() && running) {
                Thread.sleep(threadSleepTime);
                dayNumber++;
                runDay();
                map.notifyObservers("Day " + dayNumber);
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private void runDay() {
        map.removeDeadAnimals();
        moveAnimals();
        feedAnimals();
        breedAnimals();
        plantCreator.addPlantsDaily(5);
    }

    private void moveAnimals() {
        for (Animal animal : map.getAnimals()) {
            map.move(animal, animal.getDirection());
        }
    }

    private void feedAnimals() {
        List<Vector2d> plantsToRemove = new ArrayList<>();
        map.getPlantMap().forEach((position, plant) -> {
            List<Animal> animalsAtPosition = map.getAnimalsAtPosition(position);
            if (!animalsAtPosition.isEmpty()) {
                animalsAtPosition.get(0).addEnergy(energyPerPlant);
                plantsToRemove.add(position);
            }
        });

        for (Vector2d position : plantsToRemove) {
            map.removePlant(position);
        }
    }


    private void breedAnimals() {
        map.getAnimalPositions().forEach(position -> {
            List<Animal> animalsAtPosition = map.getAnimalsAtPosition(position);
            if (animalsAtPosition.size() >= 2) {
                Animal parent1 = animalsAtPosition.get(0);
                Animal parent2 = animalsAtPosition.get(1);
                if (breeding.canBreed(parent1, parent2)) {
                    Animal child = breeding.breed(parent1, parent2);
                    map.insertAnimal(position, child); // Add new animal to the map
                }
            }
        });
    }
}
