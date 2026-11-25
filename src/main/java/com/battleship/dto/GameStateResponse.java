package com.battleship.dto;

import com.battleship.event.MessageLog;
import com.battleship.game.Game;
import com.battleship.model.Board;
import com.battleship.model.Cell;
import com.battleship.model.Coordinate;
import com.battleship.model.GameState;
import com.battleship.model.ShipType;
import com.battleship.players.Player;

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

    /**
     * Builds the JSON state from a real Game. The human's own board shows
     * their ships; the bot's board uses getOpponentView(), so the bot's
     * ships stay hidden until they are hit.
     */
    public static GameStateResponse from(Game game, MessageLog log) {
        Player winner = game.getWinner();
        return new GameStateResponse(
                game.getState(),
                List.copyOf(game.shipsLeftToPlace()),
                ownView(game.getHuman().getBoard()),
                toStrings(game.getBot().getBoard().getOpponentView()),
                List.copyOf(log.getMessages()),
                winner == null ? null : winner.getName(),
                null);
    }

    /** Same state, with an error message attached. */
    public GameStateResponse withError(String message) {
        return new GameStateResponse(phase, shipsToPlace, playerBoard, enemyBoard,
                messages, winner, message);
    }

    /**
     * The human's own board, ships included.
     *
     * TODO: once Board.getOwnView() and OwnCellView are merged (Track 1),
     * replace the body with: return toStrings(board.getOwnView());
     * The output is the same, so the front end won't need to change.
     */
    private static String[][] ownView(Board board) {
        String[][] view = new String[Board.SIZE][Board.SIZE];
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Cell cell = board.getCell(new Coordinate(row, col));
                if (cell.isHit()) {
                    view[row][col] = cell.hasShip() ? "HIT" : "MISS";
                } else {
                    view[row][col] = cell.hasShip() ? "SHIP" : "EMPTY";
                }
            }
        }
        return view;
    }

    // Turns a grid of enum values (CellView, OwnCellView) into their names.
    private static String[][] toStrings(Enum<?>[][] grid) {
        String[][] result = new String[grid.length][];
        for (int row = 0; row < grid.length; row++) {
            result[row] = new String[grid[row].length];
            for (int col = 0; col < grid[row].length; col++) {
                result[row][col] = grid[row][col].name();
            }
        }
        return result;
    }
}
