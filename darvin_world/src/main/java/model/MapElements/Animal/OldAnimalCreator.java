package model.MapElements.Animal;

import model.Maps.AbstractWorldMap;
import model.Others.MapDirection;
import model.Others.Vector2d;

public class OldAnimalCreator extends AbstractAnimalCreator {

    public OldAnimalCreator(AbstractWorldMap map, int animalStartingEnergy, int genomeSize) {
        super(map, animalStartingEnergy, genomeSize);
    }

    @Override
    public Animal createAnimal(Vector2d position) {
        return new OldAnimal(MapDirection.NORTH,position,generateRandomGenes(genomeSize), animalStartingEnergy);
    }
}
