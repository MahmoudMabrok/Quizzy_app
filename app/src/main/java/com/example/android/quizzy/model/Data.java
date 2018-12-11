package com.example.android.quizzy.model;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

    private String quizName;
    private int fails;
    private int success;
    private int na;
    private List<AttemptedQuiz> attemptedQuizList;
    private String topStudent;


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

    public String getTopStudent() {
        return topStudent;
    }

    public void setTopStudent(String topStudent) {
        this.topStudent = topStudent;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public List<AttemptedQuiz> getAttemptedQuizList() {
        return attemptedQuizList;
    }

    public void setAttemptedQuizList(List<AttemptedQuiz> attemptedQuizList) {
        this.attemptedQuizList = attemptedQuizList;
    }
}
