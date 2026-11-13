package com.schlexamsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.schlexamsystem.dao.UserDAO;
import com.schlexamsystem.model.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null) {
            response.sendRedirect("login.jsp?error=empty");
            return;
        }

        email = email.trim();

        UserDAO dao = new UserDAO();
        User user = dao.findByEmail(email);

        if (user == null) {
            response.sendRedirect("login.jsp?error=notregistered");
            return;
        }

        if (!password.equals(user.getPassword())) {
            response.sendRedirect("login.jsp?error=wrongpassword&email=" + java.net.URLEncoder.encode(email, "UTF-8"));
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        response.sendRedirect("dashboard.jsp");
    }
}
