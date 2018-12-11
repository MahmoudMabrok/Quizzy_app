package com.example.android.quizzy.model;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahmoud on 10/21/2018.
 */
@Keep
public class QuizSeriazle implements Serializable {
    private String key;
    private String name;
    private String creatorName;
    private String teacherKey;
    private List<Question> questionList;
    private int score;

    public QuizSeriazle() {
    }

    public QuizSeriazle(String key, String name, String creatorName, String teacherKey, List<Question> questionList, int score) {
        this.key = key;
        this.name = name;
        this.creatorName = creatorName;
        this.teacherKey = teacherKey;
        this.questionList = questionList;
        this.score = score;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTeacherKey() {
        return teacherKey;
    }

    public void setTeacherKey(String teacherKey) {
        this.teacherKey = teacherKey;
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

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = new ArrayList<>(questionList);
    }
}
