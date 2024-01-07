package src.Scanner;

import src.Pair.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MyScanner {

    private final ArrayList<String> operators = new ArrayList<>(
            List.of("+", "-", "*", "/", "%", "<=", ">=", "==", "!=", "<", ">", "=", "&&", "||"));
    private final ArrayList<String> separators = new ArrayList<>(
            List.of("(", ")", ":", ";", " ", "\t", "\n", "'", "\""));
    private final ArrayList<String> reservedWords = new ArrayList<>(
            List.of("readRulesFor", "uno", "if", "else", "for", "while", "int", "float", "bool", "true", "false",
                    "char", "string", "cardsAre", "pack", "hasRule", "yo", "itsDone"));

    private SymbolTable identifierSymbolTable;
    private SymbolTable constantsSymbolTable;

    private ProgramInternalForm pif;
    private final String filePath;

    private FiniteAutomaton fa_identifier = new FiniteAutomaton("Input_Output/FA_Identifier.txt");
    private FiniteAutomaton fa_constant = new FiniteAutomaton("Input_Output/FA_integer_constants.txt");

    public MyScanner(String filePath) {
        this.filePath = filePath;
        this.identifierSymbolTable = new SymbolTable(10);
        this.constantsSymbolTable = new SymbolTable(10);
        this.pif = new ProgramInternalForm();
    }

    public boolean isValidIdentifier(String token) {
        return fa_identifier.acceptsSequence(token);
    }

    public boolean isValidConstant(String token) {
        return fa_constant.acceptsSequence(token);
    }

    private static Integer findLineInFile(String searchString) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Input_Output/Input/Token.in"))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                if (line.contains(searchString)) {
                    return lineNumber;
                }
                lineNumber++;
            }
            return -1;
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return -1;
        }
    }

    private String readFile() throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        Scanner scanner = new Scanner(new File(this.filePath));
        while (scanner.hasNextLine()) {
            fileContent.append(scanner.nextLine()).append("\n");
        }
        return fileContent.toString().replace("\t", "");
    }

    private List<Pair<String, Pair<Integer, Integer>>> createListOfProgramsElems() {
        try {
            String content = this.readFile();
            String separatorsString = this.separators.stream().reduce("", (a, b) -> (a + b));
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
        StringBuilder createdString = new StringBuilder();
        boolean isStringConstant = false;
        boolean isCharConstant = false;
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

    public void scan() {
        List<Pair<String, Pair<Integer, Integer>>> tokens = createListOfProgramsElems();
        AtomicBoolean lexicalErrorExists = new AtomicBoolean(false);

        if (tokens == null) {
            return;
        }

        tokens.forEach(t -> {
            String token = t.getFirst();
            if (this.reservedWords.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), findLineInFile(token));
            } else if (this.operators.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), findLineInFile(token));
            } else if (this.separators.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), findLineInFile(token));
            } else if (isValidConstant(token)) {
                this.constantsSymbolTable.add(token);
                this.pif.add(new Pair<>("CONST: { " + token + " }", constantsSymbolTable.findPositionOfTerm(token)), 0);

            } else if (isValidIdentifier(token)) {
                this.identifierSymbolTable.add(token);
                this.pif.add(
                        new Pair<>("IDENT: { " + token + " }", identifierSymbolTable.findPositionOfTerm(token)),
                        1);

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