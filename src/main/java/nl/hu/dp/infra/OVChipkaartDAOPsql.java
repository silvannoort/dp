package nl.hu.dp.infra;

import nl.hu.dp.domein.interfaces.OVChipkaartDAO;
import nl.hu.dp.domein.OVChipkaart;
import nl.hu.dp.domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {

    private Connection conn;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        String query = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ovChipkaart.getKaartNummer());
            ps.setDate(2, ovChipkaart.getGeldigTot());
            ps.setInt(3, ovChipkaart.getKlasse());
            ps.setDouble(4, ovChipkaart.getSaldo());
            ps.setInt(5, ovChipkaart.getReiziger().getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        String query = "UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ? WHERE kaart_nummer = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, ovChipkaart.getGeldigTot());
            ps.setInt(2, ovChipkaart.getKlasse());
            ps.setDouble(3, ovChipkaart.getSaldo());
            ps.setInt(4, ovChipkaart.getKaartNummer());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        String query = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ovChipkaart.getKaartNummer());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public OVChipkaart findById(int id) throws SQLException {
        String query = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new OVChipkaart(
                        rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getInt("klasse"),
                        rs.getDouble("saldo"),
                        null
                );
            }
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        String query = "SELECT * FROM ov_chipkaart";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            List<OVChipkaart> kaarten = new ArrayList<>();
            while (rs.next()) {
                kaarten.add(new OVChipkaart(
                        rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getInt("klasse"),
                        rs.getDouble("saldo"),
                        null
                ));
            }
            return kaarten;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String query = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reiziger.getId());
            ResultSet rs = ps.executeQuery();
            List<OVChipkaart> kaarten = new ArrayList<>();
            while (rs.next()) {
                kaarten.add(new OVChipkaart(
                        rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getInt("klasse"),
                        rs.getDouble("saldo"),
                        reiziger
                ));
            }
            return kaarten;
        }
    }
}
