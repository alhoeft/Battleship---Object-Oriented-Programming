package com.battleship.dto;

/** Body of POST /api/game/fire, e.g. {"row":3,"col":7} */
public record FireRequest(int row, int col) {
}