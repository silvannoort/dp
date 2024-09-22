package nl.hu.dp.dao.implementaties;

import nl.hu.dp.dao.interfaces.ReizigerDAO;
import nl.hu.dp.model.reiziger.Reiziger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {
    private EntityManager em;

    public ReizigerDAOHibernate(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        em.getTransaction().begin();

        Reiziger existingReiziger = em.find(Reiziger.class, reiziger.getId());
        if (existingReiziger != null) {
            em.merge(reiziger);
        } else {
            em.persist(reiziger);
        }
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        em.getTransaction().begin();
        em.merge(reiziger);
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        em.getTransaction().begin();
        em.remove(em.contains(reiziger) ? reiziger : em.merge(reiziger));
        em.getTransaction().commit();
        return true;
    }

    @Override
    public Reiziger findById(int id) {
        return em.find(Reiziger.class, id);
    }

    @Override
    public List<Reiziger> findByGbdatum(Date geboortedatum) {
        TypedQuery<Reiziger> query = em.createQuery("FROM Reiziger WHERE geboortedatum = :gbdatum", Reiziger.class);
        query.setParameter("gbdatum", geboortedatum);
        return query.getResultList();
    }

    @Override
    public List<Reiziger> findAll() {
        TypedQuery<Reiziger> query = em.createQuery("FROM Reiziger", Reiziger.class);
        return query.getResultList();
    }
}
