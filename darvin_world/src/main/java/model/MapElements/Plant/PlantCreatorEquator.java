package model.MapElements.Plant;

import model.Maps.AbstractWorldMap;
import model.Others.Vector2d;
import java.util.Optional;
import java.util.Random;

public class PlantCreatorEquator extends AbstractPlantCreator {
    private final Random random = new Random();

    public PlantCreatorEquator(AbstractWorldMap map) {
        super(map);
    }

    private void addPlants(int plantCount) {
        for (int i = 0; i < plantCount; i++) {
            Optional<Vector2d> position = getPreferredPositionForPlant();
            if (position.isPresent()) {
                Plant plant = createPlant(position.get());
                map.addPlant(position.get(), plant);
                positionGenerator.removePosition(position.get()); // Używanie positionGenerator z klasy bazowej
            } else {
                break;
            }
        }
    }

    @Override
    public void addPlantsAtTheBeginning(int startingGrassCount) {
        addPlants(startingGrassCount);
    }

    @Override
    public void addPlantsDaily(int dailyPlantCount) {
        addPlants(dailyPlantCount);
    }

    private Optional<Vector2d> getPreferredPositionForPlant() {
        Vector2d preferredPosition = null;
        int equatorY = map.getHight() / 2;

        for (int x = map.getLowerLeft().getX(); x <= map.getUpperRight().getX(); x++) {
            int y = equatorY + (random.nextInt(3) - 1);

            Vector2d position = new Vector2d(x, y);
            if (map.getPlantMap().containsKey(position)) {
                continue;
            }

            preferredPosition = position;
            break;
        }

        if (preferredPosition == null) {
            return positionGenerator.getRandomFreePosition(); // Jeśli nie ma preferowanej pozycji, używamy generatora
        } else {
            return Optional.of(preferredPosition);
        }
    }

    @Override
    public Plant createPlant(Vector2d position) {
        return new Plant(position);
    }
}
