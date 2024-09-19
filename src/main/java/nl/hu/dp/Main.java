package nl.hu.dp;

import nl.hu.dp.dao.implementaties.AdresDAOHibernate;
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
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("reizigerUnit");
        EntityManager em = emf.createEntityManager();

        AdresDAO adao = new AdresDAOHibernate(em);
        ReizigerDAO rdao = new ReizigerDAOHibernate(em);

        testAdresDAO(adao, rdao);

        em.close();
        emf.close();
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
}
