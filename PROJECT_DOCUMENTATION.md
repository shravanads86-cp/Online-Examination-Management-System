# School Exam System - Project Documentation & Interview Guide

## 1. PROJECT OVERVIEW

A web-based exam system with three roles: **Student**, **Teacher**, and **Admin**. Students take MCQ exams, teachers create exams and add questions, admins approve registrations.

---

## 2. FILE-BY-FILE EXPLANATION

### **MODELS (Model Layer - Data Structure)**

| File | Purpose | Key Fields |
|------|---------|------------|
| **Student.java** | Represents a student user | studentId, name, email, password, status (APPROVED/PENDING) |
| **Teacher.java** | Represents a teacher user | teacherId, name, email, password, subject, status |
| **Admin.java** | Represents admin user | adminId, username, password |
| **Exam.java** | Represents an exam | examId, title, subject, teacherId, duration |
| **Question.java** | Represents MCQ question | questionId, examId, questionText, optionA-D, correctAnswer |
| **Result.java** | Represents student exam result | resultId, studentId, examId, score, totalQuestions, examDate |

---

### **DAO (Data Access Object - Database Operations)**

| File | Purpose | Key Methods |
|------|---------|-------------|
| **StudentDAO.java** | Student DB operations | `login()`, `register()`, `getAllPending()`, `approve()`, `reject()` |
| **TeacherDAO.java** | Teacher DB operations | `login()`, `register()`, `getAllPending()`, `approve()`, `reject()` |
| **AdminDAO.java** | Admin DB operations | `login()` |
| **ExamDAO.java** | Exam/Question/Result DB operations | `createExam()`, `addQuestion()`, `getQuestionById()`, `updateQuestion()`, `getAllExams()`, `getQuestionsByExam()`, `saveResult()`, `getResultsByStudent()`, `hasStudentTakenExam()` |

---

### **SERVLETS (Controller Layer - Request Handling)**

| File | Purpose | URL | Method |
|------|---------|-----|--------|
| **StudentLoginServlet** | Student login | /studentLogin | POST |
| **StudentRegisterServlet** | Student registration | /studentRegister | POST |
| **TeacherLoginServlet** | Teacher login | /teacherLogin | POST |
| **TeacherRegisterServlet** | Teacher registration | /teacherRegister | POST |
| **AdminLoginServlet** | Admin login | /adminLogin | POST |
| **AdminActionServlet** | Approve/Reject users | /adminAction | GET |
| **CreateExamServlet** | Create new exam | /createExam | POST |
| **AddQuestionServlet** | Add question to exam | /addQuestion | POST |
| **EditQuestionServlet** | Update question (fix wrong answer) | /editQuestion | POST |
| **TakeExamServlet** | Submit exam & calculate score | /takeExam | POST |
| **LogoutServlet** | Session invalidation | /logout | GET |

---

### **UTILITY**

| File | Purpose |
|------|---------|
| **DBConnection.java** | Singleton DB connection using JDBC, MySQL driver |

---

### **JSP (View Layer - Presentation)**

| File | Purpose |
|------|---------|
| **index.jsp** | Home page - role selection (Student/Teacher/Admin) |
| **studentLogin.jsp** | Student login form |
| **studentRegister.jsp** | Student registration form |
| **studentDashboard.jsp** | Lists available exams, "Start Exam" button, results table |
| **startExam.jsp** | Displays exam questions (radio buttons) |
| **examResult.jsp** | Shows score, percentage, PASS/FAIL |
| **teacherLogin.jsp** | Teacher login form |
| **teacherRegister.jsp** | Teacher registration form |
| **teacherDashboard.jsp** | Create exam form, list of teacher's exams |
| **addQuestions.jsp** | Add questions form + list with **Edit** button |
| **editQuestion.jsp** | Form to change correct answer or edit question text/options |
| **adminLogin.jsp** | Admin login form |
| **adminDashboard.jsp** | Pending students & teachers with Approve/Reject buttons |

---

## 3. HOW EDIT QUESTION FEATURE WORKS

**Flow when teacher selects wrong answer:**

1. Teacher adds question → sees it in "Questions Added" list.
2. Clicks **Edit** button → goes to `editQuestion.jsp?questionId=X&examId=Y`.
3. Form shows pre-filled question text, options A-D, and current correct answer.
4. Teacher changes dropdown "Correct Answer" (or edits any part).
5. Clicks **Save Changes** → `EditQuestionServlet` receives POST.
6. Servlet calls `ExamDAO.updateQuestion()` → executes `UPDATE question SET ...`.
7. Redirects back to `addQuestions.jsp?examId=Y&msg=updated`.

