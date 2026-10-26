package com.battleship.players;

import com.battleship.model.Board;
import com.battleship.model.Coordinate;
import com.battleship.model.Ship;
import com.battleship.model.ShotResult;
import com.battleship.strategies.PlacementStrategy;

import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public abstract class Player {
    private String name;
    private Board board;
    private Coordinate coordinate;
    private ShotResult shotResult;
    private Map<Coordinate, ShotResult> shotHistory = new HashMap<>();
    private PlacementStrategy placementStrategy;

    public Player(String name, PlacementStrategy placementStrategy) {
        this.name = name;
        this.board = new Board();
        this.placementStrategy = placementStrategy;
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

    public void placeFleet(List<Ship> fleet) {
        placementStrategy.placeFleet(board, fleet);
    }
}