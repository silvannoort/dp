package nl.hu.dp.dao.implementaties;

import nl.hu.dp.dao.interfaces.OVChipkaartDAO;
import nl.hu.dp.model.ov.OVChipkaart;
import nl.hu.dp.model.reiziger.Reiziger;

import javax.persistence.EntityManager;
import java.util.List;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {
    private EntityManager em;

    public OVChipkaartDAOHibernate(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        try {
            em.getTransaction().begin();
            em.persist(ovChipkaart);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        try {
            em.getTransaction().begin();
            em.merge(ovChipkaart);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            em.getTransaction().begin();
            em.remove(em.contains(ovChipkaart) ? ovChipkaart : em.merge(ovChipkaart));
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public OVChipkaart findById(int id) {
        return em.find(OVChipkaart.class, id);
    }

    @Override
    public List<OVChipkaart> findAll() {
        return em.createQuery("FROM OVChipkaart", OVChipkaart.class).getResultList();
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        return em.createQuery("FROM OVChipkaart WHERE reiziger = :reiziger", OVChipkaart.class)
                .setParameter("reiziger", reiziger)
                .getResultList();
    }
}
