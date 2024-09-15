package nl.hu.dp.dao;

import nl.hu.dp.model.Adres;
import nl.hu.dp.model.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {
    boolean save(Adres adres) throws SQLException;

    boolean update(Adres adres) throws SQLException;

    boolean delete(Adres adres) throws SQLException;

    Adres findByReiziger(Reiziger r) throws SQLException;

    List<Adres> findAll() throws SQLException;
}





