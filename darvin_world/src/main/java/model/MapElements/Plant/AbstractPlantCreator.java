package model.MapElements.Plant;

import model.Maps.AbstractWorldMap;
import model.Others.Vector2d;
import model.Util.RandomPositionGenerator;
import java.util.Optional;

public abstract class AbstractPlantCreator implements PlantCreator {
    protected final AbstractWorldMap map;
    private final RandomPositionGenerator positionGenerator;

    protected AbstractPlantCreator(AbstractWorldMap map) {
        this.map = map;
        this.positionGenerator = new RandomPositionGenerator(map);
    }

    private void addPlants(int plantCount) {
        for (int i = 0; i < plantCount; i++) {
            Optional<Vector2d> position = positionGenerator.getRandomFreePosition();
            if (position.isPresent()) {
                Plant plant = createPlant(position.get());
                map.addPlant(position.get(), plant);
                positionGenerator.removePosition(position.get());
            } else {
                break;
            }
        }
    }

    public void addPlantsAtTheBeginning(int startingGrassCount) {
        addPlants(startingGrassCount);
    }

    public void addPlantsDaily(int dailyPlantCount) {
        addPlants(dailyPlantCount);
    }

    public abstract Plant createPlant(Vector2d position);
}
