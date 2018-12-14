package com.example.android.quizzy.model;

import android.support.annotation.Keep;

@Keep
public class Award {

    private String key;
    private String teacherUUID;
    private String quizzName;
    private String quizzID;
    private String StudentUUID;
    private String studentName;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTeacherUUID() {
        return teacherUUID;
    }

    public void setTeacherUUID(String teacherUUID) {
        this.teacherUUID = teacherUUID;
    }

    public String getQuizzName() {
        return quizzName;
    }

    public void setQuizzName(String quizzName) {
        this.quizzName = quizzName;
    }

    public String getQuizzID() {
        return quizzID;
    }

    public void setQuizzID(String quizzID) {
        this.quizzID = quizzID;
    }

    public String getStudentUUID() {
        return StudentUUID;
    }

    public void setStudentUUID(String studentUUID) {
        StudentUUID = studentUUID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
