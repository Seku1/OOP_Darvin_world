package model;

import model.util.Boundary;
import model.util.IncorrectPositionException;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface WorldMap extends MoveValidator {
    /*
     * Place an animal on the map.
     *
     * @param animal The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the move is not valid.
     */
    boolean place(Animal animal) throws IncorrectPositionException;

    /*
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal, MapDirection direction);

    /*
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /*
     * Return an element at a given position.
     *
     * @param position The position of the animal.
     * @return WorldElement or null if the position is not occupied.
     */
    ArrayList<Animal> objectAt(Vector2d position);

    /*
     * Return a collection of all elements (animals and grass) on the map.
     */
    List<Livings> getElements();

    Boundary getCurrentBounds();

    String toString();

    UUID getID();

    void Insertion(Animal animal, ArrayList<Animal> animals);

    void addPlant(Vector2d position, Plant plant);

    void removePlant(Vector2d position);

    List<Animal> getAnimals();

    List<Plant> getPlants();
}
