package java.com.battleship.players;

public class HumanPlayer extends Player {
    private Coordinate nextTarget;
    private final TargetingStrategy targetingStrategy;

    public HumanPlayer(String name, TargetingStrategy targetingStrategy){
        super(name);
        this.targetingStrategy = targetingStrategy;
    }

    public boolean placeShip(Ship ship, Coordinate coordinate, Orientation orentiation){
        return getBoard().placeShip(ship,coordinate,orentiation);
    }

    public Coordinate setNextTarget(Coordinate coordinate){
        this.nextTarget = coordinate;
    }

    @Override
    public Coordinate chooseTarget() {
        return targetingStrategy.chooseTarget();
    }
}
