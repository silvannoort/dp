package nl.hu.dp.dao;

import nl.hu.dp.model.Adres;
import nl.hu.dp.model.Reiziger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class AdresDAOPsql implements AdresDAO {

    private Connection conn;


    public AdresDAOPsql(Connection con) {
        this.conn = conn;

    }


    @Override
    public boolean save(Adres adres) throws SQLException {
        String query = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, adres.getId());
            ps.setString(2, adres.getPostcode());
            ps.setString(3, adres.getHuisnummer());
            ps.setString(4, adres.getStraat());
            ps.setString(5, adres.getWoonplaats());
            ps.setInt(6, adres.getReiziger().getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        String query = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, adres.getPostcode());
            ps.setString(2, adres.getHuisnummer());
            ps.setString(3, adres.getStraat());
            ps.setString(4, adres.getWoonplaats());
            ps.setInt(5, adres.getReiziger().getId());
            ps.setInt(6, adres.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        String query = "DELETE FROM adres WHERE adres_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, adres.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger r) throws SQLException {
        String query = "SELECT * FROM adres WHERE reiziger_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, r.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Adres(
                        rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        r
                );
            }
        }
        return null;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        List<Adres> adressen = new ArrayList<>();
        String query = "SELECT * FROM adres";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Adres adres = new Adres(
                        rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        null
                );
                adressen.add(adres);
            }
        }
        return adressen;
    }
}
