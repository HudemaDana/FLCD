package Parser;

import Grammar.Grammar;
import kotlin.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Table {
    // Map<Integer, Map<Integer, Pair<Integer, Integer>>> rows;

    Map<Integer, Row> rows;

    public void constructTable(Stack<Pair<String, Integer>> workingStack, Grammar grammar) {
        this.rows = new HashMap<>();
        int index = 0;

        Row initialState = new Row();
        initialState.index = index;
        initialState.info = workingStack.firstElement().getFirst();
        initialState.parent = 0;
        initialState.sibling = 0;
        this.rows.put(0, initialState);

        Stack<Pair<String, Integer>> stack = new Stack<>();

        for(var elem: workingStack) {
            if(elem.getSecond() != -1) { //is non-terminal
                stack.add(0, elem);
            }
        }

        Stack<Integer> parents = new Stack<>();
        parents.add(0);
        index++;

        while(!stack.isEmpty()) {
            Pair<String, Integer> pair = stack.pop();
            String nonTerminal = pair.getFirst();
            int parent = parents.pop();
            int sibling = 0;

            List<String> production = grammar.getProductionsForNonTerminal(nonTerminal).get(pair.getSecond());
            for(String elem: production) {
                Row row = new Row();
                row.index = index;
                row.info = elem;
                row.parent = parent;
                row.sibling = sibling;
                this.rows.put(index, row);
                sibling = index;
                if(grammar.isNonTerminal(elem))
                    parents.add(0, index);
                index++;
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(var row: rows.values()){
            sb.append(row).append("\n");
        }
        return sb.toString();
    }
}
