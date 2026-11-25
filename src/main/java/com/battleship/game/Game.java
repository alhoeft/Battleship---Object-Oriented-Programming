package com.battleship.game;

import com.battleship.event.GameEventListener;
import com.battleship.model.Coordinate;
import com.battleship.model.GameState;
import com.battleship.model.Orientation;
import com.battleship.model.Ship;
import com.battleship.model.ShipType;
import com.battleship.model.ShotResult;
import com.battleship.players.BotPlayer;
import com.battleship.players.HumanPlayer;
import com.battleship.players.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs one game of Battleship: ship placement, then alternating shots
 * (human, then bot) until one fleet is sunk.
 *
 * STUB: the public methods match the shared contract, but placement
 * and firing are not implemented yet.
 */
public class Game {

    private final HumanPlayer human;
    private final BotPlayer bot;
    private final List<Ship> humanShipsToPlace;
    private final List<GameEventListener> listeners = new ArrayList<>();

    private GameState state = GameState.PLACING;
    private Player winner;

    public Game(HumanPlayer human, BotPlayer bot, List<Ship> humanFleet) {
        this.human = human;
        this.bot = bot;
        this.humanShipsToPlace = new ArrayList<>(humanFleet);
    }

    public void addListener(GameEventListener listener) {
        listeners.add(listener);
    }

    /**
     * PLACING phase only. Returns false if the ship is off the board or
     * overlaps another ship. After the last ship is placed, the state
     * becomes IN_PROGRESS.
     */
    public boolean placeHumanShip(ShipType type, Coordinate start, Orientation orientation) {
        throw new UnsupportedOperationException("Game.placeHumanShip is not implemented yet");
    }

    /** Ship types the human still has to place, largest first. */
    public List<ShipType> shipsLeftToPlace() {
        List<ShipType> types = new ArrayList<>();
        for (Ship ship : humanShipsToPlace) {
            types.add(ship.getType());
        }
        return types;
    }

    /**
     * IN_PROGRESS phase only. Runs the human's shot, then the bot's reply
     * (unless the human just won). Returns the result of the human's shot.
     * Throws IllegalStateException if called in the wrong phase.
     */
    public ShotResult fire(Coordinate target) {
        throw new UnsupportedOperationException("Game.fire is not implemented yet");
    }

    public GameState getState() {
        return state;
    }

    /** The winning player, or null until the game is over. */
    public Player getWinner() {
        return winner;
    }

    public HumanPlayer getHuman() {
        return human;
    }

    public BotPlayer getBot() {
        return bot;
    }
}