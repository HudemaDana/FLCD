import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
    private static void printToFile(String filePath, Object object) {
        try (PrintStream printStream = new PrintStream(filePath)) {
            printStream.println(object);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void run(String filePath) {
        MyScanner scanner = new MyScanner(filePath);
        scanner.scan();
        printToFile(filePath.replace(".txt", "ST.txt"), scanner.constantSymbolTable());
        printToFile(filePath.replace(".txt", "ST.txt"), scanner.identifierSymbolTable());
        printToFile(filePath.replace(".txt", "PIF.txt"), scanner.getPif());
    }

    public static void main(String[] args) {
        run("Input_Output/P1.txt");
        run("Input_Output/P2.txt");
        run("Input_Output/P3.txt");
        run("Input_Output/P1err.txt");
    }
}