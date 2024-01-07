package Grammar;

import java.io.BufferedReader;

import java.io.*;
import java.util.*;

public class Grammar {
    private Set<String> nonTerminals;
    private Set<String> terminals;
    private Map<String, List<List<String>>> productions;
    private String startSymbol;

    public Grammar() {
        nonTerminals = new HashSet<>();
        terminals = new HashSet<>();
        productions = new HashMap<>();
    }

    public void readGrammarFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            Arrays.stream(br.readLine().split(" "))
                    .forEach(symbol -> nonTerminals.add(symbol)); // Changed
            Arrays.stream(br.readLine().split(" "))
                    .forEach(symbol -> terminals.add(symbol)); // Changed

            startSymbol = br.readLine().trim(); // Changed

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("->");
                String lhs = parts[0].trim(); // Changed
                String[] rhsParts = parts[1].trim().split("\\s+"); // Split RHS into parts
                List<String> rhs = Arrays.asList(rhsParts); // Create a list of RHS parts

                productions.computeIfAbsent(lhs, k -> new ArrayList<>()).add(rhs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printNonTerminals() {
        System.out.println("Non-terminals: " + nonTerminals);
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public void printTerminals() {
        System.out.println("Terminals: " + terminals);
    }

    public Set<String> getTerminals() {
        return terminals;
    }
    public void printProductions() {
        System.out.println("Productions: " + productions);
    }

    public Map<String, List<List<String>>> getProductions() {
        return productions;
    }

    public void printProductionsForNonTerminal(String nonTerminal) {
        System.out.println("Productions for " + nonTerminal + ": " + productions.get(nonTerminal));
    }

    public List<List<String>> getProductionsForNonTerminal(String nonTerminal) {
        return productions.get(nonTerminal);
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public boolean isCFG() {
        for (String nonTerminal : productions.keySet()) {
            if (!nonTerminals.contains(nonTerminal) || nonTerminal.contains(" ")) {
                return false;
            }
        }
        return true;
    }

    public boolean isNonTerminal(String element) {
        return nonTerminals.contains(element);
    }

    public boolean isTerminal(String element) {
        return terminals.contains(element);
    }

}