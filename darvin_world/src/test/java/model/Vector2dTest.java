package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {
    private static final int[] i = {1,2};

    @Test
    void initWorks() {
//        given
        int x = 1;
        int y = 2;
//        when
        Vector2d v = new Vector2d(x, y);
//        then
        assertEquals(x, v.getX());
        assertEquals(y, v.getY());
    }

    @Test
    void precedesBothDifferentWorks() {
//        when
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(3, 4);
//        then
        assertTrue(v_1.precedes(v_2));
        assertFalse(v_2.precedes(v_1));
    }

    @Test
    void notPrecedesBothDifferentWorks() {
//        when
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(3, -1);
//        then
        assertFalse(v_1.precedes(v_2));
        assertFalse(v_2.precedes(v_1));
    }


    @Test
    void precedesBothEqualWorks() {
//        when
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(1, 2);
//        then
        assertTrue(v_1.precedes(v_2));
        assertTrue(v_2.precedes(v_1));
    }

    @Test
    void precedesYDifferentWorks() {
//        when
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(1, 4);
//        then
        assertTrue(v_1.precedes(v_2));
        assertFalse(v_2.precedes(v_1));
    }

    @Test
    void precedesXDifferentWorks() {
//        when
        Vector2d v_1 = new Vector2d(2, 1);
        Vector2d v_2 = new Vector2d(4, 1);
//    then
        assertTrue(v_1.precedes(v_2));
        assertFalse(v_2.precedes(v_1));
    }

    @Test
    void followBothDifferentWorks() {
//        when
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(3, 4);
//        then
        assertFalse(v_1.follows(v_2));
        assertTrue(v_2.follows(v_1));
    }

    @Test
    void notFollowBothDifferentWorks() {
//        when
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(3, -1);
//        then
        assertFalse(v_1.follows(v_2));
        assertFalse(v_2.follows(v_1));
    }


    @Test
    void followBothEqualWorks() {
//        when
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(1, 2);
//        then
        assertTrue(v_1.follows(v_2));
        assertTrue(v_2.follows(v_1));
    }

    @Test
    void followsYDifferentWorks() {
//        when
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(1, 4);
//        then
        assertFalse(v_1.follows(v_2));
        assertTrue(v_2.follows(v_1));
    }

    @Test
    void followsXDifferentWorks() {
//        when
        Vector2d v_1 = new Vector2d(2, 1);
        Vector2d v_2 = new Vector2d(4, 1);
//    then
        assertFalse(v_1.follows(v_2));
        assertTrue(v_2.follows(v_1));
    }

    @Test
    void addWorks() {
//        given
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(3, 4);
        Vector2d res = new Vector2d(4, 6);
//        when
        Vector2d v_3 = v_1.add(v_2);
//        then
        assertEquals(res, v_3);
    }

    @Test
    void subtractWorks() {
//        given
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(3, 4);
        Vector2d expect_1_2 = new Vector2d(-2, -2);
        Vector2d expect_2_1 = new Vector2d(2, 2);
//        when
        Vector2d res_1_2 = v_1.subtract(v_2);
        Vector2d res_2_1 = v_2.subtract(v_1);
//        then
        assertEquals(expect_1_2, res_1_2);
        assertEquals(expect_2_1, res_2_1);
    }

    @Test
    void upperRightDiffWorks() {
//        given
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(3, 4);
        Vector2d expect = new Vector2d(3, 4);
//        when
        Vector2d res = v_1.upperRight(v_2);
//        then
        assertEquals(expect, res);
    }

    @Test
    void upperRightEqualWorks() {
//        given
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(1,2);
//        when
        Vector2d res = v_1.upperRight(v_2);
//        then
        assertEquals(res, v_1);
    }

    @Test
    void lowerLeftDiffWorks() {
//        given
        Vector2d v_1 = new Vector2d(5, 2);
        Vector2d v_2 = new Vector2d(3, 4);
        Vector2d expect = new Vector2d(3, 2);
//        when
        Vector2d res = v_1.lowerLeft(v_2);
//        then
        assertEquals(expect, res);
    }

    @Test
    void lowerLeftEqualWorks() {
//        given
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(1,2);
//        when
        Vector2d res = v_1.lowerLeft(v_2);
//        then
        assertEquals(res, v_1);
    }

    @Test
    void oppositeWorks() {
//        given
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d exp = new Vector2d(-1, -2);
//        when
        Vector2d res = v_1.opposite();
//        then
        assertEquals(exp, res);
    }


    @Test
    void oppositeZeroWorks() {
//        given
        Vector2d v_1 = new Vector2d(0,0);
//        when
        Vector2d res = v_1.opposite();
//        then
        assertEquals(res, v_1);
    }

    @Test
    void equalsDifferentTypesWorks() {
//        when
        Vector2d v = new Vector2d(1, 2);
//        then
        assertFalse(v.equals(i));
    }

    @Test
    void equalsDifferentWorks() {
//        when
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(2, 2);
//        then
        assertFalse(v_1.equals(v_2));
        assertFalse(v_2.equals(v_1));
    }

    @Test
    void equalsEqualWorks() {
//        when
        Vector2d v_1 = new Vector2d(1, 2);
        Vector2d v_2 = new Vector2d(1, 2);
//        then
        assertTrue(v_1.equals(v_2));
        assertTrue(v_2.equals(v_1));
    }


    @Test
    void equalsSameWorks() {
//        when
        Vector2d v_1 = new Vector2d(1, 2);
//        then
        assertTrue(v_1.equals(v_1));
    }

    @Test
    void toStringWorks() {
//        given
        Vector2d v = new Vector2d(1, 2);
        String expected = "(1,2)";
//        when
        String res = v.toString();
//        then
        assertEquals(expected, res);
    }
}