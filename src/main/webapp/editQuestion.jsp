<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.schlexamsystem.model.*, com.schlexamsystem.dao.*" %>
<%
    Teacher teacher = (Teacher) session.getAttribute("teacher");
    if (teacher == null) {
        response.sendRedirect("teacherLogin.jsp");
        return;
    }
    int questionId = Integer.parseInt(request.getParameter("questionId"));
    int examId = Integer.parseInt(request.getParameter("examId"));
    ExamDAO examDAO = new ExamDAO();
    Question q = examDAO.getQuestionById(questionId);
    Exam exam = examDAO.getExamById(examId);
    if (q == null) {
        response.sendRedirect("addQuestions.jsp?examId=" + examId);
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Question</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>Edit Question</h2>
        <a href="addQuestions.jsp?examId=<%= examId %>">Back to Questions</a>
    </div>
    <div class="page-container">
        <div class="card">
            <h3>Exam: <%= exam.getTitle() %></h3>
            <div class="highlight">Change the correct answer or edit any part of the question below.</div>
            <form action="editQuestion" method="post">
                <input type="hidden" name="questionId" value="<%= q.getQuestionId() %>">
                <input type="hidden" name="examId" value="<%= examId %>">
                <div class="form-group">
                    <label>Question</label>
                    <textarea name="questionText" required><%= q.getQuestionText() %></textarea>
                </div>
                <div class="options-grid">
                    <div class="form-group">
                        <label>Option A</label>
                        <input type="text" name="optionA" value="<%= q.getOptionA() %>" required>
                    </div>
                    <div class="form-group">
                        <label>Option B</label>
                        <input type="text" name="optionB" value="<%= q.getOptionB() %>" required>
                    </div>
                    <div class="form-group">
                        <label>Option C</label>
                        <input type="text" name="optionC" value="<%= q.getOptionC() %>" required>
                    </div>
                    <div class="form-group">
                        <label>Option D</label>
                        <input type="text" name="optionD" value="<%= q.getOptionD() %>" required>
                    </div>
                </div>
                <div class="form-group">
                    <label>Correct Answer (select the right option)</label>
                    <select name="correctAnswer" required>
                        <option value="A" <%= "A".equals(q.getCorrectAnswer()) ? "selected" : "" %>>A</option>
                        <option value="B" <%= "B".equals(q.getCorrectAnswer()) ? "selected" : "" %>>B</option>
                        <option value="C" <%= "C".equals(q.getCorrectAnswer()) ? "selected" : "" %>>C</option>
                        <option value="D" <%= "D".equals(q.getCorrectAnswer()) ? "selected" : "" %>>D</option>
                    </select>
                </div>
                <div class="actions">
                    <button type="submit" class="btn">Save Changes</button>
                    <a href="addQuestions.jsp?examId=<%= examId %>" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
