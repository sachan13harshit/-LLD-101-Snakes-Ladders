public interface WinningStrategy {
    boolean hasWon(int position, int boardSize);

    int calculateNewPosition(int currentPosition, int diceRoll, int boardSize);
}