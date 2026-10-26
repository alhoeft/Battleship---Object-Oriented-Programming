package com.battleship.strategies;

import java.util.List;
import java.util.Random;

import com.battleship.model.Board;
import com.battleship.model.Coordinate;
import com.battleship.model.Orientation;
import com.battleship.model.Ship;

public class RandomPlacementStrategy implements PlacementStrategy {

    private final Random random;

    public RandomPlacementStrategy(Random random) {
        this.random = random;
    }

    @Override
    public void placeFleet(Board board, List<Ship> fleet){
        for(Ship ship : fleet){
            boolean placed = false;

            while(!placed) {
                int row = random.nextInt(Board.SIZE);
                int col = random.nextInt(Board.SIZE);

                Coordinate coordinates = new Coordinate(row, col);
                Orientation orientation;

                if (random.nextBoolean()) {
                    orientation = Orientation.HORIZONTAL;
                } else {
                    orientation = Orientation.VERTICAL;
                }

                placed = board.placeShip(ship, coordinates, orientation);
            }
        }
    }
}