package com.battleship.model;

import java.util.ArrayList;
import java.util.List;

public class FleetFactory {

    public List<Ship> createStandardFleet() {
        List<Ship> fleet = new ArrayList<>();
        for (ShipType type : ShipType.values()) {
            fleet.add(new Ship(type));
        }
        return fleet;
    }
}
