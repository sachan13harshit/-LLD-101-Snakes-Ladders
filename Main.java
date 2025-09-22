import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<Player> players = List.of(
                    new HumanPlayer("harshit"),
                    new HumanPlayer("ahsk"));

            Game.Builder gameBuilder = Game.builder()
                    .withPlayers(players)
                    .withRandomLayout(100);

            WinningStrategy winningStrategy = WinningStrategyFactory
                    .createWinningStrategy(WinningStrategyType.EXACT_MATCH);
            KillingStrategy killingStrategy = KillingStrategyFactory
                    .createKillingStrategy(KillingStrategyType.START_AGAIN);

            gameBuilder.withWinningStrategy(winningStrategy);
            gameBuilder.withKillingStrategy(killingStrategy);

            Game snakeAndLadderGame = gameBuilder.build();
            snakeAndLadderGame.start();

        } catch (IllegalStateException | IllegalArgumentException e) {
            System.err.println("Error during game setup: " + e.getMessage());
        }
    }
}