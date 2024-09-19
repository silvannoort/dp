package nl.hu.dp.dao.implementaties;

import nl.hu.dp.dao.interfaces.AdresDAO;
import nl.hu.dp.model.adres.Adres;
import nl.hu.dp.model.reiziger.Reiziger;

import javax.persistence.EntityManager;
import java.util.List;

public class AdresDAOHibernate implements AdresDAO {

    private EntityManager em;

    public AdresDAOHibernate(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean save(Adres adres) {
        em.getTransaction().begin();
        em.persist(adres);
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean update(Adres adres) {
        em.getTransaction().begin();
        em.merge(adres);
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean delete(Adres adres) {
        em.getTransaction().begin();
        em.remove(em.contains(adres) ? adres : em.merge(adres));
        em.getTransaction().commit();
        return true;
    }

    @Override
    public Adres findById(int id) {
        return em.find(Adres.class, id);
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        return em.createQuery("SELECT a FROM Adres a WHERE a.reiziger = :reiziger", Adres.class)
                .setParameter("reiziger", reiziger)
                .getSingleResult();
    }

    @Override
    public List<Adres> findAll() {
        return em.createQuery("SELECT a FROM Adres a", Adres.class).getResultList();
    }
}
