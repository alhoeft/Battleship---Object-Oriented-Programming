package java.com.battleship.players;

import java.util.List;
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
                new RandomTargeting(random),
                new RandomPlacement(random));
        bot.placeFleet(fleetFactory.createStandardFleet());
        return bot;
    }

    public HumanPlayer createHuman(){
        return new createHuman(name)
    }
}

