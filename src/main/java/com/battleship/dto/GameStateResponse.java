package com.battleship.dto;

import com.battleship.model.GameState;
import com.battleship.model.ShipType;

import java.util.List;

/**
 * The JSON every endpoint returns. The front end redraws everything from this.
 *
 * playerBoard cells: "EMPTY", "SHIP", "HIT", "MISS"
 * enemyBoard cells:  "UNKNOWN", "HIT", "MISS"
 * winner: the winner's name, or null until the game is over
 * error:  a short message when a move was rejected, otherwise null
 */
public record GameStateResponse(
        GameState phase,
        List<ShipType> shipsToPlace,
        String[][] playerBoard,
        String[][] enemyBoard,
        List<String> messages,
        String winner,
        String error) {

    /** Same state, with an error message attached. */
    public GameStateResponse withError(String message) {
        return new GameStateResponse(phase, shipsToPlace, playerBoard, enemyBoard,
                messages, winner, message);
    }
}