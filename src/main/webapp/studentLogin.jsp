<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="auth-page">
    <div class="auth-box">
        <h2>Student Login</h2>
        <% if(request.getParameter("error") != null) { %>
            <div class="msg-error">Invalid email or password, or account not approved</div>
        <% } %>
        <% if(request.getParameter("msg") != null && request.getParameter("msg").equals("registered")) { %>
            <div class="msg-success">Registration successful! Wait for admin approval.</div>
        <% } %>
        <form action="studentLogin" method="post">
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required>
            </div>
            <button type="submit" class="btn-primary">Login</button>
        </form>
        <div class="auth-links">
            <a href="studentRegister.jsp">Don't have an account? Register</a>
            <a href="index.jsp">Back to Home</a>
        </div>
    </div>
</body>
</html>
