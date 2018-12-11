package com.example.android.quizzy.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.AddEditQuiz;
import com.example.android.quizzy.adapter.QuizeListTeacherAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.interfaces.OnQuizzClick;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizzListTeacher extends Fragment implements OnQuizzClick {


    public static final String TAG = "QuizzListTeacher";
    @BindView(R.id.rvQuizListTeacher)
    RecyclerView rvQuizListTeacher;
    Unbinder unbinder;
    @BindView(R.id.fabAddQuizz)
    FloatingActionButton fabAddQuizz;
    @BindView(R.id.tvNoInternet)
    TextView tvNoInternet;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    private QuizeListTeacherAdapter adapter;

    @Override
    public void onQuizzChangeState(Quiz quiz, boolean isChecked) {
        //// TODO: 12/7/2018
        quiz.setShown(isChecked);
        dataRepo.addQuiz(quiz);
    }

    public QuizzListTeacher() {
        // Required empty public constructor
    }

    private String teacherKey;
    private FirebaseDatabase database;
    private DataRepo dataRepo = new DataRepo();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quizz_list_teacher, container, false);
        unbinder = ButterKnife.bind(this, view);
        teacherKey = getArguments().getString(Constants.TEACHERS_KEY);
        initRv();

        return view;
    }

    private void initRv() {
        adapter = new QuizeListTeacherAdapter(getContext(), this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvQuizListTeacher.setAdapter(adapter);
        rvQuizListTeacher.setLayoutManager(manager);
        //   controlTextView(true);
        //   database = FirebaseDatabase.getInstance();

    }

    @Override
    public void onResume() {
        super.onResume();

        //region fetch data
        DatabaseReference reference = FirebaseDatabase.getInstance().
                getReference(Constants.USERS_KEY)
                .child(Constants.TEACHERS_KEY)
                .child(teacherKey)
                .child(Constants.QUIZZ_CHILD);

        Disposable disposable = RxFirebaseDatabase.observeSingleValueEvent(reference, DataSnapshotMapper.listOf(Quiz.class))
                .subscribe(e -> {
                    Log.d(TAG, "initRv: " + e.size());
                    if (this.isResumed()) {
                        spinKit.setVisibility(View.GONE);
                        if (e.size() > 0) {
                            adapter.setList(e);
                            tvNoInternet.setVisibility(View.GONE);
                        } else {
                            makeNoItem();
                        }
                    }
                   /* if (tvNoInternet != null) {
                        tvNoInternet.setVisibility(View.GONE);
                    }*/
                });

    }

    private void makeNoItem() {
        tvNoInternet.setVisibility(View.VISIBLE);
        tvNoInternet.setText(getString(R.string.no_data_found));
    }


    private void controlTextView(boolean b) {
        if (tvNoInternet != null) {
            if (b) {
                tvNoInternet.setVisibility(View.VISIBLE);
            } else {
                tvNoInternet.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onQuizzClick(Quiz quiz) {
        Intent view = new Intent(getContext(), AddEditQuiz.class);
        if (quiz != null) {
            Log.d(TAG, "onQuizzClick: " + quiz.getQuestionList().size());
        }
        view.putExtra("t_key", teacherKey);
        view.putExtra("q_key", quiz.getKey());
        startActivity(view);
    }

    @OnClick(R.id.fabAddQuizz)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), AddEditQuiz.class);
        intent.putExtra("t_key", teacherKey);
        startActivity(intent);

    }
}
