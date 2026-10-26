package com.battleship.strategies;

import com.battleship.model.Board;
import com.battleship.model.Coordinate;
import com.battleship.model.ShotResult;
import java.util.Scanner;

public class HumanTargetingStrategy implements TargetingStrategy {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public Coordinate chooseTarget() {
        Coordinate target;

        do {
            System.out.print("Enter row (0-9): ");
            int row = scanner.nextInt();

            System.out.print("Enter column (0-9): ");
            int col = scanner.nextInt();

            target = new Coordinate(row, col);

        } while (!target.isInBounds(Board.SIZE));

        return target;
    }

    @Override
    public void recordShot(Coordinate coordinate, ShotResult result) {
        // Player handles recording shot history
    }
}