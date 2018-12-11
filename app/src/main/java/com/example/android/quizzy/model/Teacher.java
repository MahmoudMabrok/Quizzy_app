package com.example.android.quizzy.model;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class Teacher extends User {

    private String telephoneNumber;
    private String subject;

    public Teacher(){
        //No-arg constructor
    }

    public Teacher(@NonNull String id) {
        super(id);
    }

    public Teacher(@NonNull String id, @NonNull String firstName, @NonNull String lastName, @NonNull String telephoneNumber, @NonNull String subject, @Nullable String city) {
        super(id, firstName, lastName, city);
        this.telephoneNumber = telephoneNumber;
        this.subject = subject;
    }


    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
