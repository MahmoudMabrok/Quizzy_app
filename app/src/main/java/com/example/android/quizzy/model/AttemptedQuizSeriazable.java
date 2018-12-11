package com.example.android.quizzy.model;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.util.List;

@Keep
public class AttemptedQuizSeriazable implements Serializable {
    boolean state;
    private String studentName;
    private int grade;
    private List<Question> questionArrayList;


    public AttemptedQuizSeriazable() {
    }

    public AttemptedQuizSeriazable(AttemptedQuiz quiz) {
        setGrade(quiz.getGrade());
        setQuestionArrayList(quiz.getQuestionArrayList());
        setState(quiz.isState());
        setStudentName(quiz.getStudentName());
    }


    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
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

    public List<Question> getQuestionArrayList() {
        return questionArrayList;
    }

    public void setQuestionArrayList(List<Question> questionArrayList) {
        this.questionArrayList = questionArrayList;
    }
}
