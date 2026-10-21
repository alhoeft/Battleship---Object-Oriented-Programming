package java.com.battleship.model;

public class Cell {

    private com.battleship.model.Ship ship;    // null if empty water
    private boolean hit;

    public boolean hasShip() {
        return ship != null;
    }

    public boolean isHit() {
        return hit;
    }

    public void markHit() {
        hit = true;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }
}