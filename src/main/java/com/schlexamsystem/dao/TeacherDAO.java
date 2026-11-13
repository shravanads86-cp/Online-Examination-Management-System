package com.schlexamsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.schlexamsystem.model.Teacher;
import com.schlexamsystem.util.DBConnection;

public class TeacherDAO {

    public Teacher login(String email, String password) {
        Teacher teacher = null;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM teacher WHERE email=? AND password=? AND status='APPROVED'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                teacher = new Teacher();
                teacher.setTeacherId(rs.getInt("teacher_id"));
                teacher.setName(rs.getString("name"));
                teacher.setEmail(rs.getString("email"));
                teacher.setSubject(rs.getString("subject"));
                teacher.setStatus(rs.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teacher;
    }

    public boolean register(Teacher teacher) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO teacher (name, email, password, subject, status) VALUES (?, ?, ?, ?, 'PENDING')";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, teacher.getName());
            ps.setString(2, teacher.getEmail());
            ps.setString(3, teacher.getPassword());
            ps.setString(4, teacher.getSubject());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Teacher> getAllPending() {
        List<Teacher> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM teacher WHERE status='PENDING'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Teacher t = new Teacher();
                t.setTeacherId(rs.getInt("teacher_id"));
                t.setName(rs.getString("name"));
                t.setEmail(rs.getString("email"));
                t.setSubject(rs.getString("subject"));
                t.setStatus(rs.getString("status"));
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean approve(int teacherId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE teacher SET status='APPROVED' WHERE teacher_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, teacherId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean reject(int teacherId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "DELETE FROM teacher WHERE teacher_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, teacherId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
