package model;

import model.util.Boundary;
import model.util.IncorrectPositionException;
import model.util.MapVisualizer;

import java.util.*;

public class AbstractWorldMap implements WorldMap {
    protected final UUID id = UUID.randomUUID();
    protected final int cost;
    private final int height;
    private final int width;
    protected Vector2d lowerLeft = new Vector2d(0, 0);
    protected Vector2d upperRight;
    protected Vector2d upperBound;
    protected Vector2d lowerBound = new Vector2d(0, 0);
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final List<MapChangeListener> observers = new ArrayList<>();
    private final Map<Vector2d, ArrayList<Animal>> animals1 = new HashMap<>();

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

    private void Insertion(Animal animal, ArrayList<Animal> animals) {
        int i = 0;
        while (animal.getEnergyLevel() < animals.get(i).getEnergyLevel()) {
            i++;
        }
        animals.add(i, animal);
    }

    @Override
    public boolean place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPosition())) {
            Insertion(animal,animals1.get(animal.getPosition()));
            notifyObservers("Animal placed: " + animal.getPosition());
            return true;
        }
        throw new IncorrectPositionException(animal.getPosition());
    }

    @Override
    public void move(Animal animal, MapDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(direction, this);
        animals.remove(oldPosition);
        animals.put(animal.getPosition(), animal);
        notifyObservers("Animal moved to: " + animal.getPosition() + " from: " + oldPosition);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public Livings objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public List<Livings> getElements() {
        List<Livings> elements = new ArrayList<>(animals.values());
        // Add other elements if needed
        return elements;
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(lowerLeft, upperRight);
    }

    @Override
    public UUID getID() {
        return null;
    }

    @Override
    public Vector2d newPosition(Vector2d position) {
        if (!position.follows(this.lowerLeft) || !position.precedes(this.upperRight)) {
            position = new Vector2d(position.getX() % width, position.getY() % height);
        }
        return position;
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