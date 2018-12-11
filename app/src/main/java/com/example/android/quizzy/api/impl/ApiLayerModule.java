package com.example.android.quizzy.api.impl;

import com.example.android.quizzy.api.LoginApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiLayerModule {

    @Provides
    @Singleton
    LoginApi provideLoginApi() {
        return new LoginApiImpl();
    }

}
