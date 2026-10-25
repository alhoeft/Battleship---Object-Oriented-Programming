package java.com.battleship.PlayStrategies;

import java.com.battleship.PlayStrategies.TargetingStrategy;

public class RandomTargetingStrategy implements TargetingStrategy{
    private final Random random;
    private Set<Coordinate> alreadyFired;

    public RandomTargetingStrategy(Random random) {
        this.random = random;
    }

    public Coordinate chooseTarget() {
        Coordinate target;

        do {
            int row = random.nextInt(Board.SIZE);
            int col = random.nextInt(Board.SIZE);

            target = new Coordinate(row, col);

        } while (alreadyFired.contains(target));

        return target;
    }

    @Override
    public void recordShot(Coordinate coordinate, ShotResult result) {
        alreadyFired.add(coordinate);
    }
}