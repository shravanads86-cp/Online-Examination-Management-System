<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.schlexamsystem.model.*, com.schlexamsystem.dao.*, java.util.*" %>
<%
    Teacher teacher = (Teacher) session.getAttribute("teacher");
    if (teacher == null) {
        response.sendRedirect("teacherLogin.jsp");
        return;
    }
    int examId = Integer.parseInt(request.getParameter("examId"));
    ExamDAO examDAO = new ExamDAO();
    Exam exam = examDAO.getExamById(examId);
    List<Question> questions = examDAO.getQuestionsByExam(examId);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Questions - <%= exam.getTitle() %></title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>Add Questions</h2>
        <div>
            <a href="teacherDashboard.jsp">Back to Dashboard</a>
            <a href="logout">Logout</a>
        </div>
    </div>
    <div class="page-container">
        <div class="card">
            <h3>Exam: <%= exam.getTitle() %> (<%= exam.getSubject() %>)</h3>
            <% if(request.getParameter("msg") != null) { 
                String msg = request.getParameter("msg");
                if ("added".equals(msg)) { %>
                <div class="msg-success">Question added successfully!</div>
            <% } else if ("updated".equals(msg)) { %>
                <div class="msg-success">Question updated successfully!</div>
            <% } } %>
            <form action="addQuestion" method="post">
                <input type="hidden" name="examId" value="<%= examId %>">
                <div class="form-group">
                    <label>Question</label>
                    <textarea name="questionText" required placeholder="Enter your question here..."></textarea>
                </div>
                <div class="options-grid">
                    <div class="form-group">
                        <label>Option A</label>
                        <input type="text" name="optionA" required>
                    </div>
                    <div class="form-group">
                        <label>Option B</label>
                        <input type="text" name="optionB" required>
                    </div>
                    <div class="form-group">
                        <label>Option C</label>
                        <input type="text" name="optionC" required>
                    </div>
                    <div class="form-group">
                        <label>Option D</label>
                        <input type="text" name="optionD" required>
                    </div>
                </div>
                <div class="form-group">
                    <label>Correct Answer</label>
                    <select name="correctAnswer" required>
                        <option value="A">A</option>
                        <option value="B">B</option>
                        <option value="C">C</option>
                        <option value="D">D</option>
                    </select>
                </div>
                <button type="submit" class="btn">Add Question</button>
            </form>
        </div>

        <div class="card">
            <h3>Questions Added (<%= questions.size() %>)</h3>
            <% if (questions.isEmpty()) { %>
                <p>No questions added yet.</p>
            <% } else { %>
            <div class="question-list">
                <% int num = 1; for (Question q : questions) { %>
                <div class="question-item">
                    <div class="q-content">
                        <strong>Q<%= num++ %>:</strong> <%= q.getQuestionText() %><br>
                        A. <%= q.getOptionA() %> | B. <%= q.getOptionB() %> | C. <%= q.getOptionC() %> | D. <%= q.getOptionD() %><br>
                        <strong>Answer:</strong> <%= q.getCorrectAnswer() %>
                    </div>
                    <a href="editQuestion.jsp?questionId=<%= q.getQuestionId() %>&examId=<%= examId %>" class="btn-edit">Edit</a>
                </div>
                <% } %>
            </div>
            <% } %>
        </div>
    </div>
</body>
</html>
