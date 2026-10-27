package dao;

import config.DBConnection;
import model.SaleItem;
import java.sql.*;
import java.util.List;

public class SalesDAO {
    
    public int saveSale(int customerId, int userId, double total,
                        double discount, double net, String payment,
                        List<SaleItem> items) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            String saleSql = "INSERT INTO sales (customer_id, user_id, total_amount, discount, net_amount, payment_method) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(saleSql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, customerId);
            ps.setInt(2, userId);
            ps.setDouble(3, total);
            ps.setDouble(4, discount);
            ps.setDouble(5, net);
            ps.setString(6, payment);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            int saleId = -1;
            if (rs.next()) saleId = rs.getInt(1);
            
            String itemSql = "INSERT INTO sale_items (sale_id, product_id, quantity, unit_price, subtotal) VALUES (?,?,?,?,?)";
            String stockSql = "UPDATE product SET stock_qty = stock_qty - ? WHERE product_id=?";
            
            for (SaleItem item : items) {
                PreparedStatement ps2 = conn.prepareStatement(itemSql);
                ps2.setInt(1, saleId);
                ps2.setInt(2, item.getProductId());
                ps2.setInt(3, item.getQuantity());
                ps2.setDouble(4, item.getUnitPrice());
                ps2.setDouble(5, item.getSubtotal());
                ps2.executeUpdate();
                
                PreparedStatement ps3 = conn.prepareStatement(stockSql);
                ps3.setInt(1, item.getQuantity());
                ps3.setInt(2, item.getProductId());
                ps3.executeUpdate();
            }
            
            String loyaltySql = "UPDATE customer SET loyalty_points = loyalty_points + ? WHERE customer_id=?";
            PreparedStatement ps4 = conn.prepareStatement(loyaltySql);
            ps4.setInt(1, (int)(net / 100));
            ps4.setInt(2, customerId);
            ps4.executeUpdate();
            
            conn.commit();
            return saleId;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return -1;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }
}