package com.battleship.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTypeTest {

    @Test
    void sizes_matchStandardRules() {
        assertEquals(5, ShipType.CARRIER.getSize());
        assertEquals(4, ShipType.BATTLESHIP.getSize());
        assertEquals(3, ShipType.CRUISER.getSize());
        assertEquals(3, ShipType.SUBMARINE.getSize());
        assertEquals(2, ShipType.DESTROYER.getSize());
    }

    @Test
    void totalSize_is17() {
        int total = 0;
        for (ShipType type : ShipType.values()) {
            total += type.getSize();
        }
        assertEquals(17, total);
    }
}