package com.example.android.quizzy.model;

public class StudentGradeItem {
    private String name;
    private int grade;
    private int percentage;

    public StudentGradeItem(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }

    public StudentGradeItem(String name, int grade, int percentage) {
        this.name = name;
        this.grade = grade;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
