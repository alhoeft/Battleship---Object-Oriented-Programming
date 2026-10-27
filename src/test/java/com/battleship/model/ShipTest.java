package com.battleship.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    @Test
    void newShip_hasNoHitsAndIsNotSunk() {
        Ship ship = new Ship(ShipType.CRUISER);

        assertEquals(0, ship.getHits());
        assertFalse(ship.isSunk());
    }

    @Test
    void getSize_matchesShipType() {
        assertEquals(5, new Ship(ShipType.CARRIER).getSize());
        assertEquals(2, new Ship(ShipType.DESTROYER).getSize());
    }

    @Test
    void getType_returnsConstructorType() {
        assertEquals(ShipType.SUBMARINE, new Ship(ShipType.SUBMARINE).getType());
    }

    @Test
    void hit_incrementsHits() {
        Ship ship = new Ship(ShipType.CRUISER);
        ship.hit();
        assertEquals(1, ship.getHits());
    }

    @Test
    void isSunk_falseUntilHitsEqualSize() {
        Ship ship = new Ship(ShipType.CRUISER); // size 3

        ship.hit();
        ship.hit();
        assertFalse(ship.isSunk());

        ship.hit();
        assertTrue(ship.isSunk());
    }
}