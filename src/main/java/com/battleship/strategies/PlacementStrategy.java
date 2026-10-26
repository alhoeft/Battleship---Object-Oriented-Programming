package com.battleship.strategies;

import com.battleship.model.Board;
import com.battleship.model.Coordinate;
import com.battleship.model.Orientation;
import com.battleship.model.Ship;

import java.util.List;

public interface PlacementStrategy{
    void placeFleet(Board board, List<Ship> fleet);
}