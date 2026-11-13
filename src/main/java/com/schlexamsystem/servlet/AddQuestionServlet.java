package com.schlexamsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.schlexamsystem.dao.ExamDAO;
import com.schlexamsystem.model.Question;

@WebServlet("/addQuestion")
public class AddQuestionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int examId = Integer.parseInt(request.getParameter("examId"));
        String questionText = request.getParameter("questionText");
        String optionA = request.getParameter("optionA");
        String optionB = request.getParameter("optionB");
        String optionC = request.getParameter("optionC");
        String optionD = request.getParameter("optionD");
        String correctAnswer = request.getParameter("correctAnswer");

        Question q = new Question();
        q.setExamId(examId);
        q.setQuestionText(questionText);
        q.setOptionA(optionA);
        q.setOptionB(optionB);
        q.setOptionC(optionC);
        q.setOptionD(optionD);
        q.setCorrectAnswer(correctAnswer);

        ExamDAO dao = new ExamDAO();
        dao.addQuestion(q);

        response.sendRedirect("addQuestions.jsp?examId=" + examId + "&msg=added");
    }
}
