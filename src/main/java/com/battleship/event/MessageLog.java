package com.battleship.event;

import com.battleship.model.Coordinate;
import com.battleship.model.ShipType;
import com.battleship.model.ShotResult;
import com.battleship.players.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collects player-friendly messages for the front end to display later,
 * like "Bot fired at C7: hit!". Rows show as letters A-J and columns as 1-10.
 */
public class MessageLog implements GameEventListener {

    private final List<String> messages = new ArrayList<>();

    @Override
    public void onShot(Player shooter, Coordinate target, ShotResult result) {
        String outcome = switch (result) {
            case MISS -> "miss.";
            case HIT, SUNK -> "hit!";
            case ALREADY_FIRED -> "already fired there.";
        };
        messages.add(shooter.getName() + " fired at " + label(target) + ": " + outcome);
    }

    @Override
    public void onShipSunk(Player owner, ShipType type) {
        messages.add(owner.getName() + "'s " + label(type) + " was sunk!");
    }

    @Override
    public void onGameOver(Player winner) {
        messages.add("Game over: " + winner.getName() + " wins!");
    }

    /** Every message so far, oldest first. Read-only. */
    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    /** (0, 0) becomes "A1", (2, 6) becomes "C7". */
    private static String label(Coordinate c) {
        return (char) ('A' + c.row()) + String.valueOf(c.col() + 1);
    }

    /** CARRIER becomes "Carrier". */
    private static String label(ShipType type) {
        String name = type.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}