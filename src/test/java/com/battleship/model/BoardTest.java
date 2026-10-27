package com.battleship.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    // ---------- placeShip ----------

    // Required test 1
    @Test
    void placeShip_offEdge_returnsFalse() {
        boolean placed = board.placeShip(new Ship(ShipType.CARRIER), new Coordinate(0, 8), Orientation.HORIZONTAL);

        assertFalse(placed);
        assertTrue(board.getShips().isEmpty());
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                assertFalse(board.getCell(new Coordinate(row, col)).hasShip(),
                        "Cell (" + row + ", " + col + ") should be empty after a failed placement");
            }
        }
    }

    // Required test 2
    @Test
    void placeShip_overlap_returnsFalse() {
        assertTrue(board.placeShip(new Ship(ShipType.DESTROYER), new Coordinate(0, 0), Orientation.HORIZONTAL));

        boolean secondPlaced = board.placeShip(new Ship(ShipType.CRUISER), new Coordinate(0, 1), Orientation.VERTICAL);

        assertFalse(secondPlaced);
        assertEquals(1, board.getShips().size());
    }

    @Test
    void placeShip_offBottomEdge_returnsFalse() {
        assertFalse(board.placeShip(new Ship(ShipType.CARRIER), new Coordinate(6, 0), Orientation.VERTICAL));
    }

    @Test
    void placeShip_exactlyFitsAtEdge_returnsTrue() {
        assertTrue(board.placeShip(new Ship(ShipType.CARRIER), new Coordinate(0, 5), Orientation.HORIZONTAL));
    }

    @Test
    void placeShip_vertical_marksEveryCoveredCell() {
        Ship cruiser = new Ship(ShipType.CRUISER);

        assertTrue(board.placeShip(cruiser, new Coordinate(2, 4), Orientation.VERTICAL));

        assertSame(cruiser, board.getCell(new Coordinate(2, 4)).getShip());
        assertSame(cruiser, board.getCell(new Coordinate(3, 4)).getShip());
        assertSame(cruiser, board.getCell(new Coordinate(4, 4)).getShip());
        assertFalse(board.getCell(new Coordinate(5, 4)).hasShip());
        assertFalse(board.getCell(new Coordinate(2, 5)).hasShip());
    }

    // ---------- receiveShot ----------

    // Required test 3
    @Test
    void receiveShot_hitAndMiss() {
        board.placeShip(new Ship(ShipType.DESTROYER), new Coordinate(0, 0), Orientation.HORIZONTAL);

        assertEquals(ShotResult.HIT, board.receiveShot(new Coordinate(0, 0)));
        assertEquals(ShotResult.MISS, board.receiveShot(new Coordinate(5, 5)));
    }

    // Required test 4
    @Test
    void receiveShot_lastHit_returnsSunk() {
        board.placeShip(new Ship(ShipType.DESTROYER), new Coordinate(0, 0), Orientation.HORIZONTAL);

        assertEquals(ShotResult.HIT, board.receiveShot(new Coordinate(0, 0)));
        assertEquals(ShotResult.SUNK, board.receiveShot(new Coordinate(0, 1)));
    }

    // Required test 5
    @Test
    void receiveShot_sameCellTwice_returnsAlreadyFired() {
        board.receiveShot(new Coordinate(3, 3));

        assertEquals(ShotResult.ALREADY_FIRED, board.receiveShot(new Coordinate(3, 3)));
    }

    @Test
    void receiveShot_sameShipCellTwice_doesNotDoubleCountHit() {
        Ship destroyer = new Ship(ShipType.DESTROYER);
        board.placeShip(destroyer, new Coordinate(0, 0), Orientation.HORIZONTAL);

        board.receiveShot(new Coordinate(0, 0));
        board.receiveShot(new Coordinate(0, 0));

        assertEquals(1, destroyer.getHits());
        assertFalse(destroyer.isSunk());
    }

    @Test
    void receiveShot_outOfBounds_throws() {
        assertThrows(IllegalArgumentException.class, () -> board.receiveShot(new Coordinate(10, 0)));
        assertThrows(IllegalArgumentException.class, () -> board.receiveShot(new Coordinate(0, -1)));
    }

    // ---------- allShipsSunk ----------

    @Test
    void allShipsSunk_falseWhileAnyShipAfloat() {
        board.placeShip(new Ship(ShipType.DESTROYER), new Coordinate(0, 0), Orientation.HORIZONTAL);
        board.placeShip(new Ship(ShipType.DESTROYER), new Coordinate(5, 5), Orientation.HORIZONTAL);

        board.receiveShot(new Coordinate(0, 0));
        board.receiveShot(new Coordinate(0, 1));

        assertFalse(board.allShipsSunk());
    }

    @Test
    void allShipsSunk_trueAfterEveryShipSunk() {
        board.placeShip(new Ship(ShipType.DESTROYER), new Coordinate(0, 0), Orientation.HORIZONTAL);

        board.receiveShot(new Coordinate(0, 0));
        board.receiveShot(new Coordinate(0, 1));

        assertTrue(board.allShipsSunk());
    }

    // ---------- getOpponentView ----------

    @Test
    void getOpponentView_hidesShipsUntilHit() {
        board.placeShip(new Ship(ShipType.CARRIER), new Coordinate(0, 0), Orientation.HORIZONTAL);

        CellView[][] view = board.getOpponentView();

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                assertEquals(CellView.UNKNOWN, view[row][col]);
            }
        }
    }

    @Test
    void getOpponentView_showsHitsAndMisses() {
        board.placeShip(new Ship(ShipType.CARRIER), new Coordinate(0, 0), Orientation.HORIZONTAL);
        board.receiveShot(new Coordinate(0, 0)); // hit
        board.receiveShot(new Coordinate(9, 9)); // miss

        CellView[][] view = board.getOpponentView();

        assertEquals(CellView.HIT, view[0][0]);
        assertEquals(CellView.MISS, view[9][9]);
        assertEquals(CellView.UNKNOWN, view[0][1]); // unhit ship cell stays hidden
    }
}