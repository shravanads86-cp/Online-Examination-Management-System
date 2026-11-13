package com.schlexamsystem.servlet;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.schlexamsystem.dao.ExamDAO;
import com.schlexamsystem.model.Question;
import com.schlexamsystem.model.Result;
import com.schlexamsystem.model.Student;

@WebServlet("/takeExam")
public class TakeExamServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            response.sendRedirect("studentLogin.jsp");
            return;
        }
        int examId = Integer.parseInt(request.getParameter("examId"));
        ExamDAO dao = new ExamDAO();
        List<Question> questions = dao.getQuestionsByExam(examId);

        int score = 0;
        for (Question q : questions) {
            String answer = request.getParameter("q" + q.getQuestionId());
            if (answer != null && answer.equals(q.getCorrectAnswer())) {
                score++;
            }
        }
        Result result = new Result();
        result.setStudentId(student.getStudentId());
        result.setExamId(examId);
        result.setScore(score);
        result.setTotalQuestions(questions.size());

        dao.saveResult(result);

        response.sendRedirect("examResult.jsp?score=" + score + "&total=" + questions.size());
    }
}
