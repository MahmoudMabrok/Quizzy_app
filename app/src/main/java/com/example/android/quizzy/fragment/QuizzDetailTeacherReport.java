package com.example.android.quizzy.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.QuizzQuestion;
import com.example.android.quizzy.activity.TeacherHome;
import com.example.android.quizzy.adapter.ReportQuizzesDetailTeacherAdapter;
import com.example.android.quizzy.interfaces.OnQuizzReportClick;
import com.example.android.quizzy.model.AttemptedQuiz;
import com.example.android.quizzy.model.AttemptedQuizSeriazable;
import com.example.android.quizzy.model.Data;
import com.example.android.quizzy.model.StudentGradeItem;
import com.example.android.quizzy.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizzDetailTeacherReport extends Fragment implements OnQuizzReportClick {


    @BindView(R.id.quizzNameDetailReport)
    TextView quizzNameDetailReport;
    @BindView(R.id.tvquizSuccess)
    TextView tvquizSuccess;
    @BindView(R.id.tvquizFailed)
    TextView tvquizFailed;
    @BindView(R.id.tvquizNA)
    TextView tvquizNA;
    @BindView(R.id.rvReportStudentDetailTeacher)
    RecyclerView rvReportStudentDetailTeacher;
    Unbinder unbinder;

    private Data data;
    String quizzName;

    public QuizzDetailTeacherReport() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = ((TeacherHome) getActivity()).dataSendedToQuizDetail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quizz_detail_teacher_report, container, false);
        unbinder = ButterKnife.bind(this, view);
        fillView();
        RV();

        return view;
    }

    private void RV() {
        adapter = new ReportQuizzesDetailTeacherAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvReportStudentDetailTeacher.setLayoutManager(manager);
        rvReportStudentDetailTeacher.setAdapter(adapter);

        for (AttemptedQuiz quiz : data.getAttemptedQuizList()) {
            adapter.add(new StudentGradeItem(quiz.getStudentName(), quiz.getGrade(), quiz.getPercentage()));
        }
    }

    private ReportQuizzesDetailTeacherAdapter adapter;

    private void fillView() {
        quizzNameDetailReport.setText(data.getQuizName());
        tvquizFailed.setText(String.valueOf(data.getFails()));
        tvquizSuccess.setText(String.valueOf(data.getSuccess()));
        tvquizNA.setText(String.valueOf(data.getNa()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * @param pos
     */
    @Override
    public void onClick(int pos) {
        AttemptedQuiz quiz = data.getAttemptedQuizList().get(pos);
        ((TeacherHome) getActivity()).openSolvedQuizz(quiz);
    }
}
