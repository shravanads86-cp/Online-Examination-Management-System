package com.schlexamsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.schlexamsystem.model.Student;
import com.schlexamsystem.util.DBConnection;

public class StudentDAO {

    public Student login(String email, String password) {
        Student student = null;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM student WHERE email=? AND password=? AND status='APPROVED'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setStatus(rs.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return student;
    }

    public boolean register(Student student) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO student (name, email, password, status) VALUES (?, ?, ?, 'PENDING')";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPassword());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Student> getAllPending() {
        List<Student> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM student WHERE status='PENDING'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Student s = new Student();
                s.setStudentId(rs.getInt("student_id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setStatus(rs.getString("status"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean approve(int studentId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE student SET status='APPROVED' WHERE student_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean reject(int studentId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "DELETE FROM student WHERE student_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
