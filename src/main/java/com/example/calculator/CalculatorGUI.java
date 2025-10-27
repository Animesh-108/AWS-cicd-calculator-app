package com.example.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Creates a graphical user interface (GUI) for the calculator.
 * This class replaces the command-line 'Main.java'.
 */
public class CalculatorGUI extends JPanel implements ActionListener {

    // --- GUI Components ---
    private JTextField displayField;
    private JPanel buttonPanel;

    // --- Calculator State ---
    private Calculator calculator;
    private String operator = "";
    private double num1 = 0;
    private boolean isTypingNumber = false;

    /**
     * Constructor: Sets up the GUI components.
     */
    public CalculatorGUI() {
        // Use the core logic class
        this.calculator = new Calculator();

        // Use BorderLayout for the main panel
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Create the Display Field (at the top)
        displayField = new JTextField("0");
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setFont(new Font("Arial", Font.BOLD, 24));
        add(displayField, BorderLayout.NORTH);

        // 2. Create the Button Panel (in the center)
        buttonPanel = new JPanel();
        // Use a 4x4 grid for the buttons
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5)); 
        
        // Define button labels
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+"
        };

        // Create and add all buttons
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.addActionListener(this); // Register 'this' class as the listener
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);

        // 3. Create a 'Clear' button (at the bottom)
        JButton clearButton = new JButton("C");
        clearButton.setFont(new Font("Arial", Font.BOLD, 18));
        clearButton.addActionListener(e -> clear());
        add(clearButton, BorderLayout.SOUTH);
    }

    /**
     * Main event handler for all button clicks.
     * @param e The ActionEvent triggered by a button press.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand(); // Get the text from the button

        try {
            switch (command) {
                case "0": case "1": case "2": case "3": case "4":
                case "5": case "6": case "7": case "8": case "9":
                    handleNumber(command);
                    break;
                case ".":
                    handleDecimal();
                    break;
                case "+": case "-": case "*": case "/":
                    handleOperator(command);
                    break;
                case "=":
                    handleEquals();
                    break;
            }
        } catch (Exception ex) {
            displayField.setText("Error");
            // Reset state after an error
            operator = "";
            num1 = 0;
            isTypingNumber = false;
        }
    }

    /**
     * Handles number button presses.
     * @param num The number (as a string) that was pressed.
     */
    private void handleNumber(String num) {
        if (!isTypingNumber) {
            // Start of a new number
            displayField.setText(num);
            isTypingNumber = true;
        } else {
            // Append to the existing number
            if (displayField.getText().equals("0")) {
                displayField.setText(num);
            } else {
                displayField.setText(displayField.getText() + num);
            }
        }
    }

    /**
     * Handles the decimal point button.
     */
    private void handleDecimal() {
        if (!isTypingNumber) {
            // Start new number with "0."
            displayField.setText("0.");
            isTypingNumber = true;
        } else if (!displayField.getText().contains(".")) {
            // Add decimal point if one doesn't exist
            displayField.setText(displayField.getText() + ".");
        }
    }

    /**
     * Handles operator (+, -, *, /) button presses.
     * @param op The operator (as a string) that was pressed.
     */
    private void handleOperator(String op) {
        if (isTypingNumber) {
            // This is the first operator in a sequence
            num1 = Double.parseDouble(displayField.getText());
            isTypingNumber = false;
        }
        // Store the operator
        operator = op;
        // The next number typed will be a new number
    }

    /**
     * Handles the equals (=) button press.
     */
    private void handleEquals() {
        if (operator.isEmpty() || !isTypingNumber) {
            // Not enough information to calculate
            return; 
        }

        double num2 = Double.parseDouble(displayField.getText());
        double result = 0;

        switch (operator) {
            case "+":
                result = calculator.add(num1, num2);
                break;
            case "-":
                result = calculator.subtract(num1, num2);
                break;
            case "*":
                result = calculator.multiply(num1, num2);
                break;
            case "/":
                try {
                    result = calculator.divide(num1, num2);
                } catch (IllegalArgumentException e) {
                    displayField.setText("Error: Div by 0");
                    operator = "";
                    num1 = 0;
                    return;
                }
                break;
        }

        // Display the result
        displayField.setText(String.valueOf(result));

        // Reset for the next calculation
        num1 = result;
        operator = "";
        isTypingNumber = false;
    }

    /**
     * Resets the calculator state.
     */
    private void clear() {
        displayField.setText("0");
        operator = "";
        num1 = 0;
        isTypingNumber = false;
    }

    /**
     * The main method to create the window and run the application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT)
        // This is the standard, safe way to start a Swing application.
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Swing Calculator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Add our CalculatorGUI panel to the frame
            frame.setContentPane(new CalculatorGUI());
            
            // Size the window automatically based on its components
            frame.pack();
            
            // Center the window on the screen
            frame.setLocationRelativeTo(null);
            
            // Make the window visible
            frame.setVisible(true);
        });
    }
}
