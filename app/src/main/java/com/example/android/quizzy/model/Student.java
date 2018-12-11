package com.example.android.quizzy.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Student extends User {

    private String academicYear;
    private String teacherTelephoneNumber;

    public Student(){
        //No-arg constructor
    }

    public Student(@NonNull String id){
        super(id);
    }

    public Student(@NonNull String id, @NonNull String firstName, @NonNull String lastName, @Nullable String academicYear, @Nullable String city, @NonNull String teacherTelephoneNumber) {
        super(id, firstName, lastName, city);
        this.academicYear = academicYear;
        this.teacherTelephoneNumber = teacherTelephoneNumber;
    }


    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getTeacherTelephoneNumber() {
        return teacherTelephoneNumber;
    }

    public void setTeacherTelephoneNumber(String teacherTelephoneNumber) {
        this.teacherTelephoneNumber = teacherTelephoneNumber;
    }

}
