package nl.hu.dp.infra;

import nl.hu.dp.domein.interfaces.AdresDAO;
import nl.hu.dp.domein.Adres;
import nl.hu.dp.domein.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private Connection conn;

    public AdresDAOPsql(Connection conn) {
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
        String query = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ? WHERE adres_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, adres.getPostcode());
            ps.setString(2, adres.getHuisnummer());
            ps.setString(3, adres.getStraat());
            ps.setString(4, adres.getWoonplaats());
            ps.setInt(5, adres.getId());
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
    public Adres findById(int id) throws SQLException {
        return null;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        String query = "SELECT * FROM adres WHERE reiziger_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reiziger.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Adres(rs.getInt("adres_id"), rs.getString("postcode"),
                        rs.getString("huisnummer"), rs.getString("straat"),
                        rs.getString("woonplaats"), reiziger);
            }
        }
        return null;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        String query = "SELECT a.*, r.reiziger_id, r.voorletters, r.tussenvoegsel, r.achternaam, r.geboortedatum " +
                "FROM adres a " +
                "JOIN reiziger r ON a.reiziger_id = r.reiziger_id";
        List<Adres> adressen = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Reiziger reiziger = new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"), rs.getString("achternaam"),
                        rs.getDate("geboortedatum"));


                Adres adres = new Adres(rs.getInt("adres_id"), rs.getString("postcode"),
                        rs.getString("huisnummer"), rs.getString("straat"),
                        rs.getString("woonplaats"), reiziger);
                adressen.add(adres);
            }
        }
        return adressen;
    }
}
