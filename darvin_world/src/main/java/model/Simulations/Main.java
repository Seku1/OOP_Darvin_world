package model.Simulations;

import model.MapElements.Plant.PlantCreatorEquator;
import model.Maps.AbstractWorldMap;
import model.MapElements.Animal.RegularAnimalCreator; // Implementacja tworzenia zwierząt
import model.MapElements.Plant.AbstractPlantCreator; // Implementacja tworzenia roślin
import model.Activities.Breeder;
import model.Activities.Eating;
import model.Maps.GlobeMap;

public class Main {
    public static void main(String[] args) {
        // Parametry mapy
        int mapHeight = 10;
        int mapWidth = 10;
        int animalCost = 1;

        // Parametry symulacji
        int startingAnimalCount = 10;
        int startingPlantCount = 5;
        int dailyPlantCount = 3;
        int energyPerPlant = 10;
        int minEnergyToBreed = 20;

        // Tworzenie obiektów mapy i pozostałych elementów
        AbstractWorldMap map = new GlobeMap(mapHeight, mapWidth, animalCost);
        Breeder breeder = new Breeder(minEnergyToBreed);
        Eating eating = new Eating(energyPerPlant);
        RegularAnimalCreator animalCreator = new RegularAnimalCreator(map,50,7);
        AbstractPlantCreator plantCreator = new PlantCreatorEquator(map);

        // Inicjalizacja symulacji
        Simulation simulation = new Simulation(
                map,
                plantCreator,
                breeder,
                animalCreator,
                startingAnimalCount,
                startingPlantCount,
                energyPerPlant
        );

        // Wyświetlenie stanu początkowego
        System.out.println("Stan początkowy mapy:");
        System.out.println(map);

        // Uruchomienie symulacji
        new Thread(simulation).start();

        // Pozwól symulacji działać przez 10 sekund, następnie zakończ
        try {
            Thread.sleep(10000);
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
