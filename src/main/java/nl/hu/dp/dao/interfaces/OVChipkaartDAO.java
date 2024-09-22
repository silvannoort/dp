package nl.hu.dp.dao.interfaces;

import nl.hu.dp.model.ov.OVChipkaart;
import nl.hu.dp.model.reiziger.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {

    boolean save(OVChipkaart ovChipkaart) throws SQLException;

    boolean update(OVChipkaart ovChipkaart) throws SQLException;

    boolean delete(OVChipkaart ovChipkaart) throws SQLException;

    OVChipkaart findById(int id) throws SQLException;

    List<OVChipkaart> findAll() throws SQLException;

    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
}
