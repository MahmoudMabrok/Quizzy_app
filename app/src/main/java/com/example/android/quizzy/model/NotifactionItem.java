package com.example.android.quizzy.model;

public class NotifactionItem {
    private String quizzID;
    private String StudentID;
    private String quizzName;
    private String studentName;
    private boolean isSeen;
    private int grade;


    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getQuizzID() {
        return quizzID;
    }

    public void setQuizzID(String quizzID) {
        this.quizzID = quizzID;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getQuizzName() {
        return quizzName;
    }

    public void setQuizzName(String quizzName) {
        this.quizzName = quizzName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
