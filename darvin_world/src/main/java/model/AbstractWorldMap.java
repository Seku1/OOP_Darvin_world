package model;

import model.util.Boundary;
import model.util.IncorrectPositionException;
import model.util.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;

public class AbstractWorldMap implements WorldMap {
    protected final UUID id = UUID.randomUUID();
    protected final int cost;
    private final int height;
    private final int width;
    protected Vector2d lowerLeft = new Vector2d(0, 0);
    protected Vector2d upperRight;
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final List<MapChangeListener> observers = new ArrayList<>();
    private final Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();

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

    protected void notifyObservers(String message) {

        synchronized (observers) {
            for (MapChangeListener observer : observers) {
                observer.mapChanged(this, message);
            }
        }
    }

    @Override
    public void Insertion(Animal animal, ArrayList<Animal> animals) {
        if (animals.isEmpty()) {
            animals.add(animal);
            return;
        }
        int i = 0;
        while (i < animals.size() && animal.getEnergyLevel() < animals.get(i).getEnergyLevel()) {
            i++;
        }
        animals.add(i, animal);
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
            Insertion(animal, animals.get(position));
            notifyObservers("Animal placed: " + position);
            return true;
        }
        throw new IncorrectPositionException(position);
    }

    @Override
    public void move(Animal animal, MapDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        Vector2d newPosition = newPosition(oldPosition.add(direction.toUnitVector()));
        if (animals.containsKey(oldPosition)) {
            animals.get(oldPosition).remove(animal);
            if (animals.get(oldPosition).isEmpty()) {
                animals.remove(oldPosition);
            }
        }
        animals.putIfAbsent(newPosition, new ArrayList<>());
        Insertion(animal, animals.get(newPosition));
        animal.move(direction, this);
        notifyObservers("Animal moved to: " + newPosition + " from: " + oldPosition);
    }


    @Override
    public Vector2d newPosition(Vector2d position) {
        int x = (position.getX() + width) % width;
        int y = (position.getY() + height) % height;
        return new Vector2d(x, y);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position) && !animals.get(position).isEmpty();
    }

    @Override
    public ArrayList<Animal> objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public List<Livings> getElements() {
        List<Livings> elements = animals.values().stream()
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
    public List<Plant> getPlants() {
        return new ArrayList<>(plants.values());
    }


    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(lowerLeft, upperRight);
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(this.lowerLeft) && position.precedes(this.upperRight);
    }

    @Override
    public String toString() {
        return visualizer.draw(getCurrentBounds().lowerLeft(), getCurrentBounds().upperRight());
    }
}