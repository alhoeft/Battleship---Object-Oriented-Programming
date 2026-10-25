package java.com.battleship.players;

public class HumanPlayer extends Player {
    private Coordinate nextTarget;

    public HumanPlayer(String name){
        super(name);
    }

    public boolean placeShip(Ship ship, Coordinate coordinate, Orientation orentiation){
        return getBoard().placeShip(ship,coordinate,orentiation);
    }

    public Coordinate setNextTarget(Coordinate coordinate){
        this.nextTarget = coordinate;
    }

    @Override
    public Coordinate chooseTarget() {
        return nextTarget;
    }
}
