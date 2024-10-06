package nl.hu.dp.infra;

import nl.hu.dp.domein.OVChipkaart;
import nl.hu.dp.domein.Product;
import nl.hu.dp.domein.interfaces.ProductDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection conn;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Product product) throws SQLException {

        Product existingProduct = findById(product.getProductNummer());
        if (existingProduct != null) {
            System.out.println("Product met nummer " + product.getProductNummer() + " bestaat al.");
            return false;
        }


        String query = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, product.getProductNummer());
            ps.setString(2, product.getNaam());
            ps.setString(3, product.getBeschrijving());
            ps.setDouble(4, product.getPrijs());
            ps.executeUpdate();


            saveProductOvChipkaarten(product);
        }
        return true;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        String query = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, product.getNaam());
            ps.setString(2, product.getBeschrijving());
            ps.setDouble(3, product.getPrijs());
            ps.setInt(4, product.getProductNummer());
            ps.executeUpdate();


            deleteProductOvChipkaarten(product);


            saveProductOvChipkaarten(product);
        }
        return true;
    }

    @Override
    public boolean delete(Product product) throws SQLException {

        deleteProductOvChipkaarten(product);


        String query = "DELETE FROM product WHERE product_nummer = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, product.getProductNummer());
            ps.executeUpdate();
        }
        return true;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        String query = "SELECT p.* FROM product p " +
                "JOIN ov_chipkaart_product ocp ON p.product_nummer = ocp.product_nummer " +
                "WHERE ocp.kaart_nummer = ?";
        List<Product> producten = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ovChipkaart.getKaartNummer());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(rs.getInt("product_nummer"),
                            rs.getString("naam"),
                            rs.getString("beschrijving"),
                            rs.getDouble("prijs"));
                    producten.add(product);
                }
            }
        }
        return producten;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        String query = "SELECT * FROM product";
        List<Product> producten = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product(rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs"));
                producten.add(product);
            }
        }
        return producten;
    }

    @Override
    public Product findById(int id) throws SQLException {
        String query = "SELECT * FROM product WHERE product_nummer = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs")
                );
            }
        }

        return null;
    }

    private void saveProductOvChipkaarten(Product product) throws SQLException {
        String query = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
        for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, ovChipkaart.getKaartNummer());
                ps.setInt(2, product.getProductNummer());
                ps.executeUpdate();
            }
        }
    }

    private void deleteProductOvChipkaarten(Product product) throws SQLException {
        String query = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, product.getProductNummer());
            ps.executeUpdate();
        }
    }
}
