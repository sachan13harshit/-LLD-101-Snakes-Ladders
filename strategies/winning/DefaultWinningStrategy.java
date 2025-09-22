
public class DefaultWinningStrategy implements WinningStrategy {
    @Override
    public boolean hasWon(int position, int boardSize) {
        return position >= boardSize;
    }

    @Override
    public int calculateNewPosition(int currentPosition, int diceRoll, int boardSize) {
        int newPosition = currentPosition + diceRoll;
        if (newPosition > boardSize) {
            return boardSize; // Player wins by reaching or exceeding the board size
        }
        return newPosition;
    }
}