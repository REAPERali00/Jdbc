package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.xdevapi.PreparableStatement;

public class Database implements Config {
    private Connection connect;
    private PreparedStatement statement;
    private String query;

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public void exeStatement(Connection connect, String query) {
        try {
            statement = connect.prepareStatement(query);
        } catch (SQLException e) {
            System.out.println("Couldn't create execute the statement");
            e.printStackTrace();
        }
    }

    public void printTable() {
        try {
            exeStatement(connect, QUERY);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                System.out.printf("ID: %d First Name: %s Last Name: %s GPA: %.2f\n",
                        rs.getInt("Student_ID"), rs.getString("First_Name"), rs.getString("Last_Name"),
                        rs.getDouble("Gpa"));
            }
        } catch (SQLException e) {
            System.out.println("Couldn't execute select query");
            e.printStackTrace();
        }

    }

    public void insertValues(String fName, String lName, double gpa) {
        String query = String.format("insert into %s (First_Name, Last_Name, Gpa) values(\"%s\",\"%s\",%f) ",
                TABLE,
                fName,
                lName, gpa);
        exeStatement(connect, query);
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting values");
            e.printStackTrace();
        }
    }

    public void updateGpa(int id, double gpa) {
        String query = String.format("update %s set Gpa = %.2f where Student_ID=%d", TABLE, gpa, id);
        try {
            statement = connect.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating the gpa value.");
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            connect = connect();
            updateGpa(2, 70);
            printTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
