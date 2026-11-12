package Services;

import java.util.HashSet;
import java.util.Scanner;

public class InputValidator {
    // Method to validate integer inputs.
    public int integerValidation(int option, HashSet<Integer> correctOptions) {
        // Check if the input is in the set of correct options.
        if (correctOptions.contains(option)) {
            return option; // Return the valid option if it's correct.
        }

        // If invalid, prompt the user again.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a valid option");
        option = scanner.nextInt(); // Get new input from user.
        return integerValidation(option, correctOptions); // Recursively validate.
    }

    // Method to validate char inputs.
    public char charValidation(char option, HashSet<Character> correctOptions) {
        // Check if the input is in the set of correct options.
        if (correctOptions.contains(option)) {
            return option; // Return the valid option if it's correct.
        }

        // If invalid, prompt the user again.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a valid option");
        option = scanner.next().charAt(0); // Get new input from user.
        return charValidation(option, correctOptions); // Recursively validate.
    }
}