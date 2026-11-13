package com.schlexamsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.schlexamsystem.model.Admin;
import com.schlexamsystem.util.DBConnection;

public class AdminDAO {

    public Admin login(String username, String password) {
        Admin admin = null;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM admin WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }
}
