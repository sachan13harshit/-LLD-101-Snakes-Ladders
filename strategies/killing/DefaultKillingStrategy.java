import java.util.Map;
public class DefaultKillingStrategy implements KillingStrategy {
    @Override
    public void apply(Player currentPlayer, int newPosition, Map<Player, Integer> playerPositions) {
        // Default strategy: Nothing happens. Players can share the same square.
    }
}