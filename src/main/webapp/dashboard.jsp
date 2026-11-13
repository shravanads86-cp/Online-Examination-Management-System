<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    com.schlexamsystem.model.User user = (com.schlexamsystem.model.User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>Dashboard</h2>
        <a href="logoutAuth">Logout</a>
    </div>
    <div class="page-container">
        <div class="card welcome">
            <h3>Welcome, <%= user.getEmail() %>!</h3>
            <p>You have successfully logged in.</p>
        </div>
    </div>
</body>
</html>
