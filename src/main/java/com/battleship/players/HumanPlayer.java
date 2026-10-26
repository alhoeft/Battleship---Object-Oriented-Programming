package com.battleship.players;

import com.battleship.model.Coordinate;
import com.battleship.model.Orientation;
import com.battleship.model.Ship;
import com.battleship.strategies.PlacementStrategy;
import com.battleship.strategies.TargetingStrategy;

public class HumanPlayer extends Player {

    private final TargetingStrategy targetingStrategy;
    private PlacementStrategy placementStrategy;

    public HumanPlayer(String name, TargetingStrategy targetingStrategy, PlacementStrategy placementStrategy) {
        super(name, placementStrategy);
        this.targetingStrategy = targetingStrategy;
    }

    public boolean placeShip(Ship ship, Coordinate coordinate, Orientation orientation) {
        return getBoard().placeShip(ship, coordinate, orientation);
    }

    @Override
    public Coordinate chooseTarget() {
        return targetingStrategy.chooseTarget();
    }
}
