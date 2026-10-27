package dao;

import config.DBConnection;
import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.category_name FROM product p " +
                     "LEFT JOIN category c ON p.category_id = c.category_id";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setCategoryName(rs.getString("category_name"));
                p.setSize(rs.getString("size"));
                p.setColor(rs.getString("color"));
                p.setPrice(rs.getDouble("price"));
                p.setStockQty(rs.getInt("stock_qty"));
                list.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public boolean addProduct(Product p) {
        String sql = "INSERT INTO product (product_name, category_id, size, color, price, stock_qty) VALUES (?,?,?,?,?,?)";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getProductName());
            ps.setInt(2, p.getCategoryId());
            ps.setString(3, p.getSize());
            ps.setString(4, p.getColor());
            ps.setDouble(5, p.getPrice());
            ps.setInt(6, p.getStockQty());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public boolean updateProduct(Product p) {
        String sql = "UPDATE product SET product_name=?, category_id=?, size=?, color=?, price=?, stock_qty=? WHERE product_id=?";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getProductName());
            ps.setInt(2, p.getCategoryId());
            ps.setString(3, p.getSize());
            ps.setString(4, p.getColor());
            ps.setDouble(5, p.getPrice());
            ps.setInt(6, p.getStockQty());
            ps.setInt(7, p.getProductId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public boolean deleteProduct(int id) {
    try {

        Connection con =
                DBConnection.getInstance().getConnection();

        String sql1 =
                "DELETE FROM sale_items WHERE product_id=?";

        PreparedStatement ps1 =
                con.prepareStatement(sql1);

        ps1.setInt(1, id);
        ps1.executeUpdate();

        String sql2 =
                "DELETE FROM product WHERE product_id=?";

        PreparedStatement ps2 =
                con.prepareStatement(sql2);

        ps2.setInt(1, id);

        return ps2.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}
}
