package model.Others;

import java.util.List;

public enum MapDirection {

    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;
    public static final List<MapDirection> mapDirections = List.of(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST);

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case EAST -> "E";
            case SOUTH_EAST -> "SE";
            case SOUTH -> "S";
            case SOUTH_WEST -> "SW";
            case WEST -> "W";
            case NORTH_WEST -> "NW";
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1, 1);
        };
    }

    public int positionInListOfDirections() {
        return switch (this) {
            case NORTH -> 0;
            case NORTH_EAST -> 1;
            case EAST -> 2;
            case SOUTH_EAST -> 3;
            case SOUTH -> 4;
            case SOUTH_WEST -> 5;
            case WEST -> 6;
            case NORTH_WEST -> 7;
        };
    }

    public MapDirection afterTurnDirection(int turn) {
        return mapDirections.get((this.positionInListOfDirections()+turn)%mapDirections.size());
    }
}