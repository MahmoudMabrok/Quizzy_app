package com.example.android.quizzy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.QuizzQuestion;
import com.example.android.quizzy.adapter.QuizeListStudentAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.interfaces.OnQuizzClick;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class student_quiz_list extends Fragment implements OnQuizzClick {

    public static final String TAG = "student_quiz_list";

    @BindView(R.id.rvQuizListStudent)
    RecyclerView rvQuizListStudent;
    Unbinder unbinder;
    DataRepo dataRepo = new DataRepo();
    QuizeListStudentAdapter adapter;
    @BindView(R.id.spin_kit)
    com.github.ybq.android.spinkit.SpinKitView spinKit;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;


    private List<Quiz> completedList = new ArrayList<>();
    private List<Quiz> quizList = new ArrayList<>();
    private String studentName;
    private String studentUUID;
    private String teacherID;

    public student_quiz_list() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studentUUID = getArguments().getString(Constants.STUDENT_UUID);
            teacherID = getArguments().getString(Constants.STUDENT_Teacher_uuid);
            studentName = getArguments().getString(Constants.STUDENT_NAME);
        } else {
            show("error");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_quiz_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        loadState();
        initRv();
        retriveCompletedList(studentUUID);
        retriveQuizzList(teacherID);

        return view;
    }

    private void loadState() {
        showRv(View.GONE);
        setSpin(View.VISIBLE);
        setTextView(View.GONE);

    }

    private void setTextView(int gone) {
        tvNoData.setVisibility(gone);
    }

    private void setSpin(int visible) {
        spinKit.setVisibility(visible);
    }

    private void retriveQuizzList(String teacherID) {
        dataRepo.getTeacherQuizz(teacherID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Quiz temp;
                quizList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d(TAG, "@2 onDataChange: " + dataSnapshot1);
                    temp = dataSnapshot1.getValue(Quiz.class);
                    if (temp != null && temp.getName() != null) {
                        if (temp.isShown()) {
                            quizList.add(temp);
                        }
                    }
                }
                if (rvQuizListStudent != null) {
                    setSpin(View.GONE);
                    if (quizList.size() > 0 && completedList != null) {
                        adapter.setList(quizList, completedList);
                        foundDataState();
                    } else {
                        notFounddataState();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void notFounddataState() {
        setTextView(View.VISIBLE);
        showRv(View.GONE);
    }

    private void foundDataState() {
        setTextView(View.GONE);
        showRv(View.VISIBLE);

    }

    private void showRv(int visible) {
        rvQuizListStudent.setVisibility(visible);
    }

    private void retriveCompletedList(String studentUUID) {
        dataRepo.getCompleteListRef(teacherID, studentUUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange:  completeList" + dataSnapshot);
                List<Quiz> list = new ArrayList<>();
                Quiz temp;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    temp = snapshot.getValue(Quiz.class);
                    if (temp != null) {
                        list.add(temp);
                    }
                }
                completedList = new ArrayList<>(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initRv() {
        adapter = new QuizeListStudentAdapter(getContext(), this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvQuizListStudent.setLayoutManager(manager);
        rvQuizListStudent.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onQuizzClick(Quiz quiz) {
        Intent intent = new Intent(getContext(), QuizzQuestion.class);
        intent.putExtra(Constants.STUDENT_UUID, studentUUID);
        intent.putExtra(Constants.Quizz_id, quiz.getKey());
        if (quiz.getQuestionList().get(0).getStudentAnswer() != null) {
            intent.putExtra("s", true);
        }
        intent.putExtra(Constants.STUDENT_NAME, studentName);
        intent.putExtra(Constants.STUDENT_Teacher_uuid, teacherID);
        getContext().startActivity(intent);
    }

    @Override
    public void onQuizzChangeState(Quiz quiz, boolean isChecked) {

    }

    private void show(String name) {
        Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
    }

}
