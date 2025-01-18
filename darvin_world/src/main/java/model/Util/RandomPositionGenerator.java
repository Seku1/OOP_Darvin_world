package model.Util;

import model.Maps.AbstractWorldMap;
import model.Others.Vector2d;
import java.util.*;

public class RandomPositionGenerator {
    private final AbstractWorldMap map;
    private final List<Vector2d> availablePositions;
    private final Random random;

    public RandomPositionGenerator(AbstractWorldMap map) {
        this.map = map;
        this.availablePositions = new ArrayList<>();
        this.random = new Random();
        initializeAvailablePositions();
    }

    private void initializeAvailablePositions() {
        for (int x = map.getLowerLeft().getX(); x <= map.getUpperRight().getX(); x++) {
            for (int y = map.getLowerLeft().getY(); y <= map.getUpperRight().getY(); y++) {
                Vector2d position = new Vector2d(x, y);
                if (!map.getPlantMap().containsKey(position)) {
                    availablePositions.add(position);
                }
            }
        }
    }

    public Optional<Vector2d> getRandomFreePosition() {
        if (availablePositions.isEmpty()) {
            return Optional.empty();
        }
        int index = random.nextInt(availablePositions.size());
        return Optional.of(availablePositions.get(index));
    }

    public void removePosition(Vector2d position) {
        availablePositions.remove(position);
    }

    public void addPosition(Vector2d position) {
        if (!map.getPlantMap().containsKey(position) && !availablePositions.contains(position)) {
            availablePositions.add(position);
        }
    }
}
