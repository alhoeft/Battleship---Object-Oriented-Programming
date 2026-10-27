package com.battleship.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FleetFactoryTest {

    private final FleetFactory factory = new FleetFactory();

    // Required test 10
    @Test
    void createStandardFleet_hasFiveCorrectShips() {
        List<Ship> fleet = factory.createStandardFleet();

        assertEquals(5, fleet.size());
        int[] expectedSizes = {5, 4, 3, 3, 2};
        for (int i = 0; i < expectedSizes.length; i++) {
            assertEquals(expectedSizes[i], fleet.get(i).getSize());
        }
    }

    @Test
    void createStandardFleet_hasOneOfEachType() {
        List<Ship> fleet = factory.createStandardFleet();

        for (ShipType type : ShipType.values()) {
            long count = fleet.stream().filter(ship -> ship.getType() == type).count();
            assertEquals(1, count, "Expected exactly one " + type);
        }
    }

    @Test
    void createStandardFleet_shipsStartUndamaged() {
        for (Ship ship : factory.createStandardFleet()) {
            assertEquals(0, ship.getHits());
            assertFalse(ship.isSunk());
        }
    }

    @Test
    void createStandardFleet_returnsNewShipsEachCall() {
        List<Ship> firstFleet = factory.createStandardFleet();
        List<Ship> secondFleet = factory.createStandardFleet();

        for (int i = 0; i < firstFleet.size(); i++) {
            assertNotSame(firstFleet.get(i), secondFleet.get(i));
        }
    }
}