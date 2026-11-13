<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.schlexamsystem.model.*, com.schlexamsystem.dao.*, java.util.*" %>
<%
    Teacher teacher = (Teacher) session.getAttribute("teacher");
    if (teacher == null) {
        response.sendRedirect("teacherLogin.jsp");
        return;
    }
    ExamDAO examDAO = new ExamDAO();
    List<Exam> exams = examDAO.getExamsByTeacher(teacher.getTeacherId());
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Teacher Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>Teacher Dashboard</h2>
        <a href="logout">Logout</a>
    </div>
    <div class="page-container">
        <div class="card welcome">
            <h3>Welcome, <%= teacher.getName() %>!</h3>
            <p>Subject: <%= teacher.getSubject() %> | Email: <%= teacher.getEmail() %></p>
        </div>

        <div class="card">
            <h3>Create New Exam</h3>
            <form action="createExam" method="post">
                <div class="form-row">
                    <div class="form-group">
                        <label>Exam Title</label>
                        <input type="text" name="title" required placeholder="e.g. Midterm Exam">
                    </div>
                    <div class="form-group">
                        <label>Subject</label>
                        <input type="text" name="subject" value="<%= teacher.getSubject() %>" required>
                    </div>
                    <div class="form-group">
                        <label>Duration (minutes)</label>
                        <input type="number" name="duration" value="30" min="5" required>
                    </div>
                </div>
                <button type="submit" class="btn">Create Exam & Add Questions</button>
            </form>
        </div>

        <div class="card">
            <h3>My Exams</h3>
            <% if (exams.isEmpty()) { %>
                <p>You haven't created any exams yet.</p>
            <% } else { %>
            <table>
                <tr><th>Title</th><th>Subject</th><th>Duration</th><th>Action</th></tr>
                <% for (Exam exam : exams) { %>
                <tr>
                    <td><%= exam.getTitle() %></td>
                    <td><%= exam.getSubject() %></td>
                    <td><%= exam.getDuration() %> mins</td>
                    <td><a href="addQuestions.jsp?examId=<%= exam.getExamId() %>" class="btn">Add Questions</a></td>
                </tr>
                <% } %>
            </table>
            <% } %>
        </div>
    </div>
</body>
</html>
