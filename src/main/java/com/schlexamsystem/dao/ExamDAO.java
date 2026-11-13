package com.schlexamsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.schlexamsystem.model.Exam;
import com.schlexamsystem.model.Question;
import com.schlexamsystem.model.Result;
import com.schlexamsystem.util.DBConnection;

public class ExamDAO {

    // Create exam and return generated exam_id
    public int createExam(Exam exam) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO exam (title, subject, teacher_id, duration) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, exam.getTitle());
            ps.setString(2, exam.getSubject());
            ps.setInt(3, exam.getTeacherId());
            ps.setInt(4, exam.getDuration());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Add question to exam
    public boolean addQuestion(Question q) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO question (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, q.getExamId());
            ps.setString(2, q.getQuestionText());
            ps.setString(3, q.getOptionA());
            ps.setString(4, q.getOptionB());
            ps.setString(5, q.getOptionC());
            ps.setString(6, q.getOptionD());
            ps.setString(7, q.getCorrectAnswer());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all exams
    public List<Exam> getAllExams() {
        List<Exam> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM exam";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Exam e = new Exam();
                e.setExamId(rs.getInt("exam_id"));
                e.setTitle(rs.getString("title"));
                e.setSubject(rs.getString("subject"));
                e.setTeacherId(rs.getInt("teacher_id"));
                e.setDuration(rs.getInt("duration"));
                list.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get exams by teacher
    public List<Exam> getExamsByTeacher(int teacherId) {
        List<Exam> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM exam WHERE teacher_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Exam e = new Exam();
                e.setExamId(rs.getInt("exam_id"));
                e.setTitle(rs.getString("title"));
                e.setSubject(rs.getString("subject"));
                e.setTeacherId(rs.getInt("teacher_id"));
                e.setDuration(rs.getInt("duration"));
                list.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get exam by id
    public Exam getExamById(int examId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM exam WHERE exam_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, examId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Exam e = new Exam();
                e.setExamId(rs.getInt("exam_id"));
                e.setTitle(rs.getString("title"));
                e.setSubject(rs.getString("subject"));
                e.setTeacherId(rs.getInt("teacher_id"));
                e.setDuration(rs.getInt("duration"));
                return e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get questions for exam
    public List<Question> getQuestionsByExam(int examId) {
        List<Question> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM question WHERE exam_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, examId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Question q = new Question();
                q.setQuestionId(rs.getInt("question_id"));
                q.setExamId(rs.getInt("exam_id"));
                q.setQuestionText(rs.getString("question_text"));
                q.setOptionA(rs.getString("option_a"));
                q.setOptionB(rs.getString("option_b"));
                q.setOptionC(rs.getString("option_c"));
                q.setOptionD(rs.getString("option_d"));
                q.setCorrectAnswer(rs.getString("correct_answer"));
                list.add(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Save result
    public boolean saveResult(Result result) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO result (student_id, exam_id, score, total_questions) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, result.getStudentId());
            ps.setInt(2, result.getExamId());
            ps.setInt(3, result.getScore());
            ps.setInt(4, result.getTotalQuestions());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get results for student
    public List<Result> getResultsByStudent(int studentId) {
        List<Result> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT r.*, e.title as exam_title FROM result r JOIN exam e ON r.exam_id = e.exam_id WHERE r.student_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Result r = new Result();
                r.setResultId(rs.getInt("result_id"));
                r.setStudentId(rs.getInt("student_id"));
                r.setExamId(rs.getInt("exam_id"));
                r.setScore(rs.getInt("score"));
                r.setTotalQuestions(rs.getInt("total_questions"));
                r.setExamDate(rs.getTimestamp("exam_date"));
                r.setExamTitle(rs.getString("exam_title"));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get question by id
    public Question getQuestionById(int questionId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM question WHERE question_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, questionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Question q = new Question();
                q.setQuestionId(rs.getInt("question_id"));
                q.setExamId(rs.getInt("exam_id"));
                q.setQuestionText(rs.getString("question_text"));
                q.setOptionA(rs.getString("option_a"));
                q.setOptionB(rs.getString("option_b"));
                q.setOptionC(rs.getString("option_c"));
                q.setOptionD(rs.getString("option_d"));
                q.setCorrectAnswer(rs.getString("correct_answer"));
                return q;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update question (to change correct answer or edit question)
    public boolean updateQuestion(Question q) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE question SET question_text=?, option_a=?, option_b=?, option_c=?, option_d=?, correct_answer=? WHERE question_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, q.getQuestionText());
            ps.setString(2, q.getOptionA());
            ps.setString(3, q.getOptionB());
            ps.setString(4, q.getOptionC());
            ps.setString(5, q.getOptionD());
            ps.setString(6, q.getCorrectAnswer());
            ps.setInt(7, q.getQuestionId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if student already took exam
    public boolean hasStudentTakenExam(int studentId, int examId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM result WHERE student_id=? AND exam_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, examId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
