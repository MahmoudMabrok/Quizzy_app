package com.example.android.quizzy;

import com.example.android.quizzy.fragment.LoginFragment;
import com.example.android.quizzy.fragment.RegisterFragment;
import com.example.android.quizzy.fragment.RegisterStudentFragment;
import com.example.android.quizzy.fragment.RegisterTeacherFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(LoginFragment loginFragment);
    void inject(RegisterFragment registerFragment);
    void inject(RegisterStudentFragment registerStudentFragment);
    void inject(RegisterTeacherFragment registerTeacherFragment);

}
