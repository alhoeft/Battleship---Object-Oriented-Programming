package com.battleship.service;

import com.battleship.dto.GameStateResponse;
import com.battleship.model.Board;
import com.battleship.model.FleetFactory;
import com.battleship.model.GameState;
import com.battleship.model.Orientation;
import com.battleship.model.ShipType;
import com.battleship.players.PlayerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Holds the one active game between requests and turns each move into the
 * JSON state the front end draws.
 *
 * STUB: this version does NOT use the real Game yet. It fakes a game with
 * plain arrays so the front end can be built against realistic JSON right
 * away. The method signatures are final; only the insides will change.
 */
@Service
public class GameService {

    private final PlayerFactory playerFactory;
    private final FleetFactory fleetFactory;

    // ---- fake game state (replaced by the real Game later) ----
    private GameState phase;
    private List<ShipType> shipsToPlace;
    private String[][] playerBoard;
    private String[][] enemyBoard;
    private List<String> messages;
    private String winner;
    private int shotCount;

    public GameService(PlayerFactory playerFactory, FleetFactory fleetFactory) {
        this.playerFactory = playerFactory;
        this.fleetFactory = fleetFactory;
        newGame();
    }

    public GameStateResponse newGame() {
        phase = GameState.PLACING;
        shipsToPlace = new ArrayList<>(Arrays.asList(ShipType.values()));
        playerBoard = filledBoard("EMPTY");
        enemyBoard = filledBoard("UNKNOWN");
        messages = new ArrayList<>();
        winner = null;
        shotCount = 0;
        return getState();
    }

    public GameStateResponse placeShip(ShipType type, int row, int col, Orientation orientation) {
        if (phase != GameState.PLACING) {
            return getState().withError("All ships are already placed.");
        }
        if (type != shipsToPlace.get(0)) {
            return getState().withError("Place your " + shipsToPlace.get(0) + " next.");
        }
        for (int i = 0; i < type.getSize(); i++) {
            int r = row + (orientation == Orientation.VERTICAL ? i : 0);
            int c = col + (orientation == Orientation.HORIZONTAL ? i : 0);
            if (r < 0 || r >= Board.SIZE || c < 0 || c >= Board.SIZE || !playerBoard[r][c].equals("EMPTY")) {
                return getState().withError("Ship doesn't fit there.");
            }
        }
        for (int i = 0; i < type.getSize(); i++) {
            int r = row + (orientation == Orientation.VERTICAL ? i : 0);
            int c = col + (orientation == Orientation.HORIZONTAL ? i : 0);
            playerBoard[r][c] = "SHIP";
        }
        shipsToPlace.remove(0);
        if (shipsToPlace.isEmpty()) {
            phase = GameState.IN_PROGRESS;
        }
        return getState();
    }

    public GameStateResponse fire(int row, int col) {
        if (phase != GameState.IN_PROGRESS) {
            return getState().withError("You can't fire right now.");
        }
        if (row < 0 || row >= Board.SIZE || col < 0 || col >= Board.SIZE) {
            return getState().withError("That's off the board.");
        }
        if (!enemyBoard[row][col].equals("UNKNOWN")) {
            return getState().withError("You already fired there.");
        }
        shotCount++;
        boolean hit = shotCount % 2 == 1;
        enemyBoard[row][col] = hit ? "HIT" : "MISS";
        messages.add("Player fired at " + label(row, col) + ": " + (hit ? "hit!" : "miss."));

        // Fake bot reply: fire at the next cell in reading order.
        int botRow = (shotCount - 1) / Board.SIZE;
        int botCol = (shotCount - 1) % Board.SIZE;
        boolean botHit = playerBoard[botRow][botCol].equals("SHIP");
        playerBoard[botRow][botCol] = botHit ? "HIT" : "MISS";
        messages.add("Bot fired at " + label(botRow, botCol) + ": " + (botHit ? "hit!" : "miss."));

        // Fake ending so the game-over screen can be tested: 10 shots wins.
        if (shotCount >= 10) {
            phase = GameState.GAME_OVER;
            winner = "Player";
            messages.add("Game over: Player wins!");
        }
        return getState();
    }

    public GameStateResponse getState() {
        return new GameStateResponse(phase, List.copyOf(shipsToPlace), copy(playerBoard),
                copy(enemyBoard), List.copyOf(messages), winner, null);
    }

    private static String[][] filledBoard(String value) {
        String[][] board = new String[Board.SIZE][Board.SIZE];
        for (String[] row : board) {
            Arrays.fill(row, value);
        }
        return board;
    }

    private static String[][] copy(String[][] board) {
        String[][] result = new String[board.length][];
        for (int i = 0; i < board.length; i++) {
            result[i] = board[i].clone();
        }
        return result;
    }

    private static String label(int row, int col) {
        return (char) ('A' + row) + String.valueOf(col + 1);
    }
}