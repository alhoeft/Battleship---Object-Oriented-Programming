package com.battleship.strategies;

public interface TargetingStrategy{
    Coordinate chooseTarget();

    public void recordShot(Coordinate coordinate, ShotResult result) {
        if (result != ShotResult.ALREADY_FIRED) {
            shotHistory.put(coordinate, result);
        }
    }
}