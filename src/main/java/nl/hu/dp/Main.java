package nl.hu.dp;

import nl.hu.dp.domein.OVChipkaart;
import nl.hu.dp.domein.Product;
import nl.hu.dp.domein.Reiziger;
import nl.hu.dp.domein.interfaces.OVChipkaartDAO;
import nl.hu.dp.domein.interfaces.ProductDAO;
import nl.hu.dp.infra.OVChipkaartDAOHibernate;
import nl.hu.dp.infra.ProductDAOHibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("reizigerUnit");
        EntityManager em = emf.createEntityManager();

        OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOHibernate(em);
        ProductDAO productDAO = new ProductDAOHibernate(em);


        em.getTransaction().begin();
        try {
            testProductDAO(productDAO, ovChipkaartDAO);


            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

    private static void testProductDAO(ProductDAO productDAO, OVChipkaartDAO ovChipkaartDAO) throws SQLException {
        System.out.println("\n---------- Test ProductDAO -------------");


        Reiziger reiziger = new Reiziger(1, "J", "van", "Doe", Date.valueOf("1990-01-01"));
        OVChipkaart ovChipkaart = new OVChipkaart(12345, Date.valueOf("2027-12-31"), 1, 50.00, reiziger);


        Product product = new Product(1, "Product A", "Beschrijving A", 9.99);
        ovChipkaart.voegProductToe(product);


        ovChipkaartDAO.save(ovChipkaart);
        productDAO.save(product);


        List<Product> productenByOVChipkaart = productDAO.findByOVChipkaart(ovChipkaart);
        System.out.println("[Test] Producten gevonden bij OVChipkaart:");
        for (Product p : productenByOVChipkaart) {
            System.out.println(p);
        }


        product.setNaam("Product A - Updated");
        productDAO.update(product);
        System.out.println("[Test] Product geüpdatet: " + product);


        productDAO.delete(product);
        ovChipkaartDAO.delete(ovChipkaart);
        System.out.println("[Test] Product en gekoppelde OVChipkaart verwijderd.");
    }
}