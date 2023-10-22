import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        String[] reservedWords = { "readRulesFor", "uno", "if", "else", "for", "while", "int", "float", "char",
                "string", "cardsAre", "pack", "hasRule" };
        String identifierRegex = "^(?!(" + String.join("|", reservedWords) + "$))[a-zA-Z_][a-zA-Z0-9_]*$";

        Pattern identifierPattern = Pattern.compile(identifierRegex);
        Pattern constantPattern = Pattern.compile("^[0-9]+$");

        SymbolTable identifierSymbolTable = new SymbolTable(10);
        SymbolTable constantsSymbolTable = new SymbolTable(10);

        String[] wordList = { "uno", "3", "var", "despacito", "12", "int", "aaaaaaaaaaaaaaaa" };
        for (int a = 0; a < wordList.length; a++) {
            Matcher identifierMatcher = identifierPattern.matcher(wordList[a]);
            if (identifierMatcher.matches()) {
                identifierSymbolTable.add(wordList[a]);
            } else {
                Matcher constantMatcher = constantPattern.matcher(wordList[a]);
                if (constantMatcher.matches()) {
                    constantsSymbolTable.add(wordList[a]);
                }
            }
        }

        System.out.println(identifierSymbolTable.containsTerm("uno"));
        System.out.println(identifierSymbolTable.containsTerm("12"));

        System.out.println(identifierSymbolTable.containsTerm("despacito"));
        Pair position = identifierSymbolTable.findPositionOfTerm("despacito");
        System.out.println(position);

        System.out.println(constantsSymbolTable.containsTerm("uno"));

        System.out.println(constantsSymbolTable.containsTerm("12"));
        position = constantsSymbolTable.findPositionOfTerm("12");
        System.out.println(position);

    }
}
