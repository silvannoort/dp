package nl.hu.dp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    private static Connection getConnection() throws SQLException {

        String url = "jdbc:postgresql://localhost/ovchip";
        String user = "postgres";
        String password = "Waterpolo10";


        return DriverManager.getConnection(url, user, password);
    }

    private static void getAllReizigers() {
        String query = "SELECT * FROM reiziger";

        try (Connection connection = getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);


            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {
                int id = resultSet.getInt("reiziger_id");
                String voorletters = resultSet.getString("voorletters");
                String tussenvoegsel = resultSet.getString("tussenvoegsel");
                String achternaam = resultSet.getString("achternaam");
                String geboortedatum = resultSet.getString("geboortedatum");

                System.out.printf("Reiziger ID: %d, Naam: %s %s %s, Geboortedatum: %s%n",
                        id, voorletters, tussenvoegsel != null ? tussenvoegsel : "", achternaam, geboortedatum);
            }
        } catch (SQLException e) {
            System.err.println("Fout bij het ophalen van reizigers: " + e.getMessage());
        }
    }


    public static void main(String[] args) {

        getAllReizigers();
    }
}
