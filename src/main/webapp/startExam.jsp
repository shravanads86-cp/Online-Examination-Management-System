<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.schlexamsystem.model.*, com.schlexamsystem.dao.*, java.util.*" %>
<%
    Student student = (Student) session.getAttribute("student");
    if (student == null) {
        response.sendRedirect("studentLogin.jsp");
        return;
    }
    int examId = Integer.parseInt(request.getParameter("examId"));
    ExamDAO examDAO = new ExamDAO();
    Exam exam = examDAO.getExamById(examId);
    List<Question> questions = examDAO.getQuestionsByExam(examId);
    
    if (examDAO.hasStudentTakenExam(student.getStudentId(), examId)) {
        response.sendRedirect("studentDashboard.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= exam.getTitle() %> - Exam</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="exam-header">
        <h2><%= exam.getTitle() %></h2>
        <p>Subject: <%= exam.getSubject() %> | Duration: <%= exam.getDuration() %> minutes | Questions: <%= questions.size() %></p>
    </div>
    <div class="page-container">
        <form action="takeExam" method="post">
            <input type="hidden" name="examId" value="<%= examId %>">
            <% int qNum = 1; for (Question q : questions) { %>
            <div class="question-card">
                <div class="question-num">Question <%= qNum++ %></div>
                <div class="question-text"><%= q.getQuestionText() %></div>
                <div class="options">
                    <label><input type="radio" name="q<%= q.getQuestionId() %>" value="A"><span>A. <%= q.getOptionA() %></span></label>
                    <label><input type="radio" name="q<%= q.getQuestionId() %>" value="B"><span>B. <%= q.getOptionB() %></span></label>
                    <label><input type="radio" name="q<%= q.getQuestionId() %>" value="C"><span>C. <%= q.getOptionC() %></span></label>
                    <label><input type="radio" name="q<%= q.getQuestionId() %>" value="D"><span>D. <%= q.getOptionD() %></span></label>
                </div>
            </div>
            <% } %>
            <div class="submit-section">
                <button type="submit" class="btn">Submit Exam</button>
            </div>
        </form>
    </div>
</body>
</html>
