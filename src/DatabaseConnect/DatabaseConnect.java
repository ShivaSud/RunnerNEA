package DatabaseConnect;

import java.sql.*;

public class DatabaseConnect {
    private Connection conn = null;

    // Initialises the database connection using SQLite and sets auto-commit to false.
    public void initialiseDatabase(){
        try{
            String url = "jdbc:sqlite:RunnerNEA.db";
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
            System.out.println("Opened database successfully");
        } catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Creates and returns a SQL Statement object for executing SQL commands.
    public Statement createStatement() throws SQLException{
        return conn.createStatement();
    }

    // Commits the current transaction to the database.
    public void commit() throws SQLException {
        conn.commit();
    }

    // Closes the database connection.
    public void closeDatabase() throws SQLException {
        conn.close();
    }

    // Retrieves the pathID from the PATH table based on the provided path's start and end points.
    public String calculatePathID(String path){
        Statement stmt;
        ResultSet rs;
        String pathID = "";
        try{
            stmt = createStatement();
            rs = stmt.executeQuery("SELECT pathID FROM PATH WHERE startPoint = '"+ path.charAt(0) + "' AND endPoint = '" + path.charAt(1) + "'");
            pathID = rs.getString("pathID");
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pathID;
    }

    // Determines the next available ID for a given table and ID column by finding the highest current ID.
    public int calculateNextID(String table, String ID){
        Statement stmt;
        ResultSet rs;
        int nextId = 0;
        try{
            stmt = createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + table + ";");
            while (rs.next()){
                int id = rs.getInt(ID);
                if(id > nextId){
                    nextId = id;
                }
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return nextId + 1;
    }

    // Finds and returns the runnerID for a given runner's name from the Runner table. Returns -1 if not found.
    public int findRunner(String name){
        Statement stmt;
        ResultSet rs;
        int runnerID = -1;
        try{
            stmt = createStatement();
            rs = stmt.executeQuery("SELECT runnerID FROM Runner WHERE name = '" + name + "';");
            runnerID = rs.getInt("runnerID");
            rs.close();
            stmt.close();
        }
        catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return runnerID;
    }
    // Finds and returns the runnerID for a given runner's name from the Runner table. Returns -1 if not found.
    public String findName(int runnerID){
        Statement stmt;
        ResultSet rs;
        String name = "";
        try{
            stmt = createStatement();
            rs = stmt.executeQuery("SELECT name FROM Runner WHERE runnerID = '" + runnerID + "';");
            name = rs.getString("name");
            rs.close();
            stmt.close();
        }
        catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return name;
    }

}