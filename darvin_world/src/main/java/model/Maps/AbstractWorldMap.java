package model.Maps;

import model.Genes.GeneMutator;
import model.Genes.RegularMutation;
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
import java.util.stream.Stream;

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
    protected Map<int[], Integer> most_popular_genomes = new HashMap<>();
    protected ArrayList<Animal> deadAnimals = new ArrayList<>();

    public AbstractWorldMap(int height, int width, int cost) {
        this.upperRight = new Vector2d(width, height);
        this.height = height;
        this.width = width;
        this.cost = cost;
    }

    public Map<int[], Integer> findMostPopularGenomes() {
        Map<String, Integer> genomeCountMap = new HashMap<>();
        for (ArrayList<Animal> animalList : animals.values()) {
            for (Animal animal : animalList) {
                String genomeKey = Arrays.toString(animal.getGenes());
                genomeCountMap.put(genomeKey, genomeCountMap.getOrDefault(genomeKey, 0) + 1);
            }
        }
        int maxCount = genomeCountMap.values().stream().max(Integer::compare).orElse(0);
        Map<int[], Integer> mostPopularGenomes = new HashMap<>();
        for (Map.Entry<String, Integer> entry : genomeCountMap.entrySet()) {
            if (entry.getValue() == maxCount) {
                int[] genomeArray = Arrays.stream(entry.getKey().substring(1, entry.getKey().length() - 1).split(", "))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                mostPopularGenomes.put(genomeArray, maxCount);
            }
        }
        return mostPopularGenomes;
    }

    public void setMostPopularGenomes() {
        this.most_popular_genomes = findMostPopularGenomes();
    }

    public void mutateAllGenes(GeneMutator mutator) {
        for (ArrayList<Animal> animalList : animals.values()) {
            for (Animal animal : animalList) {
                mutator.mutate(animal.getGenes());
            }
        }
        notifyObservers("All animal genes have been mutated.");
    }

    public void mutateAnimals() {
        int minimumNumberOfMutations = 0;
        Animal randomAnimal = animals.values().stream()
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .findFirst()
                .orElse(null);
        int maximumNumberOfMutations = randomAnimal.getGenes().length;
        int maxValue = 7;
        GeneMutator geneMutator = new RegularMutation(minimumNumberOfMutations,maximumNumberOfMutations,maxValue);
        mutateAllGenes(geneMutator);
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
    public void removeDeadAnimals(int day) {
        List<Vector2d> positionsToRemove = new ArrayList<>();
        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : animals.entrySet()) {
            Vector2d position = entry.getKey();
            ArrayList<Animal> animalList = entry.getValue();
            for (int i = animalList.size() - 1; i >= 0; i--) {
                if (animalList.get(i).getEnergyLevel() <= 0) {
                    animalList.get(i).setDayOfDeath(day-1);
                    deadAnimals.add(animalList.get(i));
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
    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        animal.setNewDirection(animal.getActiveGenom());
        MapDirection direction = animal.getDirection();
        Vector2d newPosition = newPosition(oldPosition,direction.toUnitVector());
        if (canMoveTo(newPosition)) {
            if (animal.getEnergyLevel() - cost >= 0) {
                moveHelper(animal, direction, oldPosition, newPosition);
            } else {
                animal.setEnergyLevel(0);
            }
        }
    }

    @Override
    public Vector2d newPosition(Vector2d position, Vector2d movement) {
        int x = position.getX() + movement.getX();
        int y = position.getY() + movement.getY();
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

        elements.addAll(plants.values());
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

    public List<Vector2d> getOccupiedPositions(){
        return Stream.concat(
                animals.keySet().stream().filter(position -> objectAt(position).isPresent()),
                plants.keySet().stream()
        ).distinct().collect(Collectors.toList());
    }

    public double getAverageEnergyLevel() {
        return getAnimals().stream().mapToDouble(Animal::getEnergyLevel).average().orElse(0.0);
    }

    public double getAverageLifeSpanOfDeadAnimals() {
        return deadAnimals.stream()
                .mapToInt(Animal::getLiveDays)
                .average()
                .orElse(0.0);
    }

    public double getAverageNumberOfChildren() {
        return getAnimals().stream()
                .mapToInt(Animal::getChildren)
                .average()
                .orElse(0.0);
    }
}