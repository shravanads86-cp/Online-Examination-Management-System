package com.schlexamsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.schlexamsystem.dao.UserDAO;

@WebServlet("/forgot")
public class ForgotPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            response.sendRedirect("forgot.jsp?error=empty");
            return;
        }

        email = email.trim();

        UserDAO dao = new UserDAO();
        boolean exists = (dao.findByEmail(email) != null);

        if (!exists) {
            response.sendRedirect("forgot.jsp?error=notregistered");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("resetEmail", email);
        response.sendRedirect("resetPassword.jsp");
    }
}
