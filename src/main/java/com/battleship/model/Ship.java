package com.battleship.model;

public class Ship {

    private final ShipType type;
    private int hits;

    public Ship(ShipType type) {
        this.type = type;
        this.hits = 0;
    }

    public void hit() {
        hits++;
    }

    public boolean isSunk() {
        return hits >= type.getSize();
    }

    public ShipType getType() {
        return type;
    }

    public int getSize() {
        return type.getSize();
    }

    public int getHits() {
        return hits;
    }
}