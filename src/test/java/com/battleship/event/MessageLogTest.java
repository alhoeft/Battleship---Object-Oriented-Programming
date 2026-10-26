package com.battleship.event;

import com.battleship.model.Coordinate;
import com.battleship.model.ShipType;
import com.battleship.model.ShotResult;
import com.battleship.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageLogTest {

    private MessageLog log;
    private Player bot;
    private Player human;

    @BeforeEach
    void setUp() {
        log = new MessageLog();
        bot = playerNamed("Bot");
        human = playerNamed("Player");
    }

    /** A bare-bones Player for testing, since Player is abstract. */
    private static Player playerNamed(String name) {
        return new Player(name, null) {
            @Override
            public Coordinate chooseTarget() {
                return null;
            }
        };
    }

    @Test
    void onShot_hit_usesLetterNumberCoordinates() {
        log.onShot(bot, new Coordinate(2, 6), ShotResult.HIT);

        assertEquals(List.of("Bot fired at C7: hit!"), log.getMessages());
    }

    @Test
    void onShot_miss_reportsMissAndCornerCells() {
        log.onShot(human, new Coordinate(0, 0), ShotResult.MISS);
        log.onShot(human, new Coordinate(9, 9), ShotResult.MISS);

        assertEquals(List.of("Player fired at A1: miss.", "Player fired at J10: miss."),
                log.getMessages());
    }

    @Test
    void onShot_sunkShot_isReportedAsHit() {
        log.onShot(human, new Coordinate(4, 4), ShotResult.SUNK);

        assertEquals(List.of("Player fired at E5: hit!"), log.getMessages());
    }

    @Test
    void onShipSunk_usesReadableShipName() {
        log.onShipSunk(bot, ShipType.DESTROYER);

        assertEquals(List.of("Bot's Destroyer was sunk!"), log.getMessages());
    }

    @Test
    void onGameOver_announcesWinner() {
        log.onGameOver(human);

        assertEquals(List.of("Game over: Player wins!"), log.getMessages());
    }

    @Test
    void getMessages_keepsOrderAndIsReadOnly() {
        log.onShot(human, new Coordinate(1, 1), ShotResult.SUNK);
        log.onShipSunk(bot, ShipType.CARRIER);
        log.onGameOver(human);

        List<String> messages = log.getMessages();
        assertEquals(List.of(
                "Player fired at B2: hit!",
                "Bot's Carrier was sunk!",
                "Game over: Player wins!"), messages);
        assertThrows(UnsupportedOperationException.class, () -> messages.add("cheating"));
    }
}