package model.MapElements.Animal;

import model.Maps.AbstractWorldMap;
import model.Others.Vector2d;
import model.Util.IncorrectPositionException;
import model.Util.RandomPositionGenerator;
import java.util.Optional;
import java.util.Random;

public abstract class AbstractAnimalCreator implements AnimalCreator {
    protected final AbstractWorldMap map;
    private final RandomPositionGenerator positionGenerator;
    protected final Random random = new Random();

    protected AbstractAnimalCreator(AbstractWorldMap map) {
        this.map = map;
        this.positionGenerator = new RandomPositionGenerator(map);
    }

    private void addAnimals(int animalCount) {
        for (int i = 0; i < animalCount; i++) {
            Optional<Vector2d> position = positionGenerator.getRandomFreePosition();
            if (position.isPresent()) {
                Animal animal = createAnimal(position.get());
                try {
                    if (map.place(animal)) {
                        positionGenerator.removePosition(position.get());
                    }
                } catch (IncorrectPositionException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
    }

    protected int[] generateRandomGenes(int genomeSize) {
        int[] genes = new int[genomeSize];
        for (int i = 0; i < genomeSize; i++) {
            genes[i] = random.nextInt(8);
        }
        return genes;
    }

    public void addAnimalsAtTheBeginning(int startingAnimalCount) {
        addAnimals(startingAnimalCount);
    }

    public abstract Animal createAnimal(Vector2d position);
}
