package model.Maps;

import model.Others.MapDirection;
import model.MapElements.Animal.Animal;
import model.MapElements.MapElement;
import model.MapElements.Plant.Plant;
import model.Others.Vector2d;
import model.Util.Boundary;
import model.Util.IncorrectPositionException;

import java.util.*;

public interface WorldMap {
    boolean place(Animal animal) throws IncorrectPositionException;
    void move(Animal animal);
    boolean isOccupied(Vector2d position);
    Optional<MapElement> objectAt(Vector2d position);
    List<MapElement> getElements();
    Boundary getCurrentBounds();
    String toString();
    UUID getID();
    void insertAnimal(Vector2d position, Animal animal);
    void addPlant(Vector2d position, Plant plant);
    void removePlant(Vector2d position);
    List<Animal> getAnimals();
    Map<Vector2d, Plant> getPlantMap();
    int getHight();
    void removeDeadAnimals();
    List<Vector2d> getAnimalPositions();
    List<Animal> getAnimalsAtPosition(Vector2d position);
    boolean isPlantAt(Vector2d position);
    Plant getPlantAt(Vector2d position);
}
