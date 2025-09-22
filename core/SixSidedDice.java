import java.util.concurrent.ThreadLocalRandom;

public class SixSidedDice implements Dice {
    @Override
    public int roll() {
        return ThreadLocalRandom.current().nextInt(1, 7);
    }
}