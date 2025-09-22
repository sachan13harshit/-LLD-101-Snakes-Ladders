import java.util.Map;

public interface KillingStrategy {
    void apply(Player currentPlayer, int newPosition, Map<Player, Integer> playerPositions);
}