import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyScanner {

    private final ArrayList<String> operators = new ArrayList<>(
            List.of("+", "-", "*", "/", "%", "<=", ">=", "==", "!=", "<", ">", "=", "&&", "||"));
    private final ArrayList<String> separators = new ArrayList<>(
            List.of("(", ")", ":", ";", " ", "\t", "\n", "'", "\""));
    private final ArrayList<String> reservedWords = new ArrayList<>(
            List.of("readRulesFor", "uno", "if", "else", "for", "while", "int", "float", "bool", "true", "false",
                    "char", "string", "cardsAre", "pack", "hasRule", "yo", "itsDone"));

    private String identifierRegex = "^(?!(" + String.join("|", reservedWords) + "$))[a-zA-Z_][a-zA-Z0-9_]*$";

    // private Pattern identifierPattern = Pattern.compile(identifierRegex);
    private Pattern constantPattern = Pattern
            .compile("^(0|[1-9]|[1-9][0-9]*|[-+][1-9][0-9]*|'[a-zA-Z]'|\"[0-9]*[a-zA-Z ]*\")$");

    private SymbolTable identifierSymbolTable;
    private SymbolTable constantsSymbolTable;

    private final String filePath;
    private ProgramInternalForm pif;

    public MyScanner(String filePath) {
        this.filePath = filePath;
        this.identifierSymbolTable = new SymbolTable(100);
        this.constantsSymbolTable = new SymbolTable(100);
        this.pif = new ProgramInternalForm();
    }

    private String readFile() throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        Scanner scanner = new Scanner(new File(this.filePath));
        while (scanner.hasNextLine()) {
            fileContent.append(scanner.nextLine()).append("\n");
        }
        // System.out.println(fileContent.toString());
        return fileContent.toString().replace("\t", "");
    }

    private List<Pair<String, Pair<Integer, Integer>>> createListOfProgramsElems() {
        try {
            String content = this.readFile();
            String separatorsString = this.separators.stream().reduce("", (a, b) -> (a + b));
            // System.out.println(separatorsString);
            StringTokenizer tokenizer = new StringTokenizer(content, separatorsString, true);

            List<String> tokens = Collections.list(tokenizer)
                    .stream()
                    .map(t -> (String) t)
                    .collect(Collectors.toList());

            return tokenize(tokens);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Pair<String, Pair<Integer, Integer>>> tokenize(List<String> tokensToBe) {

        List<Pair<String, Pair<Integer, Integer>>> resultedTokens = new ArrayList<>();
        boolean isStringConstant = false;
        boolean isCharConstant = false;
        StringBuilder createdString = new StringBuilder();
        int numberLine = 1;
        int numberColumn = 1;

        for (String t : tokensToBe) {
            switch (t) {
                case "\"":
                    if (isStringConstant) {
                        createdString.append(t);
                        resultedTokens.add(new Pair<>(createdString.toString(), new Pair<>(numberLine, numberColumn)));
                        createdString = new StringBuilder();
                    } else {
                        createdString.append(t);
                    }
                    isStringConstant = !isStringConstant;
                    break;
                case "'":
                    if (isCharConstant) {
                        createdString.append(t);
                        resultedTokens.add(new Pair<>(createdString.toString(), new Pair<>(numberLine, numberColumn)));
                        createdString = new StringBuilder();
                    } else {
                        createdString.append(t);
                    }
                    isCharConstant = !isCharConstant;
                    break;
                case "\n":
                    numberLine++;
                    numberColumn = 1;
                    break;
                default:
                    if (isStringConstant) {
                        createdString.append(t);
                    } else if (isCharConstant) {
                        createdString.append(t);
                    } else if (!t.equals(" ")) {
                        resultedTokens.add(new Pair<>(t, new Pair<>(numberLine, numberColumn)));
                        numberColumn++;
                    }
                    break;
            }
        }
        return resultedTokens;
    }

    /**
     * In this method, we scan the list of created tokens and we classify each of
     * them in a category:
     * a) 2 - for reservedWords
     * b) 3 - for operators
     * c) 4 - for separators
     * d) 0 - for constants
     * e) 1 - for identifiers
     * If the token is a constant or an identifier we add it to the Symbol Table
     * After figuring out the category, we add them to the ProgramInternalForm +
     * their position in the symbol table ( (-1, -1) for anything that is not a
     * constant and an identifier ) + their category (0, 1, 2, 3, 4)
     * If the token is not in any of the categories, we print a message with the
     * line and the column of the error + the token which is invalid.
     */

    public void scan() {

        List<Pair<String, Pair<Integer, Integer>>> tokens = createListOfProgramsElems();
        AtomicBoolean lexicalErrorExists = new AtomicBoolean(false);

        if (tokens == null) {
            return;
        }

        tokens.forEach(t -> {
            String token = t.getFirst();
            // System.out.println(token);
            if (this.reservedWords.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 2);
            } else if (this.operators.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 3);
            } else if (this.separators.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 4);
            } else if (Pattern.compile("^(0|[1-9]|[1-9][0-9]*|[-+][1-9][0-9]*|'[a-zA-Z]'|\"[0-9]*[a-zA-Z ]*\")$")
                    .matcher(token).matches()) {
                this.constantsSymbolTable.add(token);
                this.pif.add(new Pair<>("CONST", constantsSymbolTable.findPositionOfTerm(token)), 0);

            } else if (Pattern.compile("^([a-zA-Z]|_)|[a-zA-Z_0-9]*").matcher(token).matches()) {
                this.identifierSymbolTable.add(token);
                this.pif.add(new Pair<>("IDENTIFIER", identifierSymbolTable.findPositionOfTerm(token)), 1);

            } else {
                Pair<Integer, Integer> pairLineColumn = t.getSecond();
                System.out.println("Error at line: " + pairLineColumn.getFirst() + " and column: "
                        + pairLineColumn.getSecond() + ", invalid token: " + t.getFirst());
                lexicalErrorExists.set(true);
            }
        });

        if (!lexicalErrorExists.get()) {
            System.out.println("Program is lexically correct!");
        }

    }

    public ProgramInternalForm getPif() {
        return this.pif;
    }

    public SymbolTable constantSymbolTable() {
        return this.constantsSymbolTable;
    }

    public SymbolTable identifierSymbolTable() {
        return this.identifierSymbolTable;
    }

}