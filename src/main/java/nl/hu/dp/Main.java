package nl.hu.dp;

import nl.hu.dp.dao.ReizigerDAO;
import nl.hu.dp.dao.ReizigerDAOPsql;
import nl.hu.dp.model.Reiziger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip", "postgres", "Waterpolo10");

        ReizigerDAO rdao = new ReizigerDAOPsql(conn);

        testReizigerDAO(rdao);

        conn.close();
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(90, "S", "", "Boers", Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        System.out.println("[Test] ReizigerDAO.update() test:");
        sietske.setAchternaam("UpdatedBoers");
        rdao.update(sietske);
        Reiziger updatedReiziger = rdao.findById(77);
        System.out.println("[Test] Geüpdatete reiziger: " + updatedReiziger);


        System.out.println("[Test] ReizigerDAO.delete() test:");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println("[Test] Aantal reizigers na ReizigerDAO.delete(): " + reizigers.size());


        System.out.println("[Test] ReizigerDAO.findByGbdatum() test:");
        List<Reiziger> foundReizigers = rdao.findByGbdatum(Date.valueOf(gbdatum));
        for (Reiziger r : foundReizigers) {
            System.out.println(r);
        }


    }
}
