import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Name: Abdullah Bin Zubair
   Date: 1st May 2024
   Class: COMP167
   Section: 003
   Project: 03
   Description of Program: 
   This program implements a scientific calculator with a graphical user interface (GUI) using Java Swing components. The calculator supports
   basic arithmetic operations such as addition, subtraction, multiplication, and division, as well as more advanced functions including 
   square root, exponentiation, trigonometric functions, logarithms, and percentages. Users can interact with the calculator by clicking on
   buttons corresponding to numeric digits, mathematical operators, and various mathematical functions. They can also input expressions 
   directly into the input field. The program evaluates the expressions and displays the result in the output field. The calculator handles
   errors gracefully, such as division by zero or invalid input, by displaying "Error" in the output field. Additionally, it provides 
   functionalities like clearing the input/output fields and exiting the application. The GUI is designed with a pleasing color scheme and
   layout to enhance user experience and readability. The program is intended for educational purposes to demonstrate GUI programming in 
   Java and basic calculator functionality.
 */  

public class Calculating extends JFrame {
    private JTextField inputField;
    private JTextField outputField;
    private StringBuilder currentInput = new StringBuilder();

    // Constructor for the calculator GUI
    public Calculating() {
        setTitle("Scientific Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Colors for the GUI components
        Color white = new Color(255, 255, 255);
        Color parrotGreen = new Color(119, 171, 89);
        Color darkGray = new Color(64, 64, 64);
        getContentPane().setBackground(white);

        // Input and output text fields
        inputField = new JTextField(15);
        inputField.setEditable(false);
        outputField = new JTextField(30);
        outputField.setEditable(false);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(9, 4));

        // Labels for buttons
        String[] buttonLabels =
                {
                        "7", "8", "9", "0", "4",
                        "5", "6", "=", "1", "2",
                        "3", ".", "+", "-", "*",
                        "/", "sqr", "cube", "inverse", "%", "sqrt",
                        "sin", "cos", "tan", "log", "asin",
                        "acos", "atan", "ln", "sinh", "cosh",
                        "tanh", "abs", "Mod", "C", "Exit"
                };

        // Create buttons and add them to the panel
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setPreferredSize(new Dimension(40, 40));
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        // Set background colors for components
        buttonPanel.setBackground(darkGray);
        inputField.setPreferredSize(new Dimension(200, 43));
        outputField.setPreferredSize(new Dimension(400, 60));
        inputField.setBackground(parrotGreen);
        outputField.setBackground(parrotGreen);

        // Add components to the frame
        add(inputField, BorderLayout.NORTH);
        add(outputField, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set frame properties and make it visible
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ActionListener for buttons
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "=":
                    if (currentInput.length() > 0) {
                        evaluateExpression();
                    }
                    break;
                case "C":
                    clear();
                    break;
                case "Exit":
                    System.exit(0);
                    break;
                case "%":
                    handlePercentage();
                    break;
                case "Mod":
                    handleMod();
                    break;
                default:
                    currentInput.append(command);
                    inputField.setText(currentInput.toString());
                    break;
            }
        }
    }

    // Method to handle percentage calculation
    private void handlePercentage() {
        String expression = currentInput.toString();
        if (!expression.isEmpty()) {
            try {
                double value = Double.parseDouble(expression);
                double result = value / 100;
                outputField.setText(Double.toString(result));
            } catch (NumberFormatException ex) {
                outputField.setText("Error");
            }
        }
    }

    // Method to handle modulo operation
    private void handleMod() {
        String expression = currentInput.toString();
        if (expression.endsWith("Mod")) {
            outputField.setText("Error");
        }
    }

    // Method to evaluate the expression
    private void evaluateExpression() {
        String expression = currentInput.toString();
        try {
            double result;
            if (expression.matches("(sin|cos|tan|asin|acos|atan|sinh|cosh|tanh|log|ln|sqrt|sqr|cube|inverse|abs)(\\-?\\d*\\.?\\d+)?")) {
                result = applyFunction(expression.replaceAll("\\-?\\d*\\.?\\d+", ""), Double.parseDouble(expression.replaceAll("[^\\-\\d.]+", "")));
            } else {
                result = evaluate(expression);
            }
            outputField.setText(Double.toString(result));
        } catch (ArithmeticException | IllegalArgumentException ex) {
            outputField.setText("Error");
        }
        inputField.setText(currentInput.toString()); // Set input field to display the expression
        currentInput.setLength(0); // Clear current input for next calculation
    }

    // Method to evaluate the mathematical expression
    private double evaluate(String expression) {
        // Split the expression into tokens
        String[] tokens = expression.split("(?<=[-+*/^()])|(?=[-+*/^()])");
        double result = 0;
        String previousOperator = "";

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (isOperator(token)) {
                previousOperator = token;
            } else {
                double operand;
                try {
                    operand = Double.parseDouble(token);
                } catch (NumberFormatException e) {
                    operand = applyFunction(token, result);
                }

                switch (previousOperator) {
                    case "":
                        result = operand;
                        break;
                    case "+":
                        result += operand;
                        break;
                    case "-":
                        result -= operand;
                        break;
                    case "*":
                        result *= operand;
                        break;
                    case "/":
                        if (operand == 0) {
                            throw new ArithmeticException("Cannot divide by zero");
                        }
                        result /= operand;
                        break;
                    case "^":
                        result = Math.pow(result, operand);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operator: " + previousOperator);
                }
            }
        }
        return result;
    }

    // Method to check if a token is an operator
    private boolean isOperator(String token) {
        return token.matches("[+\\-*/^]");
    }

    // Method to apply mathematical functions
    private double applyFunction(String token, double result) {
        switch (token) {
            case "inverse":
                return reciprocal(result);
            case "sqrt":
                return squareRoot(result);
            case "sqr":
                return square(result);
            case "cube":
                return cube(result);
            case "log":
                return logarithm(result);
            case "ln":
                return naturalLogarithm(result);
            case "sin":
                return sin(result);
            case "cos":
                return cos(result);
            case "tan":
                return tan(result);
            case "asin":
                return asin(result);
            case "acos":
                return acos(result);
            case "atan":
                return atan(result);
            case "sinh":
                return sinh(result);
            case "cosh":
                return cosh(result);
            case "tanh":
                return tanh(result);
            case "abs":
                return Math.abs(result);
            case "Mod":
                return result % 1; // Modulo operator for decimal numbers
            default:
                throw new IllegalArgumentException("Invalid function: " + token);
        }
    }

    // Methods for various mathematical operations
    private double reciprocal(double num) {
        if (num == 0) throw new ArithmeticException("Cannot divide by zero");
        return 1 / num;
    }

    private double squareRoot(double num) {
        if (num < 0) throw new IllegalArgumentException("Square root of a negative number is undefined");
        return Math.sqrt(num);
    }

    private double square(double num) {
        return Math.pow(num, 2);
    }

    private double cube(double num) {
        return Math.pow(num, 3);
    }

    private double logarithm(double num) {
        if (num <= 0) throw new IllegalArgumentException("Logarithm of non-positive number is undefined");
        return Math.log10(num);
    }

    private double naturalLogarithm(double num) {
        if (num <= 0) throw new IllegalArgumentException("Natural logarithm of non-positive number is undefined");
        return Math.log(num);
    }

    private double sin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }

    private double cos(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    private double tan(double angle) {
        return Math.tan(Math.toRadians(angle));
    }

    private double asin(double value) {
        return Math.toDegrees(Math.asin(value));
    }

    private double acos(double value) {
        return Math.toDegrees(Math.acos(value));
    }

    private double atan(double value) {
        return Math.toDegrees(Math.atan(value));
    }

    private double sinh(double angle) {
        return Math.sinh(angle);
    }

    private double cosh(double angle) {
        return Math.cosh(angle);
    }

    private double tanh(double angle) {
        return Math.tanh(angle);
    }

    // Method to clear input and output fields
    private void clear() {
        currentInput.setLength(0);
        inputField.setText("");
        outputField.setText("");
    }

    // Main method to start the calculator
    public static void main(String[] args) {
        new Calculating();
    }
}
