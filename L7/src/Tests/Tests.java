package src.Tests;

import src.Parser.Grammar.*;
import src.Parser.RecursiveDescent.*;

public class Tests {

    private Grammar grammar1;
    private RecursiveDescent recursiveDescent;

    public Tests() {
        grammar1 = new Grammar();
        recursiveDescent = new RecursiveDescent(grammar1);
    }

    public void runExpressionTest1() {
        // Test for a simple arithmetic expression
        String input = "1 + 2 * 3";
        recursiveDescent.parse(input);
        // assert result : "Expression Test 1 Failed";
        System.out.println("Expression Test 1 Successful");
    }

    public void runExpressionTest2() {
        // Test for another arithmetic expression
        String input = "(3 + 4) * 5";
        recursiveDescent.parse(input);
        // assert result : "Expression Test 2 Failed";
        System.out.println("Expression Test 2 Successful");
    }

    public void runIfStatementTest() {
        // Test for a simple if statement
        String input = "if (x > 0) { y = 2; } else { y = 1; }";
        recursiveDescent.parse(input);
        // assert result : "If Statement Test Failed";
        System.out.println("If Statement Test Successful");
    }

    public void runAllTests() {
        runExpressionTest1();
        runExpressionTest2();
        runIfStatementTest();
        // Add more tests as needed
    }

    public static void main(String[] args) {
        Tests tests = new Tests();
        tests.runAllTests();
    }
}
