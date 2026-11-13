<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Registration</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="auth-page">
    <div class="auth-box">
        <h2>User Registration</h2>
        <% 
        String error = request.getParameter("error");
        if (error != null) {
            if ("exists".equals(error)) { %>
                <div class="msg-error">User already exists</div>
        <% } else if ("empty".equals(error)) { %>
                <div class="msg-error">Email and password are required</div>
        <% } else if ("failed".equals(error)) { %>
                <div class="error">Registration failed. Please try again.</div>
        <% }
        } %>
        <form action="register" method="post">
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" required placeholder="Enter your email">
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required placeholder="Enter password">
            </div>
            <button type="submit" class="btn-primary">Register</button>
        </form>
        <div class="auth-links">
            <a href="login.jsp">Already have an account? Login</a>
        </div>
    </div>
</body>
</html>
