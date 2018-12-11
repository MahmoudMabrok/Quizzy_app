package com.example.android.quizzy.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.android.quizzy.R;

import java.util.HashMap;

public class Student_TeacherDialog extends Dialog implements View.OnClickListener {

    private LoginFragment fragment;
    private HashMap<String, Object> userInput;

    public Student_TeacherDialog(@NonNull Context context, LoginFragment fragment, HashMap<String, Object> userInput) {
        super(context);
        this.fragment = fragment;
        this.userInput = userInput;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        Button studentBtn = findViewById(R.id.btn_dialog_student);
        Button teacherBtn = findViewById(R.id.btn_dialog_teacher);
        studentBtn.setOnClickListener(this);
        teacherBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dialog_student :
                fragment.getTransient().openFragment(RegisterStudentFragment.Companion.newInstance(userInput));
                break;
            case R.id.btn_dialog_teacher :
                fragment.getTransient().openFragment(RegisterTeacherFragment.Companion.newInstance(userInput));
                break;
        }
        dismiss();
    }
}
