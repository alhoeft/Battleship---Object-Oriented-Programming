# Battleship: Design

A single-player Battleship game: one human versus one bot. Built in Java with Spring Boot on the back end and HTML/JS on the front end.

The full class diagram is in [`uml.md`](uml.md).

## Architecture at a glance

| Layer | Classes | Responsibility |
| --- | --- | --- |
| Web | `GameController` | Maps HTTP endpoints to Java methods and returns JSON |
| Service | `GameService` | Holds the active `Game` between requests and runs each turn |
| Domain | `Game`, `Player`, `Board`, `Ship`, strategies, listeners, factories | Plain Java game rules with no Spring dependencies |

Keeping Spring out of the domain layer means every game rule can be unit tested with plain JUnit.

---

## Design patterns

We use three design patterns: **Strategy**, **Factory**, and **Observer**.

### 1. Strategy: bot targeting and ship placement

**Classes:** `TargetingStrategy` (interface), `RandomTargeting`, `PlacementStrategy` (interface), `RandomPlacement`, `BotPlayer`

**Problem it solves:** The bot needs a way to choose where to fire and where to place ships. We want to be able to change that behavior, for example by adding a smarter "hunt" bot later, without rewriting `BotPlayer` or `Game`.

**How it's used:** `BotPlayer` doesn't contain any decision logic itself. It receives a `TargetingStrategy` and a `PlacementStrategy` through its constructor and delegates to them. Adding a new difficulty level means writing one new class that implements `TargetingStrategy`.

```java
public interface TargetingStrategy {
    Coordinate chooseTarget();
    void recordResult(Coordinate target, ShotResult result);
}

public class BotPlayer extends Player {
    private final TargetingStrategy targeting;
    private final PlacementStrategy placement;

    public BotPlayer(String name, TargetingStrategy targeting, PlacementStrategy placement) {
        super(name);
        this.targeting = targeting;
        this.placement = placement;
    }

    @Override
    public Coordinate chooseTarget() {
        return targeting.chooseTarget();
    }

    @Override
    public void recordResult(Coordinate target, ShotResult result) {
        targeting.recordResult(target, result);
    }
}
```

`RandomTargeting` keeps a set of cells it has already fired at, and picks random coordinates until it finds a new one. A future `HuntTargeting` could use `recordResult()` to remember hits and aim at neighboring cells.

### 2. Factory: creating players and the fleet

**Classes:** `PlayerFactory`, `FleetFactory`

**Problem it solves:** Creating a bot means wiring it with the right strategies, and every player needs the same five ships. If that setup were scattered across `GameService` and `Game`, changing the fleet or the bot's default strategy would mean hunting through several files.

**How it's used:**
- `FleetFactory.createStandardFleet()` returns the five standard ships, built from `ShipType`.
- `PlayerFactory.createBot()` builds a `BotPlayer` with its `RandomTargeting` and `RandomPlacement` strategies, and places its fleet.
- `GameService` asks the factory for players and never calls `new` on a strategy itself.

```java
public class FleetFactory {
    public List<Ship> createStandardFleet() {
        List<Ship> fleet = new ArrayList<>();
        for (ShipType type : ShipType.values()) {
            fleet.add(new Ship(type));
        }
        return fleet;
    }
}

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
}
```

### 3. Observer: game events

**Classes:** `GameEventListener` (interface), `ConsoleGameLogger`, `MessageLog`, `Game`

**Problem it solves:** Several parts of the app care when something happens, such as a shot, a sunk ship, or the end of the game. The console log and the on-screen message feed both need to react. If `Game` called each of them directly, it would have to change every time we added a new reaction.

**How it's used:** `Game` keeps a list of `GameEventListener`s and notifies all of them when an event occurs. It doesn't know or care what each listener does with the event.

- `ConsoleGameLogger` prints events, which helps with debugging.
- `MessageLog` collects messages like "Bot sank your Destroyer!" that the front end displays.

```java
public interface GameEventListener {
    void onShot(Player shooter, Coordinate target, ShotResult result);
    void onShipSunk(Player owner, ShipType type);
    void onGameOver(Player winner);
}

// Inside Game
private final List<GameEventListener> listeners = new ArrayList<>();

public void addListener(GameEventListener listener) {
    listeners.add(listener);
}

private void notifyShot(Player shooter, Coordinate target, ShotResult result) {
    for (GameEventListener listener : listeners) {
        listener.onShot(shooter, target, result);
    }
}
```

### Also present: Template Method

The abstract `Player` class defines shared behavior (`hasLost()`, owning a `Board`) and leaves `chooseTarget()` abstract for each subclass to fill in. `Game` calls `currentTurn.chooseTarget()` without knowing whether a human or the bot is playing.

---

## Object-oriented principles

### Coding to abstractions

Classes depend on interfaces, not concrete implementations:

- `BotPlayer` depends on `TargetingStrategy` and `PlacementStrategy`, not on `RandomTargeting` or `RandomPlacement`.
- `Game` depends on `GameEventListener`, not on a specific logger.
- `Game` stores its current player as the abstract `Player` type.

### Polymorphism

- `Game.takeTurn()` calls `currentTurn.chooseTarget()`. The same call runs different code for `HumanPlayer` (returns the clicked cell) and `BotPlayer` (asks its strategy).
- `BotPlayer` calls `targeting.chooseTarget()`, which behaves differently depending on which strategy was injected.
- `Game` calls `listener.onShot(...)` on every listener, and each implementation reacts in its own way.

### Dependency injection

DI shows up in two places:

1. **Spring constructor injection.** Spring creates `GameService` and passes it into `GameController`. We use constructor injection so the dependency is explicit in the code.

    ```java
    @RestController
    public class GameController {
        private final GameService gameService;

        public GameController(GameService gameService) {
            this.gameService = gameService;
        }
    }
    ```

2. **Plain-Java constructor injection.** `BotPlayer` receives its strategies, and the strategies receive a `Random`, all through constructors. This makes the bot testable. A test can pass in `new Random(42)`, so the bot's "random" choices are the same on every run.

---

## Test plan

| # | Test | Class under test | What it checks |
| --- | --- | --- | --- |
| 1 | Ship off the edge is rejected | `Board` | `placeShip` returns `false` when a ship would extend past the grid |
| 2 | Overlapping ships are rejected | `Board` | `placeShip` returns `false` when cells are already occupied |
| 3 | Hit and miss are reported | `Board` | `receiveShot` returns `MISS` on empty water and `HIT` on a ship |
| 4 | Final hit sinks a ship | `Board`, `Ship` | Hitting every cell of a ship returns `SUNK` on the last hit |
| 5 | Repeat shots are rejected | `Board` | Firing at the same cell twice returns `ALREADY_FIRED` |
| 6 | Game ends when a fleet is sunk | `Game` | `allShipsSunk()` becomes true and `checkWinner()` returns the right player |
| 7 | No firing during placement | `Game` | A shot during the `PLACING` phase is rejected |
| 8 | Bot never repeats a target | `RandomTargeting` | With a seeded `Random`, 100 calls produce 100 unique cells |
| 9 | Listeners are notified | `Game` | A test listener receives `onShot` and `onGameOver` events |
| 10 | Factory builds a full fleet | `FleetFactory` | Returns 5 ships with sizes 5, 4, 3, 3, 2 |

Test 9 uses a simple fake listener that records the events it receives, which is easy to write because `GameEventListener` is an interface.
