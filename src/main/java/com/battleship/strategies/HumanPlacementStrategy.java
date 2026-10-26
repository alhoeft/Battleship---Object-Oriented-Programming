package com.battleship.strategies;

import com.battleship.model.Board;
import com.battleship.model.Coordinate;
import com.battleship.model.Orientation;
import com.battleship.model.Ship;

import java.util.List;
import java.util.Scanner;

public class HumanPlacementStrategy implements PlacementStrategy {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void placeFleet(Board board, List<Ship> fleet) {
        for (Ship ship : fleet) {
            boolean placed = false;

            while (!placed) {
                System.out.println("Place ship of size " + ship.getSize());

                System.out.print("Enter row (0-9): ");
                int row = scanner.nextInt();

                System.out.print("Enter column (0-9): ");
                int col = scanner.nextInt();

                System.out.print("Orientation (H/V): ");
                String direction = scanner.next();

                Orientation orientation =
                        direction.equalsIgnoreCase("V")
                                ? Orientation.VERTICAL
                                : Orientation.HORIZONTAL;

                Coordinate start = new Coordinate(row, col);

                placed = board.placeShip(ship, start, orientation);

                if (!placed) {
                    System.out.println("Invalid placement. Try again.");
                }
            }
        }
    }
}