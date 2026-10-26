package com.battleship.players;

import com.battleship.model.Coordinate;
import com.battleship.strategies.PlacementStrategy;
import com.battleship.strategies.TargetingStrategy;

import java.util.Random;

public class BotPlayer extends Player {
    private TargetingStrategy targetingStrategy;
    private PlacementStrategy placementStrategy;

    public BotPlayer(String name, TargetingStrategy targetingStrategy, PlacementStrategy placementStrategy) {
        super(name, placementStrategy);
        this.targetingStrategy = targetingStrategy;
    }

    @Override
    public Coordinate chooseTarget() {
        return targetingStrategy.chooseTarget();
    }
}
