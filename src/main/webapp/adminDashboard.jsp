<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.schlexamsystem.model.*, com.schlexamsystem.dao.*, java.util.*" %>
<%
    Admin admin = (Admin) session.getAttribute("admin");
    if (admin == null) {
        response.sendRedirect("adminLogin.jsp");
        return;
    }
    StudentDAO studentDAO = new StudentDAO();
    TeacherDAO teacherDAO = new TeacherDAO();
    List<Student> pendingStudents = studentDAO.getAllPending();
    List<Teacher> pendingTeachers = teacherDAO.getAllPending();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>Admin Dashboard</h2>
        <a href="logout">Logout</a>
    </div>
    <div class="page-container">
        <div class="card welcome">
            <h3>Welcome, <%= admin.getUsername() %>!</h3>
        </div>

        <div class="card">
            <h3>Pending Students <span class="badge"><%= pendingStudents.size() %></span></h3>
            <% if (pendingStudents.isEmpty()) { %>
                <p>No pending student registrations.</p>
            <% } else { %>
            <table>
                <tr><th>Name</th><th>Email</th><th>Actions</th></tr>
                <% for (Student s : pendingStudents) { %>
                <tr>
                    <td><%= s.getName() %></td>
                    <td><%= s.getEmail() %></td>
                    <td>
                        <a href="adminAction?action=approve&type=student&id=<%= s.getStudentId() %>" class="btn btn-approve">Approve</a>
                        <a href="adminAction?action=reject&type=student&id=<%= s.getStudentId() %>" class="btn btn-reject">Reject</a>
                    </td>
                </tr>
                <% } %>
            </table>
            <% } %>
        </div>

        <div class="card">
            <h3>Pending Teachers <span class="badge"><%= pendingTeachers.size() %></span></h3>
            <% if (pendingTeachers.isEmpty()) { %>
                <p>No pending teacher registrations.</p>
            <% } else { %>
            <table>
                <tr><th>Name</th><th>Email</th><th>Subject</th><th>Actions</th></tr>
                <% for (Teacher t : pendingTeachers) { %>
                <tr>
                    <td><%= t.getName() %></td>
                    <td><%= t.getEmail() %></td>
                    <td><%= t.getSubject() %></td>
                    <td>
                        <a href="adminAction?action=approve&type=teacher&id=<%= t.getTeacherId() %>" class="btn btn-approve">Approve</a>
                        <a href="adminAction?action=reject&type=teacher&id=<%= t.getTeacherId() %>" class="btn btn-reject">Reject</a>
                    </td>
                </tr>
                <% } %>
            </table>
            <% } %>
        </div>
    </div>
</body>
</html>
