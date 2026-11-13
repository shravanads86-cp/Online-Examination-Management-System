<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>School Exam System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="landing">
    <div>
        <h1>School Exam System</h1>
        <p class="subtitle">Select your role to continue</p>
        <div class="role-cards">
            <div class="role-card">
                <h2>Student</h2>
                <p>Take exams and view your results</p>
                <a href="studentLogin.jsp" class="btn">Login</a>
                <a href="studentRegister.jsp" class="btn btn-outline">Register</a>
            </div>
            <div class="role-card">
                <h2>Teacher</h2>
                <p>Create exams and add questions</p>
                <a href="teacherLogin.jsp" class="btn">Login</a>
                <a href="teacherRegister.jsp" class="btn btn-outline">Register</a>
            </div>
            <div class="role-card">
                <h2>Admin</h2>
                <p>Manage students and teachers</p>
                <a href="adminLogin.jsp" class="btn">Login</a>
            </div>
        </div>
    </div>
</body>
</html>
