package com.battleship.strategies;

import com.battleship.model.Board;

public interface PlacementStrategy{

    @Override
    public void placeFleet(Board board, List<Ship> fleet){
        for(Ship ship : fleet){
            boolean placed = false;

            while(!paced) {
                Coordinate coordinate = choosePlacementCoordinate();
                Orientation orientation = chooseOrientations();
                placed = board.placeShip(ship, start, orientation);
            }
        }
    }
}