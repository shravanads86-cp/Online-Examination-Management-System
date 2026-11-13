<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Registration</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="auth-page">
    <div class="auth-box">
        <h2>Student Registration</h2>
        <% if(request.getParameter("error") != null) { %>
            <div class="msg-error">Registration failed. Email may already exist.</div>
        <% } %>
        <form action="studentRegister" method="post">
            <div class="form-group">
                <label>Full Name</label>
                <input type="text" name="name" required>
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required>
            </div>
            <button type="submit" class="btn-primary">Register</button>
        </form>
        <div class="auth-links">
            <a href="studentLogin.jsp">Already have an account? Login</a>
            <a href="index.jsp">Back to Home</a>
        </div>
    </div>
</body>
</html>
