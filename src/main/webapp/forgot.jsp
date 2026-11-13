<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forgot Password</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="auth-page">
    <div class="auth-box">
        <h2>Forgot Password</h2>
        <p style="color:#666; margin-bottom:20px; font-size:14px;">Enter your registered email to reset password.</p>
        <% 
        String error = request.getParameter("error");
        String emailParam = request.getParameter("email");
        if (error != null) {
            if ("notregistered".equals(error)) { %>
                <div class="msg-error">User not registered</div>
        <% } else if ("empty".equals(error)) { %>
                <div class="msg-error">Email is required</div>
        <% } else if ("session".equals(error)) { %>
                <div class="msg-error">Session expired. Please try again.</div>
        <% }
        } %>
        <form action="forgot" method="post">
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" required placeholder="Enter your registered email" 
                    value="<%= (emailParam != null) ? request.getParameter("email") : "" %>">
            </div>
            <button type="submit" class="btn-primary">Continue</button>
        </form>
        <div class="auth-links">
            <a href="login.jsp">Back to Login</a>
        </div>
    </div>
</body>
</html>
