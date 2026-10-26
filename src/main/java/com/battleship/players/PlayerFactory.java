package com.battleship.players;
import com.battleship.model.FleetFactory;
import com.battleship.strategies.*;

import java.util.Random;

public class PlayerFactory {
    private final FleetFactory fleetFactory;
    private final Random random;

    public PlayerFactory(FleetFactory fleetFactory, Random random) {
        this.fleetFactory = fleetFactory;
        this.random = random;
    }

    public BotPlayer createBot() {
        BotPlayer bot = new BotPlayer("Bot",
                new RandomTargetingStrategy(random),
                new RandomPlacementStrategy(random));
        bot.placeFleet(fleetFactory.createStandardFleet());
        return bot;
    }

    public HumanPlayer createHuman(String name) {
        HumanPlayer human = new HumanPlayer(
                name,
                new HumanTargetingStrategy(),
                new HumanPlacementStrategy()
        );

        human.placeFleet(fleetFactory.createStandardFleet());

        return human;
    }
}

