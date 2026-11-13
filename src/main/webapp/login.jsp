<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="auth-page">
    <div class="auth-box">
        <h2>User Login</h2>
        <% 
        String error = request.getParameter("error");
        String msg = request.getParameter("msg");
        String emailParam = request.getParameter("email");
        
        if (msg != null && "success".equals(msg)) { %>
            <div class="msg-success">Registration successful</div>
        <% } else if (msg != null && "resetsuccess".equals(msg)) { %>
            <div class="msg-success">Password reset successful</div>
        <% }
        
        if (error != null) {
            if ("notregistered".equals(error)) { %>
                <div class="msg-error">User not registered</div>
        <% } else if ("wrongpassword".equals(error)) { %>
                <div class="msg-error">Wrong password</div>
                <div class="forgot-link"><a href="forgot.jsp<%= (emailParam != null && !emailParam.isEmpty()) ? "?email=" + java.net.URLEncoder.encode(emailParam, "UTF-8") : "" %>">Forgot Password?</a></div>
        <% } else if ("empty".equals(error)) { %>
                <div class="msg-error">Email and password are required</div>
        <% }
        } %>
        <form action="login" method="post">
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" required placeholder="Enter your email" 
                    value="<%= (emailParam != null && "wrongpassword".equals(error)) ? request.getParameter("email") : "" %>">
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required placeholder="Enter password">
            </div>
            <button type="submit" class="btn-primary">Login</button>
        </form>
        <div class="auth-links">
            <a href="forgot.jsp">Forgot Password?</a>
            <a href="register.jsp">Don't have an account? Register</a>
        </div>
    </div>
</body>
</html>
