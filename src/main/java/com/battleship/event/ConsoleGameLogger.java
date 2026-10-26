package com.battleship.event;

import com.battleship.model.Coordinate;
import com.battleship.model.ShipType;
import com.battleship.model.ShotResult;
import com.battleship.players.Player;

/** Prints every game event to the console. Useful for debugging. */
public class ConsoleGameLogger implements GameEventListener {

    @Override
    public void onShot(Player shooter, Coordinate target, ShotResult result) {
        System.out.println(shooter.getName() + " fired at (" + target.row() + ", "
                + target.col() + "): " + result);
    }

    @Override
    public void onShipSunk(Player owner, ShipType type) {
        System.out.println(owner.getName() + "'s " + type + " was sunk");
    }

    @Override
    public void onGameOver(Player winner) {
        System.out.println("Game over: " + winner.getName() + " wins");
    }
}