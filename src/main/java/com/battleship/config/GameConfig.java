package com.battleship.config;

import com.battleship.model.FleetFactory;
import com.battleship.players.PlayerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * Tells Spring how to build the plain-Java objects that GameService needs.
 * Spring calls these once at startup and injects the results through
 * constructors (dependency injection).
 */
@Configuration
public class GameConfig {

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public FleetFactory fleetFactory() {
        return new FleetFactory();
    }

    @Bean
    public PlayerFactory playerFactory(FleetFactory fleetFactory, Random random) {
        return new PlayerFactory(fleetFactory, random);
    }
}