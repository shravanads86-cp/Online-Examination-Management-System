<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.schlexamsystem.model.*, com.schlexamsystem.dao.*, java.util.*" %>
<%
    Student student = (Student) session.getAttribute("student");
    if (student == null) {
        response.sendRedirect("studentLogin.jsp");
        return;
    }
    ExamDAO examDAO = new ExamDAO();
    List<Exam> exams = examDAO.getAllExams();
    List<Result> results = examDAO.getResultsByStudent(student.getStudentId());
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>Student Dashboard</h2>
        <a href="logout">Logout</a>
    </div>
    <div class="page-container">
        <div class="card welcome">
            <h3>Welcome, <%= student.getName() %>!</h3>
            <p>Email: <%= student.getEmail() %></p>
        </div>

        <div class="card">
            <h3>Available Exams</h3>
            <% if (exams.isEmpty()) { %>
                <p>No exams available at the moment.</p>
            <% } else { %>
            <table>
                <tr><th>Title</th><th>Subject</th><th>Duration</th><th>Action</th></tr>
                <% for (Exam exam : exams) { 
                    boolean taken = examDAO.hasStudentTakenExam(student.getStudentId(), exam.getExamId());
                %>
                <tr>
                    <td><%= exam.getTitle() %></td>
                    <td><%= exam.getSubject() %></td>
                    <td><%= exam.getDuration() %> mins</td>
                    <td>
                        <% if (taken) { %>
                            <span class="btn btn-disabled" style="pointer-events:none">Completed</span>
                        <% } else { %>
                            <a href="startExam.jsp?examId=<%= exam.getExamId() %>" class="btn">Start Exam</a>
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </table>
            <% } %>
        </div>

        <div class="card">
            <h3>My Results</h3>
            <% if (results.isEmpty()) { %>
                <p>You haven't taken any exams yet.</p>
            <% } else { %>
            <table>
                <tr><th>Exam</th><th>Score</th><th>Percentage</th><th>Date</th></tr>
                <% for (Result r : results) { 
                    int percent = (r.getScore() * 100) / r.getTotalQuestions();
                    String scoreClass = percent >= 70 ? "score-good" : (percent >= 40 ? "score-avg" : "score-poor");
                %>
                <tr>
                    <td><%= r.getExamTitle() %></td>
                    <td><%= r.getScore() %>/<%= r.getTotalQuestions() %></td>
                    <td class="<%= scoreClass %>"><%= percent %>%</td>
                    <td><%= r.getExamDate() %></td>
                </tr>
                <% } %>
            </table>
            <% } %>
        </div>
    </div>
</body>
</html>
