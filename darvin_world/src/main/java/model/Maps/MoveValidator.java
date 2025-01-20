package model.Maps;

import model.Others.Vector2d;

public interface MoveValidator {
    Vector2d newPosition(Vector2d position, Vector2d movement);
    boolean canMoveTo(Vector2d position);
}