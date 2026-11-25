package com.battleship.dto;

import com.battleship.event.MessageLog;
import com.battleship.game.Game;
import com.battleship.model.Coordinate;
import com.battleship.model.FleetFactory;
import com.battleship.model.GameState;
import com.battleship.model.Orientation;
import com.battleship.model.Ship;
import com.battleship.model.ShipType;
import com.battleship.players.BotPlayer;
import com.battleship.players.HumanPlayer;
import com.battleship.strategies.RandomPlacementStrategy;
import com.battleship.strategies.RandomTargetingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameStateResponseTest {

    private HumanPlayer human;
    private BotPlayer bot;
    private Game game;

    @BeforeEach
    void setUp() {
        Random random = new Random(42);
        // The human's strategies aren't used here, so null is fine.
        human = new HumanPlayer("Player", null, null);
        bot = new BotPlayer("Bot", new RandomTargetingStrategy(random), new RandomPlacementStrategy(random));
        bot.placeFleet(new FleetFactory().createStandardFleet());
        game = new Game(human, bot, new FleetFactory().createStandardFleet());
    }

    @Test
    void newGameIsPlacingWithAllFiveShipsLeft() {
        GameStateResponse state = GameStateResponse.from(game, new MessageLog());

        assertEquals(GameState.PLACING, state.phase());
        assertEquals(5, state.shipsToPlace().size());
        assertEquals(ShipType.CARRIER, state.shipsToPlace().get(0));
        assertNull(state.winner());
        assertNull(state.error());
        assertEquals(0, state.messages().size());
    }

    @Test
    void playerBoardShowsOwnShipsHitsAndMisses() {
        human.placeShip(new Ship(ShipType.DESTROYER), new Coordinate(0, 0), Orientation.HORIZONTAL);
        human.getBoard().receiveShot(new Coordinate(0, 0)); // hit
        human.getBoard().receiveShot(new Coordinate(5, 5)); // miss

        String[][] board = GameStateResponse.from(game, new MessageLog()).playerBoard();

        assertEquals("HIT", board[0][0]);
        assertEquals("SHIP", board[0][1]);
        assertEquals("MISS", board[5][5]);
        assertEquals("EMPTY", board[9][9]);
    }

    @Test
    void enemyBoardNeverRevealsBotShips() {
        String[][] board = GameStateResponse.from(game, new MessageLog()).enemyBoard();

        assertEquals(10, board.length);
        for (String[] row : board) {
            assertEquals(10, row.length);
            for (String cell : row) {
                assertEquals("UNKNOWN", cell);
                assertNotEquals("SHIP", cell);
            }
        }
    }

    @Test
    void withErrorKeepsStateAndAddsMessage() {
        GameStateResponse state = GameStateResponse.from(game, new MessageLog())
                .withError("Ship doesn't fit there.");

        assertEquals("Ship doesn't fit there.", state.error());
        assertEquals(GameState.PLACING, state.phase());
    }
}
