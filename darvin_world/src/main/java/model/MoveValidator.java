package model;

import model.Vector2d;

public interface MoveValidator {
    Vector2d newPosition(Vector2d move);
    boolean canMoveTo(Vector2d position);
}
