package nl.hu.dp.infra;

import nl.hu.dp.domein.OVChipkaart;
import nl.hu.dp.domein.Product;
import nl.hu.dp.domein.interfaces.ProductDAO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ProductDAOHibernate implements ProductDAO {
    private EntityManager em;

    public ProductDAOHibernate(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean save(Product product) {
        try {
            em.persist(product);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        try {
            em.merge(product);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        try {
            em.remove(em.contains(product) ? product : em.merge(product));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p JOIN p.ovChipkaarten ovc WHERE ovc = :ovChipkaart", Product.class);
        query.setParameter("ovChipkaart", ovChipkaart);
        return query.getResultList();
    }

    @Override
    public List<Product> findAll() {
        return em.createQuery("FROM Product", Product.class).getResultList();
    }

    @Override
    public Product findById(int id) {
        return em.find(Product.class, id);
    }
}
