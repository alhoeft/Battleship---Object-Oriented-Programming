package com.battleship.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void newCell_isEmptyAndNotHit() {
        Cell cell = new Cell();

        assertFalse(cell.hasShip());
        assertNull(cell.getShip());
        assertFalse(cell.isHit());
    }

    @Test
    void setShip_storesShip() {
        Cell cell = new Cell();
        Ship ship = new Ship(ShipType.DESTROYER);

        cell.setShip(ship);

        assertTrue(cell.hasShip());
        assertSame(ship, cell.getShip());
    }

    @Test
    void markHit_setsHit() {
        Cell cell = new Cell();
        cell.markHit();
        assertTrue(cell.isHit());
    }
}