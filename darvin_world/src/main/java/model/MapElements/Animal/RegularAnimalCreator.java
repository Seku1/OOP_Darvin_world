package model.MapElements.Animal;

import model.Maps.AbstractWorldMap;
import model.Others.MapDirection;
import model.Others.Vector2d;

public class RegularAnimalCreator extends AbstractAnimalCreator {
    public RegularAnimalCreator(AbstractWorldMap map, int startingAnimalEnergy, int genomeSize) {
        super(map, startingAnimalEnergy, genomeSize);
    }

    @Override
    public Animal createAnimal(Vector2d position) {
        return new RegularAnimal(MapDirection.NORTH,position,generateRandomGenes(genomeSize),animalStartingEnergy);
    }
}
