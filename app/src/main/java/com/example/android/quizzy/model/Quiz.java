package com.example.android.quizzy.model;

import android.support.annotation.Keep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mahmoud on 10/21/2018.
 */
@Keep
public class Quiz {
    private String key;
    private String name ;
    private String creatorName ;
    private String teacherKey;
    private List<Question> questionList ;
    private int score;
    private boolean shown;
    private int percentage;
    private int grade;
    private int hour = 0;
    private int minute = 15;

    private List<AttemptedQuiz> AttemptedQuiz ;

    public List<com.example.android.quizzy.model.AttemptedQuiz> getAttemptedQuiz() {
        return AttemptedQuiz;
    }

    public void setAttemptedQuiz(List<com.example.android.quizzy.model.AttemptedQuiz> attemptedQuiz) {
        AttemptedQuiz = attemptedQuiz;
    }

    public Quiz() {
    }


    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getTeacherKey() {
        return teacherKey;
    }

    public void setTeacherKey(String teacherKey) {
        this.teacherKey = teacherKey;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
