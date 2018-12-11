package com.example.android.quizzy.model;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mahmoud on 10/21/2018.
 */
@Keep
public class QuestionToSerialize implements Serializable {
    public Map<String, List<String>> theanswer;
    private String key;
    private String question;
    private List<String> answerList;
    private int weight;
    private String correctAnswer;

    public QuestionToSerialize() {
    }

    public QuestionToSerialize(String question, int weight, String correctAnswer) {
        this.question = question;
        this.weight = weight;
        this.correctAnswer = correctAnswer;
    }


    public Map<String, List<String>> getTheanswer() {
        return theanswer;
    }

    public void setTheanswer(Map<String, List<String>> theanswer) {
        this.theanswer = theanswer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Question{" +
                "key='" + key + '\'' +
                ", question='" + question + '\'' +
                ", answerList=" + answerList.size() +
                ", weight=" + weight +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = new ArrayList<>(answerList);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
