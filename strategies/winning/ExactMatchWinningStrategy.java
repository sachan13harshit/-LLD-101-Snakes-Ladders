
public class ExactMatchWinningStrategy implements WinningStrategy {
    @Override
    public boolean hasWon(int position, int boardSize) {
        return position == boardSize;
    }

    @Override
    public int calculateNewPosition(int currentPosition, int diceRoll, int boardSize) {
        int newPosition = currentPosition + diceRoll;
        if (newPosition > boardSize) {
            System.out.println(
                    "Exact match required! Roll would overshoot the board. Staying at position " + currentPosition);
            return currentPosition;
        }
        return newPosition;
    }
}