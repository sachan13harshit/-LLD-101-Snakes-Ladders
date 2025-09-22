
public class KillingStrategyFactory {

    public static KillingStrategy createKillingStrategy(KillingStrategyType type) {
        switch (type) {
            case DEFAULT:
                return new DefaultKillingStrategy();
            case START_AGAIN:
                return new StartAgainKillingStrategy();
            default:
                throw new IllegalArgumentException("Unknown killing strategy type: " + type);
        }
    }
}