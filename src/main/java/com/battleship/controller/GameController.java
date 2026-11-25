package com.battleship.controller;

import com.battleship.dto.FireRequest;
import com.battleship.dto.GameStateResponse;
import com.battleship.dto.PlaceRequest;
import com.battleship.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Maps the four REST endpoints to GameService. Every endpoint returns the
 * full game state as JSON. Rejected moves come back with HTTP 200 and the
 * "error" field filled in.
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    public GameStateResponse newGame() {
        return gameService.newGame();
    }

    @PostMapping("/place")
    public GameStateResponse place(@RequestBody PlaceRequest request) {
        if (request.ship() == null || request.orientation() == null) {
            return gameService.getState().withError("Missing ship or orientation.");
        }
        return gameService.placeShip(request.ship(), request.row(), request.col(), request.orientation());
    }

    @PostMapping("/fire")
    public GameStateResponse fire(@RequestBody FireRequest request) {
        return gameService.fire(request.row(), request.col());
    }

    @GetMapping("/state")
    public GameStateResponse state() {
        return gameService.getState();
    }
}