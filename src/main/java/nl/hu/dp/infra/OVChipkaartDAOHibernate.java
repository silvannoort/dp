package nl.hu.dp.infra;

import nl.hu.dp.domein.OVChipkaart;
import nl.hu.dp.domein.Reiziger;
import nl.hu.dp.domein.interfaces.OVChipkaartDAO;

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
            em.persist(ovChipkaart);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        try {
            em.merge(ovChipkaart);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            em.remove(em.contains(ovChipkaart) ? ovChipkaart : em.merge(ovChipkaart));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        return em.createQuery("FROM OVChipkaart WHERE reiziger = :reiziger", OVChipkaart.class)
                .setParameter("reiziger", reiziger)
                .getResultList();
    }

    @Override
    public List<OVChipkaart> findAll() {
        return em.createQuery("FROM OVChipkaart", OVChipkaart.class).getResultList();
    }

    @Override
    public OVChipkaart findById(int id) {
        return em.find(OVChipkaart.class, id);
    }
}
