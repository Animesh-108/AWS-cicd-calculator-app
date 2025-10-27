package com.example.calculator;

/**
 * Main class to run the calculator from the command line.
 * Expects three arguments: number1 operator number2
 * Example: 10 + 5
 */
public class Main {

    public static void main(String[] args) {
        // 1. Check for the correct number of arguments
        if (args.length != 3) {
            printUsage();
            return;
        }

        try {
            // 2. Parse arguments
            double num1 = Double.parseDouble(args[0]);
            String operator = args[1];
            double num2 = Double.parseDouble(args[2]);

            Calculator calculator = new Calculator();
            double result;

            // 3. Perform calculation based on the operator
            switch (operator) {
                case "+":
                    result = calculator.add(num1, num2);
                    break;
                case "-":
                    result = calculator.subtract(num1, num2);
                    break;
                case "*":
                case "x": // Support both * and x for multiplication
                    result = calculator.multiply(num1, num2);
                    break;
                case "/":
                    result = calculator.divide(num1, num2);
                    break;
                default:
                    System.err.println("Error: Unknown operator '" + operator + "'");
                    printUsage();
                    return;
            }

            // 4. Print the result
            System.out.println("Result: " + result);

        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number provided.");
            printUsage();
        } catch (IllegalArgumentException e) {
            // This catches the "divide by zero" error from the Calculator class
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar maven-calculator-1.0-SNAPSHOT.jar <num1> <operator> <num2>");
        System.out.println("Operators: + - * /");
        System.out.println("Example: java -jar maven-calculator-1.0-SNAPSHOT.jar 10.5 + 5");
    }
}
