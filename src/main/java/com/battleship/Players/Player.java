package java.com.battleship.players;

import java.com.battleship.model.Board;
import java.com.battleship.model.Coordinate;
import java.com.battleship.model.ShotResult;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public abstract class Player {
    private String name;
    private Board board;
    private Coordinate coordinate;
    private ShotResult shotResult;
    private Map<Coordinate, ShotResult> shotHistory = new HashMap<>();

    public Player(String name) {
        this.name = name;
        this.board = new Board();
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public abstract Coordinate chooseTarget();

    public void recordShot(Coordinate coordinate, ShotResult result) {
        if (result != ShotResult.ALREADY_FIRED) {
            shotHistory.put(coordinate, result);
        }
    }

    public boolean hasLost(){
        return board.allShipsSunk();
    }

    public boolean hasAlreadyShotAtChosenCoordinate(Coordinate coordinate) {
        return shotHistory.containsKey(coordinate);
    }
}