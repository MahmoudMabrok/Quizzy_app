package com.example.android.quizzy;

import android.app.Application;
import android.content.Context;

import com.example.android.quizzy.api.impl.ApiLayerModule;
import com.example.android.quizzy.viewModel.impl.ViewModelLayerModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApiLayerModule.class, ViewModelLayerModule.class})
public class AppModule {

    private Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return app;
    }

}
