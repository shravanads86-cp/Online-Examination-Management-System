<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("resetEmail") == null) {
        response.sendRedirect("forgot.jsp?error=session");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reset Password</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="auth-page">
    <div class="auth-box">
        <h2>Reset Password</h2>
        <% 
        String error = request.getParameter("error");
        if (error != null) {
            if ("mismatch".equals(error)) { %>
                <div class="msg-error">Passwords do not match</div>
        <% } else if ("empty".equals(error)) { %>
                <div class="msg-error">All fields are required</div>
        <% } else if ("failed".equals(error)) { %>
                <div class="msg-error">Password reset failed. Please try again.</div>
        <% }
        } %>
        <form action="resetPassword" method="post">
            <div class="form-group">
                <label>New Password</label>
                <input type="password" name="newPassword" required placeholder="Enter new password">
            </div>
            <div class="form-group">
                <label>Confirm Password</label>
                <input type="password" name="confirmPassword" required placeholder="Confirm new password">
            </div>
            <button type="submit" class="btn-primary">Reset Password</button>
        </form>
        <div class="auth-links">
            <a href="forgot.jsp">Back</a>
        </div>
    </div>
</body>
</html>
