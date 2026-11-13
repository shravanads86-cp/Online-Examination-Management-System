package com.schlexamsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.schlexamsystem.dao.TeacherDAO;
import com.schlexamsystem.model.Teacher;

@WebServlet("/teacherRegister")
public class TeacherRegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String subject = request.getParameter("subject");

        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setEmail(email);
        teacher.setPassword(password);
        teacher.setSubject(subject);

        TeacherDAO dao = new TeacherDAO();
        boolean success = dao.register(teacher);

        if (success) {
            response.sendRedirect("teacherLogin.jsp?msg=registered");
        } else {
            response.sendRedirect("teacherRegister.jsp?error=failed");
        }
    }
}
