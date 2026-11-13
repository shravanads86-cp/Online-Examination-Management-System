package com.schlexamsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.schlexamsystem.dao.UserDAO;

@WebServlet("/resetPassword")
public class ResetPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("resetEmail");

        if (email == null) {
            response.sendRedirect("forgot.jsp?error=session");
            return;
        }

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (newPassword == null || confirmPassword == null || newPassword.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            response.sendRedirect("resetPassword.jsp?error=empty");
            return;
        }

        newPassword = newPassword.trim();
        confirmPassword = confirmPassword.trim();

        if (!newPassword.equals(confirmPassword)) {
            response.sendRedirect("resetPassword.jsp?error=mismatch");
            return;
        }

        UserDAO dao = new UserDAO();
        boolean success = dao.updatePassword(email, newPassword);

        if (success) {
            session.removeAttribute("resetEmail");
            response.sendRedirect("login.jsp?msg=resetsuccess");
        } else {
            response.sendRedirect("resetPassword.jsp?error=failed");
        }
    }
}
