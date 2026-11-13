package com.schlexamsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.schlexamsystem.dao.ExamDAO;
import com.schlexamsystem.model.Exam;
import com.schlexamsystem.model.Teacher;

@WebServlet("/createExam")
public class CreateExamServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("teacher");

        if (teacher == null) {
            response.sendRedirect("teacherLogin.jsp");
            return;
        }

        String title = request.getParameter("title");
        String subject = request.getParameter("subject");
        int duration = Integer.parseInt(request.getParameter("duration"));

        Exam exam = new Exam();
        exam.setTitle(title);
        exam.setSubject(subject);
        exam.setTeacherId(teacher.getTeacherId());
        exam.setDuration(duration);

        ExamDAO dao = new ExamDAO();
        int examId = dao.createExam(exam);

        if (examId > 0) {
            response.sendRedirect("addQuestions.jsp?examId=" + examId);
        } else {
            response.sendRedirect("teacherDashboard.jsp?error=failed");
        }
    }}
