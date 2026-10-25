package java.com.battleship.PlayStrategies;

import java.com.battleship.model.Board;

public interface PlacementStrategy implements TargetingStrategy{

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