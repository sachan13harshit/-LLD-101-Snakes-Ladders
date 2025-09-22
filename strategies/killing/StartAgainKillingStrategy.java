import java.util.Map;

public class StartAgainKillingStrategy implements KillingStrategy {
    @Override
    public void apply(Player currentPlayer, int newPosition, Map<Player, Integer> playerPositions) {
        Player otherPlayer = null;
        for (Map.Entry<Player, Integer> entry : playerPositions.entrySet()) {
            if (!entry.getKey().equals(currentPlayer) && entry.getValue() == newPosition) {
                otherPlayer = entry.getKey();
                break;
            }
        }

        if (otherPlayer != null) {
            System.out.println(
                    "Oh no! " + currentPlayer.getName() + " landed on " + otherPlayer.getName() + "'s square!");
            System.out.println(otherPlayer.getName() + " goes back to the start (position 0).");
            playerPositions.put(otherPlayer, 0);
        }
    }
}