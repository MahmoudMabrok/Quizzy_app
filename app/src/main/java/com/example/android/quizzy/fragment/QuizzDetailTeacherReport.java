package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.TeacherHome;
import com.example.android.quizzy.adapter.ReportQuizzesDetailTeacherAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.interfaces.OnQuizzReportClick;
import com.example.android.quizzy.model.AttemptedQuiz;
import com.example.android.quizzy.model.Award;
import com.example.android.quizzy.model.Data;
import com.example.android.quizzy.model.StudentGradeItem;
import com.example.android.quizzy.util.Constants;
import com.example.android.quizzy.util.Utils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    public static final String TAG = "QuizzDetailTeacherRepor";
    @BindView(R.id.btnCalacurtBest)
    Button btnCalacurtBest;
    String quizzID;
    private Data data;
    String quizzName;
    private String teacherUUID;
    private DataRepo repo = new DataRepo();

    public QuizzDetailTeacherReport() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = ((TeacherHome) getActivity()).dataSendedToQuizDetail;
        quizzID = data.getQuizID();
        quizzName = data.getQuizName();
        Bundle bundle = getArguments();
        if (bundle != null) {
            quizzName = bundle.getString(Constants.QUIZZ_NAME);
            quizzID = bundle.getString(Constants.Quizz_id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quizz_detail_teacher_report, container, false);
        //  teacherUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //// TODO: 12/14/2018 to replace when integrate
        teacherUUID = "011";
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

    @OnClick(R.id.btnCalacurtBest)
    public void onViewClicked() {
        if (data.getAttemptedQuizList().size() > 0) {
            List<Integer> list = new ArrayList<>();
            List<Item> itemList = new ArrayList<>();
            for (AttemptedQuiz quiz : data.getAttemptedQuizList()) {
                Item element = new Item(quiz.getStudentUUID(), quiz.getStudentName(), quiz.getPercentage());
                itemList.add(element);
                list.add(quiz.getPercentage());
            }
            Collections.sort(list);
            int max = list.get(list.size() - 1);
            Log.d(TAG, "onViewClicked: " + max);
            List<Award> awards = new ArrayList<>();
            Award award;
            for (Item item : itemList) {
                if (item.percentage == max) {
                    award = new Award();
                    award.setQuizzID(quizzID);
                    award.setQuizzName(quizzName);
                    award.setTeacherUUID(teacherUUID);
                    award.setStudentName(item.studentName);
                    award.setStudentUUID(item.studentUUID);
                    repo.addAward(award);

                    show(item.studentName);
                }
            }
        } else {
            show("No Student Complete a Quiz ");
        }

    }

    private void show(String a) {
        Toast.makeText(getContext(), a, Toast.LENGTH_SHORT).show();
    }

    private class Item {
        String studentUUID;
        String studentName;
        int percentage;

        public Item(String studentUUID, String studentName, int percentage) {
            this.studentUUID = studentUUID;
            this.studentName = studentName;
            this.percentage = percentage;
        }
    }
}
