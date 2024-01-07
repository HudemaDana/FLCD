package src.Parser.RecursiveDescent;

import src.Parser.Grammar.*;
import src.Parser.ParsingTable.*;
import src.Pair.Pair;

import java.util.*;

public class RecursiveDescent {
    private Grammar grammar;

    private int position;
    private Stack<Pair<String, Integer>> workingStack;
    private Stack<String> inputStack;
    private StateType state;

    enum StateType {
        q, b, f, e
    }

    public RecursiveDescent(Grammar grammar) {
        this.grammar = grammar;
        this.position = 0;
        this.workingStack = new Stack<>();
        this.inputStack = new Stack<>();
        this.state = StateType.q;
    }

    public void parse(String input) {
        parse(Collections.singletonList(input));
    }

    public void parse(List<String> sequenceList) {
        inputStack.push(grammar.getStartSymbol());
        System.out.println(partingStatus());

        while (state != StateType.f && state != StateType.e) {
            if (state == StateType.q) {
                if (isEndOfInput(sequenceList.size()) && inputStack.isEmpty()) {
                    success();
                } else {
                    if (!inputStack.isEmpty()) {
                        String topElem = inputStack.peek();
                        if (grammar.isNonTerminal(topElem)) {
                            expand();
                        } else if (topElem.equals(String.valueOf(sequenceList.get(position)))) {
                            advance();
                        } else {
                            momentaryInsuccess();
                        }
                    }
                }
            } else if (state == StateType.b) {
                String topElem = workingStack.peek().getFirst();
                if (grammar.isTerminal(topElem)) {
                    back();
                } else {
                    anotherTry();
                }
            }
        }

        if (state == StateType.e) {
            System.out.println("Parsing Error");
        } else {
            System.out.println("Sequence Accepted");
            buildStringOfProductions(workingStack);
            Table table = new Table();
            table.constructTable(workingStack, grammar);
            System.out.println(table.toString());
        }
    }

    private void buildStringOfProductions(Stack<Pair<String, Integer>> workingStack) {
        StringBuilder sb = new StringBuilder("Productions String: ");
        for (var elem : workingStack) {
            String elemName = elem.getFirst();
            if (grammar.isNonTerminal(elemName))
                sb.append(elemName).append(elem.getSecond() + 1).append(" ");
        }
        System.out.println(sb);
    }

    private void expand() {
        System.out.println("-----Expand----\n");
        String nonTerminal = inputStack.pop();
        List<String> firstProduction = grammar.getProductionsForNonTerminal(nonTerminal).get(0);
        Pair<String, Integer> pair = new Pair<>(nonTerminal, 0);
        workingStack.push(pair);
        // for (int i=0; i<firstProduction.size(); i++) {
        // inputStack.push(firstProduction.get(i));
        // }
        for (int i = firstProduction.size() - 1; i >= 0; i--) {
            inputStack.push(firstProduction.get(i));
        }
        System.out.println(partingStatus());
    }

    private void advance() {
        System.out.println("-----Advance----\n");
        String terminal = inputStack.pop();
        position++;
        Pair<String, Integer> pair = new Pair<>(terminal, -1);
        workingStack.push(pair);
        System.out.println(partingStatus());
    }

    private void momentaryInsuccess() {
        System.out.println("-----Momentary Insuccess----\n");
        state = StateType.b;
        System.out.println(partingStatus());
    }

    private void back() {
        System.out.println("-----Back----\n");
        String terminal = workingStack.pop().getFirst();
        position--;
        inputStack.push(terminal);
        System.out.println(partingStatus());
    }

    private void anotherTry() {
        System.out.println("-----Another Try----\n");
        Pair<String, Integer> pair = workingStack.pop();
        if (pair.getSecond() != -1) {
            String nonTerminal = pair.getFirst();
            Integer productionNumber = pair.getSecond();

            List<List<String>> productions = grammar.getProductionsForNonTerminal(nonTerminal);

            boolean isLastProduction = productionNumber + 1 == productions.size();

            if (!isLastProduction) {
                state = StateType.q;
                productionNumber++;

                String nextProduction = productions.get(productionNumber).get(0);

                // inputStack.push(nextProduction);
                Pair<String, Integer> newPair = new Pair<>(nonTerminal, productionNumber);
                workingStack.push(newPair);

                for (int i = 0; i < productions.size(); i++) {
                    inputStack.pop();
                }

                inputStack.push(nextProduction);
            } else {
                for (int i = 0; i < productions.size(); i++) {
                    inputStack.pop();
                }
                inputStack.push(nonTerminal);
            }
            if (position == 0 && Objects.equals(nonTerminal, grammar.getStartSymbol()))
                state = StateType.e;
        }
        System.out.println(partingStatus());
    }

    private void success() {
        state = StateType.f;
        System.out.println(partingStatus());
    }

    private boolean isEndOfInput(int sequenceListSize) {
        return position >= sequenceListSize;
    }

    private String partingStatus() {
        return "State: " + state + ", Position: " + position + ", Working stack: " + workingStack + ", Input stack: "
                + inputStack + '\n';
    }
}
