package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.TeacherHome;
import com.example.android.quizzy.adapter.ReportQuizzesTeacherAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.interfaces.OnQuizzReportClick;
import com.example.android.quizzy.model.AttemptedQuiz;
import com.example.android.quizzy.model.Data;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.model.ReportQuizzItem;
import com.example.android.quizzy.util.Constants;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsTeacherFragment extends Fragment implements OnQuizzReportClick {

    @BindView(R.id.barTeacherQuizzes)
    BarChart barTeacherQuizzes;
    Unbinder unbinder;
    @BindView(R.id.rvReportQuiezzTeacher)
    RecyclerView rvReportQuiezzTeacher;
    @BindView(R.id.rvReportStudentsGradesTeacherFragment)
    RecyclerView rvReportStudentsGradesTeacherFragment;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.tvNoInternet)
    TextView tvNoInternet;
    @BindView(R.id.dividerQuizzReports)
    View dividerQuizzReports;
    @BindView(R.id.QuizzReortsLayout)
    LinearLayout QuizzReortsLayout;
    @BindView(R.id.spin_kit_student)
    SpinKitView spinKitStudent;
    @BindView(R.id.StudentQuizzReportLayout)
    LinearLayout StudentQuizzReportLayout;
    @BindView(R.id.tvNoInternetStudent)
    TextView tvNoInternetStudent;
    private long totalStudentNumber;
    private int n_quizzes;

    public ReportsTeacherFragment() {
        // Required empty public constructor
    }

    String teacherKey;
    ArrayList<List<BarEntry>> listOfListEntries = new ArrayList<>();
    private DataRepo repo = new DataRepo();
    List<Data> dataList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teacherKey = getArguments().getString(Constants.TEACHERS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports_teacher, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initRv();
        retriveTotalNStudents();
    }

    private void retriveTotalNStudents() {
        repo.getStudentOfTeacherRef(teacherKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalStudentNumber = dataSnapshot.getChildrenCount();
                if (isResumed()) {
                    start(teacherKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initRv() {
        adapter = new ReportQuizzesTeacherAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvReportQuiezzTeacher.setAdapter(adapter);
        rvReportQuiezzTeacher.setLayoutManager(manager);

        studentAdapter = new ReportQuizzesTeacherAdapter(null);
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        rvReportStudentsGradesTeacherFragment.setAdapter(studentAdapter);
        rvReportStudentsGradesTeacherFragment.setLayoutManager(manager1);

    }

    public static final String TAG = "ReportsTeacherFragment";

    private void start(final String teacherKey) {
        //region retriveQuizzes of Teacher
        repo.getTeacherQuizz(teacherKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isResumed()) {
                    dataList = new ArrayList<>();
                    Data data;
                    List<AttemptedQuiz> list;
                    AttemptedQuiz attemptedQuiz;
                    for (DataSnapshot quiz : dataSnapshot.getChildren()) { // for each quizz
                        data = new Data();
                        data.setQuizName((String) quiz.child(Constants.QUIZZ_NAME).getValue());
                        quizzNames.add(data.getQuizName());
                        Log.d(TAG, "onDataChange: Quizz name " + data.getQuizName());
                        list = new ArrayList<>();
                        for (DataSnapshot iteraateAttemp : quiz.child(Constants.AttemptedList).getChildren()) {
                            attemptedQuiz = iteraateAttemp.getValue(AttemptedQuiz.class);
                            Log.d(TAG, "onDataChange: " + attemptedQuiz);
                            list.add(attemptedQuiz);
                        }

                        data.setAttemptedQuizList(list);
                        dataList.add(data);
                    }
                    Log.d(TAG, "onDataChange: final size " + dataList.size());
                    startQuizAnalysis();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //endregion
    }


    List<String> quizzNames = new ArrayList<>();
    private ReportQuizzesTeacherAdapter adapter, studentAdapter;

    private void startQuizAnalysis() {
        int fails = 0, success = 0, na = 0;
        int xValue = 0;
        List<ReportQuizzItem> list = new ArrayList<>();
        HashMap<Integer, StringBuilder> grades;
        for (Data data : dataList) { //for each quizz
            fails = success = na = 0;
            grades = new HashMap<>();
            for (AttemptedQuiz attemptedQuiz : data.getAttemptedQuizList()) { //for each attemp (student solution)
                if (grades.containsKey(attemptedQuiz.getGrade())) {
                    StringBuilder builder = grades.get(attemptedQuiz.getGrade()).append("\n").append(attemptedQuiz.getStudentName());
                    grades.put(attemptedQuiz.getGrade(), builder);
                } else {
                    grades.put(attemptedQuiz.getGrade(), new StringBuilder(attemptedQuiz.getStudentName()));
                }
                if (attemptedQuiz.getGrade() > Constants.FAILED) {
                    success++;
                } else {
                    fails++;
                }
            }
            na = (int) (totalStudentNumber - (fails + success));
            na = na >= 0 ? na : 0;
            //assign result value statistics to data to be sended to QuizDetail Fragment
            data.setFails(fails);
            data.setSuccess(success);
            data.setNa(na);
            list.add(new ReportQuizzItem(fails, success, na, quizzNames.get(xValue)));
            xValue++;
        }
        spinKit.setVisibility(View.GONE);
        if (list.size() > 0) {
            QuizzReortsLayout.setVisibility(View.VISIBLE);
            for (ReportQuizzItem item : list) {
                adapter.add(item);
            }
            tvNoInternet.setVisibility(View.GONE);
        } else {
            QuizzReortsLayout.setVisibility(View.GONE);
            tvNoInternet.setVisibility(View.VISIBLE);
        }
        n_quizzes = dataList.size();
        startStudnetAnalysis();
    }

    private void startStudnetAnalysis() {
        retrieveCompleteList();
    }

    private void retrieveCompleteList() {
        repo.getStudentOfTeacherRef(teacherKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (rvReportStudentsGradesTeacherFragment != null) { // to be sure fragment in visible state
                    Log.d(TAG, "onDataChange:  complete" + dataSnapshot);
                    List<ReportQuizzItem> list = new ArrayList<>();
                    Quiz quiz;
                    String StudnetNAME;
                    int fails = 0, success = 0, na = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        StudnetNAME = (String) dataSnapshot1.child(Constants.STUDENT_NAME).getValue();
                        Log.d(TAG, "StudnetNAME " + StudnetNAME);
                        fails = success = na = 0;
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.child(Constants.COMPLETED_QUIZZ).getChildren()) {
                            quiz = dataSnapshot2.getValue(Quiz.class);
                            if (quiz != null) {
                                if (quiz.getGrade() > Constants.FAILED) {
                                    success++;
                                } else {
                                    fails++;
                                }
                            }
                        }
                        na = n_quizzes - (fails + success);
                        na = na >= 0 ? na : 0;
                        list.add(new ReportQuizzItem(fails, success, na, StudnetNAME));
                    }
                    spinKitStudent.setVisibility(View.GONE);
                    if (list.size() > 0) {
                        StudentQuizzReportLayout.setVisibility(View.VISIBLE);
                        for (ReportQuizzItem item1 : list) {
                            studentAdapter.add(item1); //used to animate inseting items
                        }
                        tvNoInternetStudent.setVisibility(View.GONE);
                    } else {
                        tvNoInternetStudent.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void show(String s) {
        if (getContext() != null) {
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * position of clicked quizz to show each full details
     *
     * @param pos
     */
    @Override
    public void onClick(int pos) {
        Log.d(TAG, "onClick: " + pos);
        Data data = dataList.get(pos);
        ((TeacherHome) getActivity()).openQuizzDetail(data);

    }
}
