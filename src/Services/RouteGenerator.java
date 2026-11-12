package Services;

import DatabaseConnect.DatabaseConnect;
import Menu.Menu;
import DataStructures.Graph;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class RouteGenerator {
    InputValidator inputValidator = new InputValidator();
    DatabaseConnect db;

    // Constructor to initialize the DatabaseConnect instance
    public RouteGenerator(DatabaseConnect db) {
        this.db = db;
    }

    // Method to view old saved routes
    public void viewOldRoute(String name) {
        Statement stmt;
        ResultSet rs;
        System.out.println("Here are all of the routes that you have saved: ");
        try {
            stmt = db.createStatement();

            // SQL query to fetch saved routes for the given runner name
            String sql = "SELECT routeID, weatherType, startpoint, endPoint, chosenOptions FROM Route, Runner WHERE Route.runnerID = Runner.runnerID AND name = '" + name + "'";
            rs = stmt.executeQuery(sql);
            boolean noRunsSaved = true;
            // Loop through the saved routes
            while (rs.next()) {
                System.out.println("Route number: " + rs.getString("routeID") + ", WeatherType: " + rs.getString("weatherType") + ", StartPoint: " + rs.getString("startPoint") + ", EndPoint: " + rs.getString("endPoint") + ", Chosen options: " + rs.getString("chosenOptions"));
                printSummaryStatistics(rs.getInt("routeID"));
                System.out.println();
                noRunsSaved = false;
            }
            // If no runs are saved, display message
            if (noRunsSaved) {
                System.out.println("You have no runs saved yet");
                return;
            } else {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Pick the number route that you wish to run on today");
                int chosenRun = scanner.nextInt();
                System.out.println("Here is every path on this route!");
                printRoute(chosenRun); // Print the route
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        // Provide user with option to return to the main menu or exit
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press 1 to return to the main menu or 2 to end the program");
        int endProgram = inputValidator.integerValidation(scanner.nextInt(), new HashSet<>(Arrays.asList(1, 2)));
        if (endProgram == 1) {
            Menu.newRouteOrOldRoute(db.findRunner(name), name);
        } else {
            System.exit(0);
        }
    }

    // Method to generate a new route
    public void generateRoute(char startPoint, char endPoint, ArrayList<String> preferredOptions, String weatherOption, int runnerID, String name) {
        Statement stmt;
        ResultSet rs;
        Graph<Character> graph = new Graph<>();

        try {
            stmt = db.createStatement();
            String sql = "SELECT * FROM Path";
            rs = stmt.executeQuery(sql);

            // Build the graph based on the available paths
            while (rs.next()) {
                double average = 0.0;
                // Calculate the average score based on preferred options and weather impact
                for (String option : preferredOptions) {
                    String pathType = rs.getString("type");
                    int weatherOptionBias = weatherOptionBias(option, weatherOption, pathType);
                    average += (rs.getDouble(option) + weatherOptionBias);
                }
                average /= preferredOptions.size();
                graph.add(rs.getString("startPoint").charAt(0), rs.getString("endPoint").charAt(0), average);
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        // Use Dijkstra's algorithm to find the best route
        DijkstrasAlgorithm<Character> dijkstrasAlgorithm = new DijkstrasAlgorithm<>(graph);
        String route = dijkstrasAlgorithm.path(startPoint, endPoint);
        int routeID = db.calculateNextID("Route", "routeID");
        initialPrint(route); // Print the initial route

        // Prompt user to save the generated route
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press 1 to save this route, 2 to not save this route");
        int saveRoute = inputValidator.integerValidation(scanner.nextInt(), new HashSet<>(Arrays.asList(1, 2)));
        if (saveRoute == 1) {
            saveRoute(routeID, route, runnerID, weatherOption, startPoint, endPoint, preferredOptions.toString());
        }

        // Option to return to the main menu or exit the program
        System.out.println("Press 1 to return to the main menu, 2 to terminate this program");
        int terminateProgram = inputValidator.integerValidation(scanner.nextInt(), new HashSet<>(Arrays.asList(1, 2)));
        if (terminateProgram == 1) {
            Menu.newRouteOrOldRoute(runnerID, name);
        } else {
            System.exit(0);
        }
    }

    // Method to calculate weather impact bias for a path option
    public int weatherOptionBias(String pathOption, String weatherOption, String pathType) {
        Statement stmt;
        ResultSet rs;
        int weatherOptionBias = 0;
        String weatherOptionType = pathType + pathOption;
        try {
            stmt = db.createStatement();
            String sql = "SELECT " + weatherOptionType + " FROM WeatherImpact WHERE weatherType = '" + weatherOption + "'";
            rs = stmt.executeQuery(sql); // Get weather bias for the path option
            while(rs.next()){
                weatherOptionBias = rs.getInt(weatherOptionType);
            }
            stmt.close();
            db.commit();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return weatherOptionBias; // Return the calculated weather bias
    }

    // Method to save the generated route into the database
    public void saveRoute(int routeID, String route, int runnerID, String weatherType, char startPoint, char endPoint, String chosenOptions) {
        Statement stmt;
        String path;
        try {
            stmt = db.createStatement();
            // SQL query to save route details
            String sql = "INSERT INTO Route (routeID, runnerID, weatherType, startPoint, endPoint, chosenOptions) " +
                    "VALUES(" + routeID + ", '" + runnerID + "', '" + weatherType + "', '" + startPoint + "', '" + endPoint + "', '" + chosenOptions + "')";

            stmt.executeUpdate(sql);

            // Save each path in the route
            for (int i = 0; i < route.length() - 1; i++) {
                path = route.substring(i, i + 2);
                sql = "INSERT INTO RoutePath (routePathID, pathID, routeID) " +
                        "VALUES(" + db.calculateNextID("RoutePath", "routePathID") + ", '" + db.calculatePathID(path) + "', " + routeID + ")";

                stmt.executeUpdate(sql);
            }

            stmt.close();
            db.commit(); // Commit the changes to the database
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // Method to print the routes for a given route ID
    public void printRoute(int routeID) {
        Statement stmt;
        ResultSet rs;
        try {
            stmt = db.createStatement();
            String sql = "SELECT pathName FROM RoutePath, Path WHERE RouteID = '" + routeID + "' AND RoutePath.pathID = Path.PathID";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString("pathName")); // Print the path name
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // Method to print the initial route
    public void initialPrint(String path) {
        Statement stmt;
        ResultSet rs;
        try {
            stmt = db.createStatement();
            // Loop through the path and print each segment
            for (int i = 0; i < path.length() - 1; i++) {
                String sql = "SELECT pathName FROM Path WHERE startPoint = '" + path.charAt(i) + "' AND endPoint = '" + path.charAt(i + 1) + "'";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    System.out.println(rs.getString("pathName")); // Print the path name
                }
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // Method to print the summary statistics for a route
    public void printSummaryStatistics(int routeID) {
        Statement stmt;
        ResultSet rs;
        String[] options = {"elevation", "scenery", "quality", "length", "safety"};
        double[] summaryStatistics = new double[5];
        int length = 0;
        try {
            stmt = db.createStatement();
            String sql = "SELECT elevation, scenery, quality, length, safety FROM Path, RoutePath WHERE RouteID = " + routeID + " AND RoutePath.pathID = Path.PathID";
            rs = stmt.executeQuery(sql);
            // Loop through the paths and calculate summary statistics
            while (rs.next()) {
                for (int i = 0; i < options.length; i++) {
                    summaryStatistics[i] += rs.getDouble(options[i]);
                }
                length++;
            }
            for(int i = 0; i < summaryStatistics.length; i++) {
                if(i != 3){
                    summaryStatistics[i] /= length;
                    summaryStatistics[i] = Math.round(summaryStatistics[i]);
                }
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        // Print the summary statistics for the route
        for (int i = 0; i < summaryStatistics.length; i++) {
            System.out.print(options[i] + ": " + summaryStatistics[i] + " ");
        }
    }
}