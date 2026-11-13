package com.schlexamsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.schlexamsystem.dao.UserDAO;
import com.schlexamsystem.model.User;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.sendRedirect("register.jsp?error=empty");
            return;
        }

        email = email.trim();
        password = password.trim();

        UserDAO dao = new UserDAO();
        User existing = dao.findByEmail(email);

        if (existing != null) {
            response.sendRedirect("register.jsp?error=exists");
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        boolean success = dao.register(user);

        if (success) {
            response.sendRedirect("login.jsp?msg=success");
        } else {
            response.sendRedirect("register.jsp?error=failed");
        }
    }
}
