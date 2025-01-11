package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {
    MapDirection md1 = MapDirection.NORTH;
    MapDirection md2 = MapDirection.NORTH_EAST;
    MapDirection md3 = MapDirection.EAST;
    MapDirection md4 = MapDirection.SOUTH_EAST;
    MapDirection md5 = MapDirection.SOUTH;
    MapDirection md6 = MapDirection.SOUTH_WEST;
    MapDirection md7 = MapDirection.WEST;
    MapDirection md8 = MapDirection.NORTH_WEST;

    @Test
    void workingToStringMethod() {
        assertEquals("N", md1.toString());
        assertEquals("NE", md2.toString());
        assertEquals("E", md3.toString());
        assertEquals("SE", md4.toString());
        assertEquals("S", md5.toString());
        assertEquals("SW", md6.toString());
        assertEquals("W", md7.toString());
        assertEquals("NW", md8.toString());
    }

    @Test
    void workingToUnitVectorMethod() {
        assertEquals(new Vector2d(0,1), md1.toUnitVector());
        assertEquals(new Vector2d(1,1), md2.toUnitVector());
        assertEquals(new Vector2d(1,0), md3.toUnitVector());
        assertEquals(new Vector2d(1,-1), md4.toUnitVector());
        assertEquals(new Vector2d(0,-1), md5.toUnitVector());
        assertEquals(new Vector2d(-1,-1), md6.toUnitVector());
        assertEquals(new Vector2d(-1,0), md7.toUnitVector());
        assertEquals(new Vector2d(-1,1), md8.toUnitVector());
    }

    @Test
    void workingPositionInListOfDirectionsMethod() {
        assertEquals(0, md1.positionInListOfDirections());
        assertEquals(1, md2.positionInListOfDirections());
        assertEquals(2, md3.positionInListOfDirections());
        assertEquals(3, md4.positionInListOfDirections());
        assertEquals(4, md5.positionInListOfDirections());
        assertEquals(5, md6.positionInListOfDirections());
        assertEquals(6, md7.positionInListOfDirections());
        assertEquals(7, md8.positionInListOfDirections());
    }

    @Test
    void workingAfterTurnDirectionMethod() {
        assertEquals(MapDirection.NORTH, md1.afterTurnDirection(0));
        assertEquals(MapDirection.NORTH_EAST, md1.afterTurnDirection(1));
        assertEquals(MapDirection.EAST, md1.afterTurnDirection(2));
        assertEquals(MapDirection.SOUTH_EAST, md1.afterTurnDirection(3));
        assertEquals(MapDirection.SOUTH, md1.afterTurnDirection(4));
        assertEquals(MapDirection.SOUTH_WEST, md1.afterTurnDirection(5));
        assertEquals(MapDirection.WEST, md1.afterTurnDirection(6));
        assertEquals(MapDirection.NORTH_WEST, md1.afterTurnDirection(7));
    }
}