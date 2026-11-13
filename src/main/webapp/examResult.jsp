<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    int score = Integer.parseInt(request.getParameter("score"));
    int total = Integer.parseInt(request.getParameter("total"));
    int percent = (score * 100) / total;
    String status = percent >= 40 ? "PASSED" : "FAILED";
    String statusColor = percent >= 40 ? "#4A8B5E" : "#8B5A5A";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Exam Result</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="result-page">
    <div class="result-card">
        <h2>Exam Completed!</h2>
        <div class="score-circle">
            <span><%= percent %>%</span>
        </div>
        <div class="status" style="color: <%= statusColor %>"><%= status %></div>
        <div class="details">
            You scored <strong><%= score %></strong> out of <strong><%= total %></strong> questions
        </div>
        <a href="studentDashboard.jsp" class="btn">Back to Dashboard</a>
    </div>
</body>
</html>
