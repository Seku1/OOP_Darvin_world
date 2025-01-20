package model.Simulations;

import model.MapElements.Plant.PlantCreatorEquator;
import model.Maps.AbstractWorldMap;
import model.MapElements.Animal.RegularAnimalCreator;
import model.MapElements.Plant.AbstractPlantCreator;
import model.Activities.Breeder;
import model.Activities.Eating;
import model.Maps.GlobeMap;

public class Main {
    public static void main(String[] args) {
        int mapHeight = 10;
        int mapWidth = 10;
        int animalCost = 1;
        int startingAnimalCount = 2;
        int startingPlantCount = 10;
        int dailyPlantCount = 3;
        int energyPerPlant = 5;
        int minEnergyToBreed = 100;

        AbstractWorldMap map = new GlobeMap(mapHeight, mapWidth, animalCost);
        Breeder breeder = new Breeder(minEnergyToBreed);
        Eating eating = new Eating(energyPerPlant);
        RegularAnimalCreator animalCreator = new RegularAnimalCreator(map,20,7);
        AbstractPlantCreator plantCreator = new PlantCreatorEquator(map);

        // Inicjalizacja symulacji
        Simulation simulation = new Simulation(
                map,
                plantCreator,
                breeder,
                animalCreator,
                startingAnimalCount,
                startingPlantCount,
                energyPerPlant,
                5
        );

        // Wyświetlenie stanu początkowego
        System.out.println("Stan początkowy mapy:");
        System.out.println(map);

        // Uruchomienie symulacji
        new Thread(simulation).start();

        // Pozwól symulacji działać przez 10 sekund, następnie zakończ
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Pauza i zakończenie symulacji
        simulation.pause();
        System.out.println("Symulacja zakończona.");
        System.out.println("Stan końcowy mapy:");
        System.out.println(map);
    }
}
