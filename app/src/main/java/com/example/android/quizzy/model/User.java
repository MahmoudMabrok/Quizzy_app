package com.example.android.quizzy.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String city;

    User(){
        //No-arg constructor
    }

    User(@NonNull String id) {
        this.id = id;
    }

    User(@NonNull String id, @NonNull String firstName, @NonNull String lastName, @Nullable String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
