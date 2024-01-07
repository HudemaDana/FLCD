package src.Scanner;

import src.Pair.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProgramInternalForm {
    private List<Pair<String, Pair<Integer, Integer>>> tokenPositionPair;

    private List<Integer> types;

    public ProgramInternalForm() {
        this.tokenPositionPair = new ArrayList<>();
        this.types = new ArrayList<>();
    }

    public void add(Pair<String, Pair<Integer, Integer>> pair, Integer type) {
        if (pair.getFirst() != "IDENT" && pair.getFirst() != "CONST" && isTokenInPIF(pair.getFirst()))
            return;

        this.tokenPositionPair.add(pair);
        this.types.add(type);
    }

    @Override
    public String toString() {
        StringBuilder table = new StringBuilder();
        table.append("Token \t\t\t\t| Token ID \t\t| ST Position\n");
        table.append("----------------------------\n");

        for (int i = 0; i < this.tokenPositionPair.size(); i++) {
            String token = this.tokenPositionPair.get(i).getFirst();
            String tokenID = types.get(i).toString();
            String stPosition = String.valueOf(this.tokenPositionPair.get(i).getSecond());

            table.append(String.format("%-20s | %-10s | %-10s\n", token, tokenID, stPosition));
        }

        return table.toString();
    }

    private Boolean isTokenInPIF(String token) {
        AtomicBoolean isTokenTaken = new AtomicBoolean(false);
        tokenPositionPair.forEach(pair -> {
            if (token.contains(pair.getFirst())) {
                isTokenTaken.set(true);
            }
        });
        return isTokenTaken.get();
    }
}
