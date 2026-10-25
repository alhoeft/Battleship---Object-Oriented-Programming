package java.com.battleship.players;

import java.util.List;
import java.util.Random;

public class FleetFactory {

    public List<Ship> createStandardFleet() {
        List<Ship> fleet = new ArrayList<>();
        for (ShipType type : ShipType.values()) {
            fleet.add(new Ship(type));
        }
        return fleet;
    }

}