package com.schlexamsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.schlexamsystem.dao.StudentDAO;
import com.schlexamsystem.model.Student;

@WebServlet("/studentLogin")
public class StudentLoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        StudentDAO dao = new StudentDAO();
        Student student = dao.login(email, password);

        if (student != null) {
            HttpSession session = request.getSession();
            session.setAttribute("student", student);
            response.sendRedirect("studentDashboard.jsp");
        } else {
            response.sendRedirect("studentLogin.jsp?error=invalid");
        }
    }
}
