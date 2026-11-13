package com.schlexamsystem.model;

public class Exam {
    private int examId;
    private String title;
    private String subject;
    private int teacherId;
    private int duration; // in minutes

    public Exam() {}

    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}
