package com.example.android.quizzy.interfaces;

import com.example.android.quizzy.model.Quiz;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public interface OnQuizzClick {
    void onQuizzClick(Quiz quiz);

    void onQuizzChangeState(Quiz quiz, boolean isChecked);
}
