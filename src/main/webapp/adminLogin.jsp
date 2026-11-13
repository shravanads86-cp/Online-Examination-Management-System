<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="auth-page">
    <div class="auth-box">
        <h2>Admin Login</h2>
        <% if(request.getParameter("error") != null) { %>
            <div class="msg-error">Invalid username or password</div>
        <% } %>
        <form action="adminLogin" method="post">
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required>
            </div>
            <button type="submit" class="btn-primary">Login</button>
        </form>
        <div class="auth-links">
            <a href="index.jsp">Back to Home</a>
        </div>
    </div>
</body>
</html>
