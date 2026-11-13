package com.schlexamsystem.model;

import java.sql.Timestamp;

public class Result {
    private int resultId;
    private int studentId;
    private int examId;
    private int score;
    private int totalQuestions;
    private Timestamp examDate;
    private String examTitle; // for display

    public Result() {}

    public int getResultId() { return resultId; }
    public void setResultId(int resultId) { this.resultId = resultId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public Timestamp getExamDate() { return examDate; }
    public void setExamDate(Timestamp examDate) { this.examDate = examDate; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }
}
