package com.schlexamsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.schlexamsystem.dao.ExamDAO;
import com.schlexamsystem.model.Question;
import com.schlexamsystem.model.Teacher;

@WebServlet("/editQuestion")
public class EditQuestionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("teacher");

        if (teacher == null) {
            response.sendRedirect("teacherLogin.jsp");
            return;
        }

        int questionId = Integer.parseInt(request.getParameter("questionId"));
        int examId = Integer.parseInt(request.getParameter("examId"));
        String questionText = request.getParameter("questionText");
        String optionA = request.getParameter("optionA");
        String optionB = request.getParameter("optionB");
        String optionC = request.getParameter("optionC");
        String optionD = request.getParameter("optionD");
        String correctAnswer = request.getParameter("correctAnswer");

        Question q = new Question();
        q.setQuestionId(questionId);
        q.setExamId(examId);
        q.setQuestionText(questionText);
        q.setOptionA(optionA);
        q.setOptionB(optionB);
        q.setOptionC(optionC);
        q.setOptionD(optionD);
        q.setCorrectAnswer(correctAnswer);

        ExamDAO dao = new ExamDAO();
        dao.updateQuestion(q);

        response.sendRedirect("addQuestions.jsp?examId=" + examId + "&msg=updated");
    }
}
