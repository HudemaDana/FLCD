import java.util.Objects;

public class Transition {
    private final String state;
    private final String symbol;

    public Transition(String state, String symbol) {
        this.state = state;
        this.symbol = symbol;
    }

    public String getState() {
        return state;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Transition that = (Transition) o;
        return Objects.equals(state, that.state) && Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, symbol);
    }
}
