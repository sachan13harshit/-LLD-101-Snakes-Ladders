import java.util.Map;
import java.util.Optional;

public class Board {
    private final int size;
    private final Map<Integer, Transport> transports;

    public Board(int size, Map<Integer, Transport> transports) {
        this.size = size;
        this.transports = transports;
    }

    public int getSize() {
        return size;
    }

    public Optional<Transport> getTransportAt(int position) {
        return Optional.ofNullable(transports.get(position));
    }
}