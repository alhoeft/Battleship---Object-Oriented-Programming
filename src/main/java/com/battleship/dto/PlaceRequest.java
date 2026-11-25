package com.battleship.dto;

import com.battleship.model.Orientation;
import com.battleship.model.ShipType;

/** Body of POST /api/game/place, e.g. {"ship":"CARRIER","row":0,"col":0,"orientation":"HORIZONTAL"} */
public record PlaceRequest(ShipType ship, int row, int col, Orientation orientation) {
}