package com.schlexamsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.schlexamsystem.dao.StudentDAO;
import com.schlexamsystem.dao.TeacherDAO;

@WebServlet("/adminAction")
public class AdminActionServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String type = request.getParameter("type");
        int id = Integer.parseInt(request.getParameter("id"));

        if ("student".equals(type)) {
            StudentDAO dao = new StudentDAO();
            if ("approve".equals(action)) {
                dao.approve(id);
            } else if ("reject".equals(action)) {
                dao.reject(id);
            }
        } else if ("teacher".equals(type)) {
            TeacherDAO dao = new TeacherDAO();
            if ("approve".equals(action)) {
                dao.approve(id);
            } else if ("reject".equals(action)) {
                dao.reject(id);
            }
        }

        response.sendRedirect("adminDashboard.jsp");
    }
}
