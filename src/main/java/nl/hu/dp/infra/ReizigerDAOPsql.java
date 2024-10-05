package nl.hu.dp.infra;

import nl.hu.dp.domein.interfaces.AdresDAO;
import nl.hu.dp.domein.interfaces.ReizigerDAO;
import nl.hu.dp.domein.Reiziger;
import nl.hu.dp.domein.Adres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adao;

    public ReizigerDAOPsql(Connection conn, AdresDAO adao) {
        this.conn = conn;
        this.adao = adao;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {Reiziger bestaandeReiziger = findById(reiziger.getId());
        if (bestaandeReiziger != null) {
            System.out.println("Reiziger met ID " + reiziger.getId() + " bestaat al.");
            return false;
        }
        String query = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reiziger.getId());
            ps.setString(2, reiziger.getVoorletters());
            ps.setString(3, reiziger.getTussenvoegsel());
            ps.setString(4, reiziger.getAchternaam());
            ps.setDate(5, reiziger.getGeboortedatum());
            ps.executeUpdate();
        }

        if (reiziger.getAdres() != null) {
            adao.save(reiziger.getAdres());
        }

        return true;
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
            ps.executeUpdate();
        }

        if (reiziger.getAdres() != null) {
            adao.update(reiziger.getAdres());
        }

        return true;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        if (reiziger.getAdres() != null) {
            adao.delete(reiziger.getAdres());
        }

        String query = "DELETE FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reiziger.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String query = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Reiziger reiziger = new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"), rs.getString("achternaam"),
                        rs.getDate("geboortedatum"));
                Adres adres = adao.findByReiziger(reiziger);
                reiziger.setAdres(adres);
                return reiziger;
            }
        }
        return null;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        String query = "SELECT * FROM reiziger";
        List<Reiziger> reizigers = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Reiziger reiziger = new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"), rs.getString("achternaam"),
                        rs.getDate("geboortedatum"));
                Adres adres = adao.findByReiziger(reiziger);
                reiziger.setAdres(adres);
                reizigers.add(reiziger);
            }
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) throws SQLException {
        String query = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        List<Reiziger> reizigers = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reiziger r = new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"), rs.getString("achternaam"),
                        rs.getDate("geboortedatum"));
                reizigers.add(r);
            }
        }
        return reizigers;
    }
}