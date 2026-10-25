
import java.util.Random;

public class BotPlayer extends Player {
    private TargetingStrategy targetingStrategy;
    private PlacementStrategy placementStrategy;

    public BotPlayer(String name, TargetingStrategy targetingStrategy, PlacementStrategy placementStrategy) {
        super(name);
        this.targetingStrategy = targetingStrategy;
        this.placementStrategy = placementStrategy;
    }

    @Override
    public Coordinate chooseTarget() {
        return targetingStrategy.chooseTarget();
    }
}
