package nl.hu.dp.dao;

import nl.hu.dp.model.Reiziger;
import nl.hu.dp.model.Adres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adao;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;

    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        String query = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reiziger.getId());
            ps.setString(2, reiziger.getVoorletters());
            ps.setString(3, reiziger.getTussenvoegsel());
            ps.setString(4, reiziger.getAchternaam());
            ps.setDate(5, reiziger.getGeboortedatum());

            if (ps.executeUpdate() > 0) {
                if (reiziger.getAdres() != null) {
                    adao.save(reiziger.getAdres());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        String query = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, reiziger.getVoorletters());
            ps.setString(2, reiziger.getTussenvoegsel());
            ps.setString(3, reiziger.getAchternaam());
            ps.setDate(4, reiziger.getGeboortedatum());
            ps.setInt(5, reiziger.getId());

            if (ps.executeUpdate() > 0) {
                if (reiziger.getAdres() != null) {
                    adao.update(reiziger.getAdres());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        String query = "DELETE FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reiziger.getId());

            if (ps.executeUpdate() > 0) {
                if (reiziger.getAdres() != null) {
                    adao.delete(reiziger.getAdres());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String query = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Reiziger reiziger = new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"), rs.getString("achternaam"), rs.getDate("geboortedatum"));
                Adres adres = adao.findByReiziger(reiziger);
                reiziger.setAdres(adres);
                return reiziger;
            }
        }
        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) throws SQLException {
        String query = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        List<Reiziger> reizigers = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reiziger reiziger = new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"), rs.getString("achternaam"), rs.getDate("geboortedatum"));
                Adres adres = adao.findByReiziger(reiziger);
                reiziger.setAdres(adres);
                reizigers.add(reiziger);
            }
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        String query = "SELECT * FROM reiziger";
        List<Reiziger> reizigers = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Reiziger reiziger = new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"), rs.getString("achternaam"), rs.getDate("geboortedatum"));
                Adres adres = adao.findByReiziger(reiziger);
                reiziger.setAdres(adres);
                reizigers.add(reiziger);
            }
        }
        return reizigers;
    }
}
