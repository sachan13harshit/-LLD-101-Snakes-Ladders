public class WinningStrategyFactory {

    public static WinningStrategy createWinningStrategy(WinningStrategyType type) {
        switch (type) {
            case DEFAULT:
                return new DefaultWinningStrategy();
            case EXACT_MATCH:
                return new ExactMatchWinningStrategy();
            default:
                throw new IllegalArgumentException("Unknown winning strategy type: " + type);
        }
    }
}