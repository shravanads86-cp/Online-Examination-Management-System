package com.schlexamsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.schlexamsystem.dao.AdminDAO;
import com.schlexamsystem.model.Admin;

@WebServlet("/adminLogin")
public class AdminLoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        AdminDAO dao = new AdminDAO();
        Admin admin = dao.login(username, password);

        if (admin != null) {
            HttpSession session = request.getSession();
            session.setAttribute("admin", admin);
            response.sendRedirect("adminDashboard.jsp");
        } else {
            response.sendRedirect("adminLogin.jsp?error=invalid");
        }
    }
}
