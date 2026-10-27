package com.battleship.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void isInBounds_trueForCorners() {
        assertTrue(new Coordinate(0, 0).isInBounds(10));
        assertTrue(new Coordinate(9, 9).isInBounds(10));
        assertTrue(new Coordinate(0, 9).isInBounds(10));
        assertTrue(new Coordinate(9, 0).isInBounds(10));
    }

    @Test
    void isInBounds_falseJustOutside() {
        assertFalse(new Coordinate(-1, 0).isInBounds(10));
        assertFalse(new Coordinate(0, -1).isInBounds(10));
        assertFalse(new Coordinate(10, 0).isInBounds(10));
        assertFalse(new Coordinate(0, 10).isInBounds(10));
    }

    @Test
    void equalCoordinates_areEqualAndHashTheSame() {
        Set<Coordinate> set = new HashSet<>();
        set.add(new Coordinate(3, 4));

        assertEquals(new Coordinate(3, 4), new Coordinate(3, 4));
        assertTrue(set.contains(new Coordinate(3, 4)));
    }
}