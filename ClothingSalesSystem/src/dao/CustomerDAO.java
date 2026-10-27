package dao;

import config.DBConnection;
import model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM customer");
            while (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setCustomerName(rs.getString("customer_name"));
                c.setPhone(rs.getString("phone"));
                c.setEmail(rs.getString("email"));
                c.setAddress(rs.getString("address"));
                c.setLoyaltyPoints(rs.getInt("loyalty_points"));
                list.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public boolean addCustomer(Customer c) {
        String sql = "INSERT INTO customer (customer_name, phone, email, address, loyalty_points) VALUES (?,?,?,?,?)";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getCustomerName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());
            ps.setInt(5, c.getLoyaltyPoints());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public boolean updateCustomer(Customer c) {
        String sql = "UPDATE customer SET customer_name=?, phone=?, email=?, address=?, loyalty_points=? WHERE customer_id=?";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getCustomerName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());
            ps.setInt(5, c.getLoyaltyPoints());
            ps.setInt(6, c.getCustomerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public boolean deleteCustomer(int id) {
        String sql = "DELETE FROM customer WHERE customer_id=?";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}