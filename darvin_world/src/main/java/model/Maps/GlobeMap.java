package model.Maps;

import model.Others.Vector2d;

public class GlobeMap extends AbstractWorldMap {
    public GlobeMap(int height, int width, int cost) {
        super(height, width, cost);
    }

    @Override
    public Vector2d newPosition(Vector2d position, Vector2d movement) {
        int x = (position.getX() + movement.getX() + width + 1)%(width+1);
        int y = (position.getY() + movement.getY())%height;
        return new Vector2d(x, y);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.followsOnTheGlobe(this.lowerLeft) && position.precedesOnTheGlobe(this.upperRight);
    }
}