---

## 4. COMMON INTERVIEWER QUESTIONS & ANSWERS

### **Architecture & Design**

**Q: What design pattern is used for database connection?**  
**A:** Singleton. `DBConnection.getConnection()` returns one shared Connection object.

**Q: What is MVC in this project?**  
**A:** Model (Student, Teacher, Exam, etc.), View (JSPs), Controller (Servlets). DAO acts as data access layer.

**Q: Why use DAO instead of writing SQL in servlets?**  
**A:** Separation of concerns. DAO centralizes database logic; servlets stay focused on request/response. Easier to maintain and test.

---

### **Session & Security**

**Q: How is user login state maintained?**  
**A:** `HttpSession`. After login, we store `student`/`teacher`/`admin` in session. Each protected page checks `session.getAttribute()`.

**Q: How do you prevent unauthorized access to teacher/student pages?**  
**A:** At the top of each JSP/dashboard, we check `if (session.getAttribute("teacher") == null) { response.sendRedirect("login"); return; }`.

**Q: Is the password stored securely?**  
**A:** No, currently plain text. In production we should use BCrypt or similar hashing.

---

### **Database**

**Q: Why use PreparedStatement instead of Statement?**  
**A:** Prevents SQL Injection. PreparedStatement uses parameterized queries (`?` placeholders), so user input cannot modify the query structure.

**Q: What is the purpose of `Statement.RETURN_GENERATED_KEYS`?**  
**A:** After INSERT, we get the auto-generated `exam_id` so we can redirect to add questions for that exam.

**Q: How do you handle duplicate exam attempts by a student?**  
**A:** `hasStudentTakenExam(studentId, examId)` checks the `result` table. If found, we show "Completed" instead of "Start Exam".

---

### **Servlets**

**Q: What is @WebServlet?**  
**A:** Annotation to map a servlet to a URL without defining it in web.xml.

**Q: Difference between doGet and doPost?**  
**A:** GET for retrieving data (e.g. logout, admin approve link). POST for submitting forms (login, register, create exam) – data in request body.

**Q: Why redirect after form submission instead of forwarding?**  
**A:** Post-Redirect-Get (PRG) pattern. Prevents form resubmission on browser refresh.

---

### **JSP**

**Q: What is the difference between JSP and Servlet?**  
**A:** JSP is for presentation (HTML with embedded Java). Servlet is for logic. JSP gets compiled into a servlet internally.

**Q: What does `<%= %>` mean?**  
**A:** Expression tag – outputs the value of a Java expression into HTML.

**Q: What does `<% %>` mean?**  
**A:** Scriptlet – executes Java code (loops, if-else, variable declarations).

---

### **Edit Question Feature**

**Q: How does the Edit Question feature work?**  
**A:** Teacher clicks Edit on a question → `editQuestion.jsp` loads with `questionId` and `examId`. `ExamDAO.getQuestionById()` fetches the question. Form submits to `EditQuestionServlet`, which calls `ExamDAO.updateQuestion()` to run `UPDATE question SET ... WHERE question_id=?`.

**Q: What SQL is used to update a question?**  
**A:** `UPDATE question SET question_text=?, option_a=?, option_b=?, option_c=?, option_d=?, correct_answer=? WHERE question_id=?`

---

## 5. FLOW DIAGRAMS (Verbal Explanation)

**Student Take Exam Flow:**  
Login → Dashboard → Click Start Exam → `startExam.jsp` loads questions → Submit → `TakeExamServlet` compares answers with `correct_answer` → Calculates score → Saves to `result` table → Redirects to `examResult.jsp`.

**Teacher Create Exam Flow:**  
Login → Dashboard → Fill Create Exam form → `CreateExamServlet` inserts exam → Redirects to `addQuestions.jsp` → Add questions → Click Edit to fix wrong answer → `EditQuestionServlet` updates question.

**Admin Approval Flow:**  
Login → Dashboard → See pending students/teachers → Click Approve/Reject → `AdminActionServlet` runs `UPDATE status='APPROVED'` or `DELETE`.

---

## 6. TECHNOLOGIES USED

- **Backend:** Java, Servlets, JSP
- **Database:** MySQL
- **Server:** Apache Tomcat 9
- **JDBC:** MySQL Connector/J
