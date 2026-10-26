package com.battleship.players;

public class HumanPlayer extends Player {

    private final TargetingStrategy targetingStrategy;

    public HumanPlayer(String name, TargetingStrategy targetingStrategy) {
        super(name);
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
