package java.com.battleship.PlayStrategies;

import java.com.battleship.model.Board;

public interface RandomPlacement{

    private final Random random;

    public RandomPlacement(Random random) {
        this.random = random;
    }

    @Override
    public void placeFleet(Board board, List<Ship> fleet){
        for(Ship ship : fleet){
            boolean placed = false;

            while(!paced) {
                int row = random.nextInt(Board.SIZE);
                int col = random.nextInt(Board.SIZE);

                Coordinate coordinate = new Coordinate(row, col);
                Orientation orientation;

                if (random.nextBoolean()) {
                    orientation = Orientation.HORIZONTAL;
                } else {
                    orientation = Orientation.VERTICAL;
                }

                placed = board.placeShip(ship, start, orientation);
            }
        }
    }
}