package nl.hu.dp;

import nl.hu.dp.dao.implementaties.*;
import nl.hu.dp.dao.interfaces.AdresDAO;
import nl.hu.dp.dao.interfaces.ReizigerDAO;
import nl.hu.dp.dao.interfaces.OVChipkaartDAO;
import nl.hu.dp.model.adres.Adres;
import nl.hu.dp.model.reiziger.Reiziger;
import nl.hu.dp.model.ov.OVChipkaart;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {


        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip", "postgres", "Waterpolo10");


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("reizigerUnit");
        EntityManager em = emf.createEntityManager();


        ReizigerDAO reizigerDAOHibernate = new ReizigerDAOHibernate(em);
        OVChipkaartDAO ovcdao = new OVChipkaartDAOHibernate(em);


        AdresDAO adao = new AdresDAOPsql(conn);
        ReizigerDAO rdao = new ReizigerDAOPsql(conn, adao);


        //testReizigerDAO(reizigerDAOHibernate);
        testOVChipkaartDAO(ovcdao, rdao);
        testAdresDAO(adao, rdao);


        em.close();
        emf.close();
        conn.close();
    }



    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a.getReiziger() + ", " + a);
        }

        Reiziger reiziger = new Reiziger(100, "J", "", "Doe", java.sql.Date.valueOf("1990-01-01"));
        rdao.save(reiziger);

        Adres adres = new Adres(100, "1234AB", "56", "Straatnaam", "Utrecht", reiziger);
        adao.save(adres);

        adres.setWoonplaats("Amsterdam");
        adao.update(adres);

        adao.delete(adres);
        rdao.delete(reiziger);
    }


    private static void testOVChipkaartDAO(OVChipkaartDAO ovcdao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        Reiziger nieuweReiziger = new Reiziger(78, "J", "van", "Doe", Date.valueOf("1990-01-01"));
        rdao.save(nieuweReiziger);


        OVChipkaart ovChipkaart = new OVChipkaart(12345, Date.valueOf("2027-12-31"), 1, 50.00, nieuweReiziger);
        System.out.print("[Test] OVChipkaartDAO.save() ");
        ovcdao.save(ovChipkaart);


        List<OVChipkaart> ovChipkaarten = ovcdao.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende OVChipkaarten:");
        for (OVChipkaart ovc : ovChipkaarten) {
            System.out.println(ovc);
        }

        ovChipkaart.setKlasse(2);
        ovcdao.update(ovChipkaart);
        System.out.println("[Test] Geüpdatete OVChipkaart: " + ovcdao.findById(ovChipkaart.getKaartNummer()));


        System.out.println("[Test] OVChipkaartDAO.delete() test:");
        List<OVChipkaart> reizigerKaarten = ovcdao.findByReiziger(nieuweReiziger);
        for (OVChipkaart kaart : reizigerKaarten) {
            ovcdao.delete(kaart);
        }


        rdao.delete(nieuweReiziger);
        System.out.println("[Test] Reiziger en gekoppelde OVChipkaarten verwijderd.");
    }
}