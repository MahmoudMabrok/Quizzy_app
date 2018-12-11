package com.example.android.quizzy.api;

import com.example.android.quizzy.model.User;
import com.google.firebase.auth.AuthResult;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface LoginApi {

    Maybe<AuthResult> registerInFirebaseAuth(String email, String password);
    Completable registerInFirebaseDatabase(User user);
    Maybe<AuthResult> login(String email, String password);
    Single<Boolean> teacherExists(String teacherTelephoneNumber);
    Single<User> getUser(String id);

}
