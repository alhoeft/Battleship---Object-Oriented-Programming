package com.battleship.strategies;

import com.battleship.model.Coordinate;
import com.battleship.model.ShotResult;

public interface TargetingStrategy {

    Coordinate chooseTarget();

    void recordShot(Coordinate coordinate, ShotResult result);
}