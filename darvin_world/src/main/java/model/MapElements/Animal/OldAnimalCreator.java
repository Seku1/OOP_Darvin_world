package model.MapElements.Animal;

import model.Maps.AbstractWorldMap;
import model.Others.MapDirection;
import model.Others.Vector2d;

public class OldAnimalCreator extends AbstractAnimalCreator {
    int genomeSize;
    int energyLevel;
    public OldAnimalCreator(AbstractWorldMap map, int genomeSize, int energyLevel) {
        super(map);
        this.genomeSize = genomeSize;
        this.energyLevel = energyLevel;
    }

    @Override
    public Animal createAnimal(Vector2d position) {
        return new OldAnimal(MapDirection.NORTH,position,generateRandomGenes(genomeSize),energyLevel);
    }
}
