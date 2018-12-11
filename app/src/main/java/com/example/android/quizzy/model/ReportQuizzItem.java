package com.example.android.quizzy.model;

public class ReportQuizzItem {
    private int fails;
    private int success;
    private int na;
    private String name;
    private String topStudent;

    public String getTopStudent() {
        return topStudent;
    }

    public void setTopStudent(String topStudent) {
        this.topStudent = topStudent;
    }

    public ReportQuizzItem() {
    }

    public ReportQuizzItem(int fails, int success, int na, String name) {
        this.fails = fails;
        this.success = success;
        this.na = na;
        this.name = name;
    }

    public int getFails() {
        return fails;
    }

    public void setFails(int fails) {
        this.fails = fails;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getNa() {
        return na;
    }

    public void setNa(int na) {
        this.na = na;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
