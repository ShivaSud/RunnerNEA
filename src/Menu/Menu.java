package Menu;

import DatabaseConnect.DatabaseConnect;
import Services.InputValidator;
import Services.LoginSystem;
import Services.RouteGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Menu {
    static DatabaseConnect db = new DatabaseConnect();
    static RouteGenerator routeGenerator = new RouteGenerator(db);
    static LoginSystem loginSystem = new LoginSystem(db);
    static InputValidator inputValidator = new InputValidator();

    public static void main(String[] args) throws SQLException {
        db.initialiseDatabase(); // Initialize database connection
        displayMenu(); // Display the main menu
        db.closeDatabase(); // Close the database connection
    }

    // Method to display the login or account creation menu.
    public static void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter 1 to log in to an existing account");
        System.out.println("Please enter 2 to create a new account");
        int runnerID;
        int loginOrSignUpOption = inputValidator.integerValidation(scanner.nextInt(), new HashSet<>(Arrays.asList(1, 2)));
        if (loginOrSignUpOption == 1) {
            System.out.println("Please enter your name to log in to your account");
            String name = scanner.next();
            runnerID = login(name); // Handle login
            name = db.findName(runnerID);
            newRouteOrOldRoute(runnerID, name); // Ask user to create or access a route
        } else {
            System.out.println("Please enter your name to create a new account");
            String name = scanner.next();
            while (loginSystem.checkLogInDetails(name)) {
                System.out.println("This name already exists. Please enter another name.");
                name = scanner.next();
            }
            loginSystem.createNewAccount(name);// Create new account
            runnerID = db.findRunner(name);// Retrieve runner ID
            newRouteOrOldRoute(runnerID, name); // Ask user to create or access a route
        }
    }

    // Method to handle the login process.
    public static int login(String name) {
        Scanner scanner = new Scanner(System.in);
        while (!loginSystem.checkLogInDetails(name)) { // Validate login
            System.out.println("The username " + name + " does not exist");
            System.out.println("Press 1 to retype your username");
            System.out.println("Press 2 to create a new account");
            int wrongLoginDetailsOption = inputValidator.integerValidation(scanner.nextInt(), new HashSet<>(Arrays.asList(1, 2)));
            if (wrongLoginDetailsOption == 1) {
                name = scanner.next(); // Retry login with new username
                loginSystem.checkLogInDetails(name);
            } else {
                System.out.println("Please enter your name to create a new account");
                name = scanner.next(); // Create new account
                loginSystem.createNewAccount(name);
            }
        }
        return db.findRunner(name); // Return runner ID
    }
    // Method to offer user the option to create a new route or access an old one.
    public static void newRouteOrOldRoute(int runnerID, String name) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello " + name + "!");
        System.out.println("Press 1 to create a new route");
        System.out.println("Press 2 to access an old route");

        int newRouteOrOldRouteOption = inputValidator.integerValidation(scanner.nextInt(), new HashSet<>(Arrays.asList(1, 2)));
        if (newRouteOrOldRouteOption == 1) {
            createNewRoute(runnerID, name); // Create a new route
        } else {
            routeGenerator.viewOldRoute(name); // View an existing route
        }
    }

    // Method to create a new route with user input.
    public static void createNewRoute(int runnerID, String name) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Here are all of the points you can start and end your run at: ");
        System.out.println("(A) Kingston Gate");
        System.out.println("(B) Ham Cross");
        System.out.println("(C) Ham Gate");
        System.out.println("(D) Richmond Gate");
        System.out.println("(E) Pond Slade");
        System.out.println("(F) Broomfield Hill Wood");
        System.out.println("(G) Broomfield Hill Gate");
        System.out.println("(H) Petersham Gate");
        System.out.println("(I) Sidmouth Wood");
        System.out.println("(J) Lawn Plantation");
        System.out.println("(K) Robin Hood Gate");
        System.out.println("(L) Bog Gate");
        System.out.println("(M) Duchess Wood");
        System.out.println("(N) Sheen Gate");
        System.out.println("(O) Kill Cat Corner");
        System.out.println("(P) Roehampton Gate");

        System.out.println("Please enter the start point of the run");
        char startPoint = inputValidator.charValidation(scanner.next().charAt(0), new HashSet<>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P')));
        System.out.println("Please enter the end point of the run");
        char endPoint = inputValidator.charValidation(scanner.next().charAt(0), new HashSet<>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P')));

        System.out.println("Enter all the attributes you want to prioritise from the list below, and press 6 to stop:");
        System.out.println("(1) Elevation");
        System.out.println("(2) Scenery");
        System.out.println("(3) Quality");
        System.out.println("(4) Distance");
        System.out.println("(5) Safety");
        System.out.println("Please note that the more values you pick the more diluted your result will be");

        ArrayList<Integer> preferredIntegerOptions = new ArrayList<>();
        ArrayList<String> preferredOptions = new ArrayList<>();
        int option = inputValidator.integerValidation(scanner.nextInt(), new HashSet<>(Arrays.asList(1, 2, 3, 4, 5)));
        preferredIntegerOptions.add(option);
        int counter = 1;
        HashSet<Integer> validPriorityOptions = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        while (counter <= 5) {
            option = inputValidator.integerValidation(scanner.nextInt(), validPriorityOptions);
            if(option == 6){
                break;
            }
            preferredIntegerOptions.add(option);
            validPriorityOptions.remove(option);
            counter++;
        }
        //preferredIntegerOptions.remove(preferredIntegerOptions.size() - 1);
        System.out.println(preferredIntegerOptions);
        for (int i = 0; i < preferredIntegerOptions.size(); i++) {
            preferredOptions.add(optionNumberToString(preferredIntegerOptions.get(i)));
        }
        System.out.println("Pick one from the following options on how the weather will be like in Richmond Park today?");
        System.out.println("(1) Sunny");
        System.out.println("(2) Rainy");
        System.out.println("(3) Foggy");
        System.out.println("(4) Windy");
        int weatherOption = inputValidator.integerValidation(scanner.nextInt(), new HashSet<>(Arrays.asList(1, 2, 3, 4)));

        String weatherType = "";
        if (weatherOption == 1) {
            weatherType = "Sunny";
        } else if (weatherOption == 2) {
            weatherType = "Rainy";
        } else if (weatherOption == 3) {
            weatherType = "Foggy";
        } else if (weatherOption == 4) {
            weatherType = "Windy";
        }
        routeGenerator.generateRoute(startPoint, endPoint, preferredOptions, weatherType, runnerID, name); // Generate route
    }

    // Converts the option number to its corresponding string representation.
    public static String optionNumberToString(int optionNumber) {
        if (optionNumber == 1) {
            return "elevation";
        } else if (optionNumber == 2) {
            return "scenery";
        } else if (optionNumber == 3) {
            return "quality";
        } else if (optionNumber == 4) {
            return "length";
        } else if (optionNumber == 5) {
            return "safety";
        } else {
            throw new IllegalArgumentException("Invalid option number");
        }
    }
}