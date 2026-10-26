package com.battleship.event;

import com.battleship.model.Coordinate;
import com.battleship.model.ShipType;
import com.battleship.model.ShotResult;
import com.battleship.players.Player;

/**
 * Observer pattern: classes that want to react to game events implement this
 * interface. Game keeps a list of listeners and notifies all of them, without
 * knowing what each one does with the event.
 */
public interface GameEventListener {
    /** Called after every shot, by either player. */
    void onShot(Player shooter, Coordinate target, ShotResult result);

    /** Called when a shot sinks a ship. owner is the player who lost the ship. */
    void onShipSunk(Player owner, ShipType type);

    /** Called once, when one player's whole fleet has been sunk. */
    void onGameOver(Player winner);
}
