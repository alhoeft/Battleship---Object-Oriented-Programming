package com.battleship.model;

public record Coordinate(int row, int col) {
    public boolean isInBounds(int size) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }
}