package nl.hu.dp.dao;

import nl.hu.dp.model.Adres;
import nl.hu.dp.model.Reiziger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {

    private Connection conn;

    public AdresDAOPsql(Connection con) {
        this.conn = conn;
    }


    @Override
    public boolean save(Adres adres) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        return false;
    }

    @Override
    public Adres findByReiziger(Reiziger r) throws SQLException {
        return null;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        return List.of();
    }

}
