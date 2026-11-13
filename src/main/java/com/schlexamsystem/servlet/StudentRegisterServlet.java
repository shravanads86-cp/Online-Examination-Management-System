package com.schlexamsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.schlexamsystem.dao.StudentDAO;
import com.schlexamsystem.model.Student;

@WebServlet("/studentRegister")
public class StudentRegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setPassword(password);

        StudentDAO dao = new StudentDAO();
        boolean success = dao.register(student);

        if (success) {
            response.sendRedirect("studentLogin.jsp?msg=registered");
        } else {
            response.sendRedirect("studentRegister.jsp?error=failed");
        }
    }
}
