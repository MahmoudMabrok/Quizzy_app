package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.TeacherHome;
import com.example.android.quizzy.adapter.QuestionQuizAdapter;
import com.example.android.quizzy.model.AttemptedQuiz;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowSolvedQuiz extends Fragment {


    @BindView(R.id.rvQuizzQuestionList)
    RecyclerView rvQuizzQuestionList;
    Unbinder unbinder;

    public ShowSolvedQuiz() {
        // Required empty public constructor
    }

    private QuestionQuizAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_solved_quiz, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRV();
        return view;
    }

    private void initRV() {
        adapter = new QuestionQuizAdapter(getContext());
        rvQuizzQuestionList.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvQuizzQuestionList.setLayoutManager(manager);

        data();
    }

    private void data() {
        AttemptedQuiz attemptedQuiz = ((TeacherHome) getActivity()).attemptedQuiz;
        adapter.setList(attemptedQuiz.getQuestionArrayList());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
