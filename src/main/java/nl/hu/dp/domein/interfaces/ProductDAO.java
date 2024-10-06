package nl.hu.dp.domein.interfaces;

import nl.hu.dp.domein.OVChipkaart;
import nl.hu.dp.domein.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    boolean save(Product product) throws SQLException;

    boolean update(Product product) throws SQLException;

    boolean delete(Product product) throws SQLException;

    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException;

    List<Product> findAll() throws SQLException;
    Product findById(int id) throws SQLException;
}
