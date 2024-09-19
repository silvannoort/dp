package nl.hu.dp;

import nl.hu.dp.dao.implementaties.ReizigerDAOHibernate;
import nl.hu.dp.dao.interfaces.AdresDAO;
import nl.hu.dp.dao.implementaties.AdresDAOPsql;
import nl.hu.dp.dao.interfaces.ReizigerDAO;
import nl.hu.dp.dao.implementaties.ReizigerDAOPsql;
import nl.hu.dp.model.adres.Adres;
import nl.hu.dp.model.reiziger.Reiziger;

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


        AdresDAO adao = new AdresDAOPsql(conn);
        ReizigerDAO rdao = new ReizigerDAOPsql(conn, adao);


        testReizigerDAO(reizigerDAOHibernate);
        testAdresDAO(adao, rdao);


        em.close();
        emf.close();
        conn.close();
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");


        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }


        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(90, "S", "", "Boers", Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers");

        System.out.println("[Test] ReizigerDAO.update() test:");
        sietske.setAchternaam("UpdatedBoers");
        rdao.update(sietske);
        Reiziger updatedReiziger = rdao.findById(90);
        System.out.println("[Test] Geüpdatete reiziger: " + updatedReiziger);

        System.out.println("[Test] ReizigerDAO.delete() test:");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println("[Test] Aantal reizigers na ReizigerDAO.delete(): " + reizigers.size());

        List<Reiziger> foundReizigers = rdao.findByGbdatum(Date.valueOf(gbdatum));
        for (Reiziger r : foundReizigers) {
            System.out.println(r);
        }
    }

    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a.getReiziger() + ", " + a);
        }


        String gbdatum = "1990-01-01";
        Reiziger reiziger = new Reiziger(100, "J", "", "Doe", Date.valueOf(gbdatum));
        rdao.save(reiziger);

        Adres adres = new Adres(100, "1234AB", "56", "Straatnaam", "Utrecht", reiziger);
        System.out.print("[Test] Aantal adressen voor Reiziger 100: " + adao.findAll().size());
        adao.save(adres);
        System.out.println(", na AdresDAO.save() " + adao.findAll().size());


        adres.setWoonplaats("Amsterdam");
        adao.update(adres);
        Adres updatedAdres = adao.findByReiziger(reiziger);
        System.out.println("[Test] Geüpdatete woonplaats: " + updatedAdres.getWoonplaats());


        adao.delete(adres);
        System.out.println("[Test] Aantal adressen na AdresDAO.delete(): " + adao.findAll().size());


        rdao.delete(reiziger);
    }

}
