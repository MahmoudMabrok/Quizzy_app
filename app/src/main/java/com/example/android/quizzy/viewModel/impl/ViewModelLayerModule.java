package com.example.android.quizzy.viewModel.impl;

import android.content.Context;

import com.example.android.quizzy.api.LoginApi;
import com.example.android.quizzy.viewModel.LoginViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelLayerModule {

    @Provides
    @Singleton
    LoginViewModel provideLoginViewModel(Context context, LoginApi api) {
        return new LoginViewModelImpl(context, api);
    }

}
