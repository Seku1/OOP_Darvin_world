package model.MapElements.Plant;

import model.Maps.AbstractWorldMap;
import model.Others.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PlantCreatorEquator extends AbstractPlantCreator {
    private final Random random = new Random();
    private final int equatorStart;
    private final int equatorEnd;

    public PlantCreatorEquator(AbstractWorldMap map) {
        super(map);
        int mapHeight = map.getHight();
        int equatorHeight = (int) Math.ceil(mapHeight * 0.2); // 20% wysokości mapy
        equatorStart = (mapHeight - equatorHeight) / 2; // Start równika
        equatorEnd = equatorStart + equatorHeight - 1; // Koniec równika
    }

    @Override
    public void addPlantsAtTheBeginning(int startingGrassCount) {
        addPlants(startingGrassCount);
    }

    @Override
    public void addPlantsDaily(int dailyPlantCount) {
        addPlants(dailyPlantCount);
    }

    private void addPlants(int plantCount) {
        int equatorPlantCount = (int) Math.round(plantCount * 0.65); // 65% na równiku
        int otherPlantCount = plantCount - equatorPlantCount; // Reszta poza równikiem

        // Dodaj rośliny na równiku
        addPlantsToRegion(equatorPlantCount, equatorStart, equatorEnd);

        // Dodaj rośliny poza równikiem
        addPlantsToNonEquatorRegions(otherPlantCount);
    }

    private void addPlantsToRegion(int plantCount, int startY, int endY) {
        List<Vector2d> possiblePositions = new ArrayList<>();

        for (int x = map.getLowerLeft().getX(); x <= map.getUpperRight().getX(); x++) {
            for (int y = startY; y <= endY; y++) {
                Vector2d position = new Vector2d(x, y);
                if (!map.getPlantMap().containsKey(position)) {
                    possiblePositions.add(position);
                }
            }
        }

        placePlantsRandomly(plantCount, possiblePositions);
    }

    private void addPlantsToNonEquatorRegions(int plantCount) {
        List<Vector2d> possiblePositions = new ArrayList<>();

        for (int x = map.getLowerLeft().getX(); x <= map.getUpperRight().getX(); x++) {
            for (int y = map.getLowerLeft().getY(); y < equatorStart; y++) {
                Vector2d position = new Vector2d(x, y);
                if (!map.getPlantMap().containsKey(position)) {
                    possiblePositions.add(position);
                }
            }
            for (int y = equatorEnd + 1; y <= map.getUpperRight().getY(); y++) {
                Vector2d position = new Vector2d(x, y);
                if (!map.getPlantMap().containsKey(position)) {
                    possiblePositions.add(position);
                }
            }
        }

        placePlantsRandomly(plantCount, possiblePositions);
    }

    private void placePlantsRandomly(int plantCount, List<Vector2d> positions) {
        int addedPlants = 0;

        while (addedPlants < plantCount && !positions.isEmpty()) {
            int randomIndex = random.nextInt(positions.size());
            Vector2d position = positions.remove(randomIndex);

            Plant plant = createPlant(position);
            map.addPlant(position, plant);
            addedPlants++;
        }
    }

    @Override
    public Plant createPlant(Vector2d position) {
        return new Plant(position);
    }

    public int getEquatorStart() {
        return equatorStart;
    }

    public int getEquatorEnd() {
        return equatorEnd;
    }
}
