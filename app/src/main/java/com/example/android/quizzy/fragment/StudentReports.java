package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.android.quizzy.R;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentReports extends Fragment {
    public static final String TAG = "StudentReports";
    @BindView(R.id.tvmaxValueStudent)
    TextView tvmaxValueStudent;
    @BindView(R.id.tvminValueStudent)
    TextView tvminValueStudent;
    Unbinder unbinder;
    @BindView(R.id.spin_kit_NoDataToShow)
    SpinKitView spinKitNoDataToShow;
    @BindView(R.id.tvNoDataToShow)
    TextView tvNoDataToShow;
    @BindView(R.id.chartQuizzes)
    AnyChartView chartQuizzes;

    @BindView(R.id.tvAverageValueStudent)
    TextView tvAverageValueStudent;
    private ArrayList<Quiz> completedList = new ArrayList<>();
    private DataRepo dataRepo;
    private ArrayList<Quiz> quizList = new ArrayList<>();


    private String studentName;
    private String studentUUID;
    private String teacherID;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            teacherID = bundle.getString(Constants.STUDENT_Teacher_uuid);
            studentName = bundle.getString(Constants.STUDENT_NAME);
            studentUUID = bundle.getString(Constants.STUDENT_UUID);

        } else {
            Log.d(TAG, "onCreate:  error ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_reports, container, false);
        unbinder = ButterKnife.bind(this, view);
        dataRepo = new DataRepo();
        loadState();
        retriveQuizzList(teacherID);
        return view;
    }

    private void loadState() {
        chartQuizzes.setVisibility(View.GONE);
        // chartGrades.setVisibility(View.GONE);
    }


    private void comptueParamter() {
        int max = 0, min = 0;
        float avg = 0;
        ArrayList<Integer> list = new ArrayList<>();
        for (Quiz quiz : completedList) {
            list.add(quiz.getPercentage());
            avg += quiz.getPercentage();
        }
        Log.d(TAG, "onDataChange : comptueParamter --  " + list.size());
        if (list.size() > 0) {
            max = Collections.max(list);
            Log.d(TAG, "comptueParamter:  max " + max);
            min = Collections.min(list);
            avg = (float) (avg / list.size() * 1.0);
        }

        if (tvAverageValueStudent != null) {
            Log.d(TAG, "onDataChange  :: comptueParamter: " + "not null ");
            tvAverageValueStudent.setText(avg + " %");
            tvmaxValueStudent.setText(max + " %");
            tvminValueStudent.setText(min + " %");
        }

    }


    private void retriveQuizzList(String teacherID) {
        //region fetch data
        FirebaseDatabase.getInstance().
                getReference(Constants.USERS_KEY)
                .child(Constants.TEACHERS_KEY)
                .child(teacherID)
                .child(Constants.QUIZZ_CHILD)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: " + dataSnapshot);
                        Log.d(TAG, "onDataChange: " + 1);
                        List<Quiz> list = new ArrayList<>();
                        Quiz temp;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            temp = snapshot.getValue(Quiz.class);
                            if (temp != null && temp.isShown()) {
                                list.add(temp);
                            }
                        }
                        //if (list.size() > 0) {
                        quizList = new ArrayList<>(list);
                        retriveCompletedList(studentUUID);
                        //}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    private void retriveCompletedList(String studentUUID) {
        dataRepo.getCompleteListRef(teacherID, studentUUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isResumed()) {
                    // Log.d(TAG, "onDataChange:  completeList" + dataSnapshot);
                    Log.d(TAG, "onDataChange: " + 2);
                    List<Quiz> list = new ArrayList<>();
                    Quiz temp;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        temp = snapshot.getValue(Quiz.class);
                        if (temp != null) {
                            list.add(temp);
                        }
                    }
                    completedList = new ArrayList<>(list);
                    Log.d(TAG, "onDataChange: " + " complete list " + completedList.size());
                    spinKitNoDataToShow.setVisibility(View.GONE);
                    if (completedList.size() > 0) {
                        Log.d(TAG, "onDataChange: " + 3);
                        loadedState();
                        //computeDistributionGrades();
                        computeDistributionQuizzes();
                        comptueParamter();
                    } else {
                        noDataState();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadedState() {
        chartQuizzes.setVisibility(View.VISIBLE);
        tvNoDataToShow.setVisibility(View.GONE);
    }

    private void noDataState() {
        chartQuizzes.setVisibility(View.GONE);
        tvNoDataToShow.setVisibility(View.VISIBLE);
    }

    /**
     * represnt States of quizzes {"Success", "Failed", "Not Attemped"}
     */
    private void computeDistributionGrades() {
        show("A");
        String[] labels = new String[]{"Success", "Failed", "Not Attemped"};
        int success = 0, falid = 0, na;
        for (Quiz quiz : completedList) {
            if (quiz.getGrade() > Constants.FAILED)
                success++;
            else
                falid++;
        }
        Log.d(TAG, "computeDistributionGrades: " + completedList.size());
        na = quizList.size() - (success + falid);
        int[] values = new int[]{success, falid, na};
        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            data.add(new ValueDataEntry(labels[i], values[i]));
        }

        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");
        cartesian.animation(true);
        cartesian.title("Quizz Grades");
        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Grade Frequents");
        cartesian.yAxis(0).title("Count");
        // chartGrades.setChart(cartesian);
    }

    private void show(String m) {
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();
    }

    /**
     * represent all Quizzes State
     */
    private void computeDistributionQuizzes() {
        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data = new ArrayList<>();
        for (Quiz quiz : completedList) {
            data.add(new ValueDataEntry(quiz.getName(), quiz.getPercentage()));
        }

        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");
        cartesian.animation(true);
        cartesian.title("Quizz Performance ");
        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Quiz Name");
        cartesian.yAxis(0).title("% (max is 100 )");
        chartQuizzes.setChart(cartesian);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
