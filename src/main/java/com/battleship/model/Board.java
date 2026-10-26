package com.battleship.model;

import com.battleship.model.Coordinate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    public static final int SIZE = 10;

    private final Cell[][] grid;
    private final List<Ship> ships;

    public Board(){
        grid = new Cell[SIZE][SIZE];
        for(int row = 0; row < SIZE; row++){
            for(int col = 0; col < SIZE; col++){
                grid[row][col] = new Cell();
            }
        }
        ships = new ArrayList<>();
    }

    public boolean placeShip(Ship ship, Coordinate start, Orientation orientation){
        List<Coordinate> coveredSpaces = new ArrayList<>();

        for (int i = 0; i < ship.getSize(); i++) {
            int row = start.row() + (orientation == Orientation.VERTICAL ? i : 0);
            int col = start.col() + (orientation == Orientation.HORIZONTAL ? i : 0);
            Coordinate cell = new Coordinate(row, col);

            if (!cell.isInBounds(SIZE) || getCell(cell).hasShip()) {
                return false;
            }

            coveredSpaces.add(cell);
        }

        for (Coordinate cell : coveredSpaces) {
            getCell(cell).setShip(ship);
        }
        ships.add(ship);
        return true;
    }

    public ShotResult receiveShot(Coordinate target) {
        if (!target.isInBounds(SIZE)) {
            throw new IllegalArgumentException("Target out of bounds: " + target);
        }

        Cell cell = getCell(target);

        if (cell.isHit()) {
            return ShotResult.ALREADY_FIRED;
        }

        cell.markHit();

        if (!cell.hasShip()) {
            return ShotResult.MISS;
        }

        Ship ship = cell.getShip();
        ship.hit();

        return ship.isSunk() ? ShotResult.SUNK : ShotResult.HIT;
    }

    public boolean allShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public CellView[][] getOpponentView() {
        CellView[][] view = new CellView[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Cell cell = grid[row][col];
                if (!cell.isHit()) {
                    view[row][col] = CellView.UNKNOWN;
                } else if (cell.hasShip()) {
                    view[row][col] = CellView.HIT;
                } else {
                    view[row][col] = CellView.MISS;
                }
            }
        }
        return view;
    }

    public Cell getCell(Coordinate coordinate) {
        return grid[coordinate.row()][coordinate.col()];
    }

    public List<Ship> getShips() {
        return Collections.unmodifiableList(ships);
    }
}

