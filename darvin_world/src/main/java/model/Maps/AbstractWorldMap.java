package model.Maps;

import model.Others.MapChangeListener;
import model.Others.MapDirection;
import model.MapElements.Animal.Animal;
import model.MapElements.MapElement;
import model.MapElements.Plant.Plant;
import model.Others.Vector2d;
import model.Util.Boundary;
import model.Util.IncorrectPositionException;
import model.Util.MapVisualizer;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractWorldMap implements WorldMap, MoveValidator {
    protected final UUID id = UUID.randomUUID();
    protected final int cost;
    protected final int height;
    protected final int width;
    protected Vector2d lowerLeft = new Vector2d(0, 0);
    protected Vector2d upperRight;
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    protected final Map<Vector2d, Plant> plants = new HashMap<>();

    public AbstractWorldMap(int height, int width, int cost) {
        this.upperRight = new Vector2d(width, height);
        this.height = height;
        this.width = width;
        this.cost = cost;
    }

    public AbstractWorldMap(int height, int width) {
        this.upperRight = new Vector2d(width, height);
        this.height = height;
        this.width = width;
        this.cost = 100;
    }

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {

        synchronized (observers) {
            for (MapChangeListener observer : observers) {
                observer.mapChanged(this, message);
            }
        }
    }

    @Override
    public void insertAnimal(Vector2d position, Animal animal) {
        animals.computeIfAbsent(position, k -> new ArrayList<>()).add(animal);
        animals.get(position).sort(Comparator
                .comparingInt(Animal::getEnergyLevel).reversed()
                .thenComparingInt(Animal::getLiveDays).reversed()
                .thenComparingInt(Animal::getChildren).reversed());
    }

    @Override
    public void removeDeadAnimals() {
        List<Vector2d> positionsToRemove = new ArrayList<>();
        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : animals.entrySet()) {
            Vector2d position = entry.getKey();
            ArrayList<Animal> animalList = entry.getValue();
            for (int i = animalList.size() - 1; i >= 0; i--) {
                if (animalList.get(i).getEnergyLevel() <= 0) {
                    animalList.remove(i);
                } else {
                    break;
                }
            }
            if (animalList.isEmpty()) {
                positionsToRemove.add(position);
            }
        }
        for (Vector2d position : positionsToRemove) {
            animals.remove(position);
        }
        notifyObservers("Dead animals removed from the map");
    }



    @Override
    public void addPlant(Vector2d position, Plant plant) {
        plants.put(position, plant);
        notifyObservers("Plant added at: " + position);
    }

    @Override
    public void removePlant(Vector2d position) {
        if (plants.containsKey(position)) {
            plants.remove(position);
            notifyObservers("Plant removed from: " + position);
        }
    }

    @Override
    public boolean place(Animal animal) throws IncorrectPositionException {
        Vector2d position = animal.getPosition();
        if (canMoveTo(position)) {
            animals.putIfAbsent(position, new ArrayList<>());
            insertAnimal(position, animal);
            notifyObservers("Animal placed: " + position);
            return true;
        }
        throw new IncorrectPositionException(position);
    }

    public void moveHelper(Animal animal, MapDirection direction, Vector2d oldPosition, Vector2d newPosition) {
        if (animals.containsKey(oldPosition)) {
            animals.get(oldPosition).remove(animal);
            if (animals.get(oldPosition).isEmpty()) {
                animals.remove(oldPosition);
            }
        }
        animals.putIfAbsent(newPosition, new ArrayList<>());
        insertAnimal(newPosition, animal);
        animal.move(direction, this);
        animal.setEnergyLevel(animal.getEnergyLevel() - cost);
        animal.incrementActiveGenom();
        notifyObservers("Animal moved to: " + newPosition + " from: " + oldPosition);
    }

    @Override
    public void move(Animal animal, MapDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        animal.setNewDirection(animal.getActiveGenom());
        Vector2d newPosition = newPosition(oldPosition.add(direction.toUnitVector()));
        moveHelper(animal, direction, oldPosition, newPosition);
    }


    @Override
    public Vector2d newPosition(Vector2d position) {
        int x = (position.getX() + width + 1) % (width + 1);
        int y = (position.getY() + height + 1) % (height + 1);
        return new Vector2d(x, y);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position) && !animals.get(position).isEmpty();
    }

    public Map<Vector2d, Plant> getPlantMap() {
        return plants;
    }

    @Override
    public List<MapElement> getElements() {
        List<MapElement> elements = animals.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        elements.addAll(plants.values()); // Dodanie roślin
        return elements;
    }

    @Override
    public List<Animal> getAnimals() {
        return animals.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(lowerLeft, upperRight);
    }

    @Override
    public UUID getID() {
        return id;
    }

    public int getHight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(this.lowerLeft) && position.precedes(this.upperRight);
    }

    @Override
    public String toString() {
        return visualizer.draw(getCurrentBounds().lowerLeft(), getCurrentBounds().upperRight());
    }

    public Vector2d getLowerLeft() {
        return lowerLeft;
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    @Override
    public Optional<MapElement> objectAt(Vector2d position) {
        if (animals.containsKey(position) && !animals.get(position).isEmpty()) {
            return Optional.of(animals.get(position).get(0));
        }
        if (plants.containsKey(position)) {
            return Optional.of(plants.get(position));
        }
        return Optional.empty();
    }

    @Override
    public List<Vector2d> getAnimalPositions() {
        return animals.keySet().stream()
                .filter(position -> !animals.get(position).isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public List<Animal> getAnimalsAtPosition(Vector2d position) {
        return animals.getOrDefault(position, new ArrayList<>());
    }

    public boolean isPlantAt(Vector2d position) {
        return plants.containsKey(position);
    }

    public Plant getPlantAt(Vector2d position) {
        return plants.get(position);
    }

    public List<Plant> getPlants() {
        return new ArrayList<>(plants.values());
    }
}