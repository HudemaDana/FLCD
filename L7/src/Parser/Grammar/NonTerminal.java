package Grammar;

import java.util.ArrayList;
import java.util.List;

public class NonTerminal implements Symbol {
    private String symbol;
    List<List<Symbol>> productions = new ArrayList<>();

    public NonTerminal(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public List<List<Symbol>> getProductions() {
        return productions;
    }

    @Override
    public String toString() {
        return symbol;
    }
}