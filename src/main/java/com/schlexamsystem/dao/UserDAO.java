package com.schlexamsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.schlexamsystem.model.User;
import com.schlexamsystem.util.DBConnection;

public class UserDAO {

    public User findByEmail(String email) {
        User user = null;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean register(User user) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(String email, String newPassword) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE users SET password = ? WHERE email = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setString(2, email);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
