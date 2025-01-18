package model.MapElements.Plant;

import model.Others.Vector2d;

public interface PlantCreator {
    void addPlantsAtTheBeginning(int startingGrassCount);
    void addPlantsDaily(int dailyPlantCount);
}
