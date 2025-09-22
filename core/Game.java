import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final Map<Player, Integer> playerPositions;
    private final Dice dice;
    private final WinningStrategy winningStrategy;
    private final KillingStrategy killingStrategy;
    private final Scanner scanner;
    private int currentPlayerIndex;
    private boolean isGameOver;

    private Game(Board board, List<Player> players, Dice dice, WinningStrategy ws, KillingStrategy ks) {
        this.board = board;
        this.players = players;
        this.dice = dice;
        this.winningStrategy = ws;
        this.killingStrategy = ks;
        this.scanner = new Scanner(System.in);
        this.playerPositions = new HashMap<>();
        players.forEach(p -> playerPositions.put(p, 0));
        this.currentPlayerIndex = 0;
        this.isGameOver = false;
    }

    public void start() {
        System.out.println("Let's start the game! Board size: " + board.getSize());
        while (!isGameOver) {
            nextTurn();
        }
        System.out.println("Game Over!");
    }

    private void nextTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println("\n--- " + currentPlayer.getName() + "'s turn ---");
        System.out.println("Current position: " + playerPositions.get(currentPlayer));
        System.out.println("Press Enter to roll the dice...");
        scanner.nextLine();

        int roll = dice.roll();
        System.out.println(currentPlayer.getName() + " rolled a " + roll);

        int currentPosition = playerPositions.get(currentPlayer);
        int newPosition = winningStrategy.calculateNewPosition(currentPosition, roll, board.getSize());

        if (newPosition != currentPosition) {
            System.out.println(currentPlayer.getName() + " moved to " + newPosition);

            // Check for transport (snake or ladder)
            if (board.getTransportAt(newPosition).isPresent()) {
                Transport transport = board.getTransportAt(newPosition).get();
                System.out.println("Landed on a " + transport.getEntityType() + "! Moving from " +
                        transport.getStart() + " to " + transport.getEnd());
                newPosition = transport.getEnd();
            }

            playerPositions.put(currentPlayer, newPosition);
            killingStrategy.apply(currentPlayer, newPosition, playerPositions);
        }

        System.out.println(currentPlayer.getName() + " is now at position " + playerPositions.get(currentPlayer));

        if (winningStrategy.hasWon(playerPositions.get(currentPlayer), board.getSize())) {
            System.out.println("\n🎉 Congratulations! " + currentPlayer.getName() + " has won the game! 🎉");
            isGameOver = true;
            return;
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int size;
        private List<Player> players;
        private final List<Transport> transports = new ArrayList<>();
        private final Dice dice = new SixSidedDice();
        private WinningStrategy winningStrategy = new DefaultWinningStrategy();
        private KillingStrategy killingStrategy = new DefaultKillingStrategy();
        private boolean isLayoutSet = false;

        public Builder withPlayers(List<Player> players) {
            this.players = players;
            return this;
        }

        public Builder withRandomLayout(int boardSize) {
            if (isLayoutSet) {
                throw new IllegalStateException("Cannot set a random layout after manually adding snakes or ladders.");
            }

            this.size = boardSize;
            int numEntities = boardSize / 10;
            Set<Integer> occupied = new HashSet<>();
            occupied.add(1);
            occupied.add(boardSize);

            generateSnakes(numEntities, occupied);
            generateLadders(numEntities, occupied);

            System.out.println("Generated " + this.transports.size() / 2 + " snakes and " +
                    this.transports.size() / 2 + " ladders.");
            this.isLayoutSet = true;
            return this;
        }

        private void generateSnakes(int numEntities, Set<Integer> occupied) {
            for (int i = 0; i < numEntities; i++) {
                while (true) {
                    int start = ThreadLocalRandom.current().nextInt(2, size);
                    int end = ThreadLocalRandom.current().nextInt(2, start);
                    if (!occupied.contains(start) && !occupied.contains(end)) {
                        this.transports.add(new Snake(start, end));
                        occupied.add(start);
                        occupied.add(end);
                        break;
                    }
                }
            }
        }

        private void generateLadders(int numEntities, Set<Integer> occupied) {
            for (int i = 0; i < numEntities; i++) {
                while (true) {
                    int start = ThreadLocalRandom.current().nextInt(2, size - 1);
                    int end = ThreadLocalRandom.current().nextInt(start + 1, size);
                    if (!occupied.contains(start) && !occupied.contains(end)) {
                        this.transports.add(new Ladder(start, end));
                        occupied.add(start);
                        occupied.add(end);
                        break;
                    }
                }
            }
        }

        public Builder withSnakes(List<Snake> snakes) {
            if (isLayoutSet) {
                throw new IllegalStateException("Cannot add snakes after setting a random layout.");
            }
            this.transports.addAll(snakes);
            isLayoutSet = true;
            return this;
        }

        public Builder withLadders(List<Ladder> ladders) {
            if (isLayoutSet) {
                throw new IllegalStateException("Cannot add ladders after setting a random layout.");
            }
            this.transports.addAll(ladders);
            isLayoutSet = true;
            return this;
        }

        public Builder withWinningStrategy(WinningStrategy strategy) {
            this.winningStrategy = strategy;
            return this;
        }

        public Builder withKillingStrategy(KillingStrategy strategy) {
            this.killingStrategy = strategy;
            return this;
        }

        public Game build() {
            if (players == null || players.size() < 2) {
                throw new IllegalStateException("Game requires at least 2 players.");
            }

            if (!isLayoutSet || size == 0) {
                this.size = 100;
            }

            Map<Integer, Transport> transportMap = new HashMap<>();
            transports.forEach(t -> transportMap.put(t.getStart(), t));

            Board board = new Board(size, transportMap);
            return new Game(board, players, dice, winningStrategy, killingStrategy);
        }
    }
}