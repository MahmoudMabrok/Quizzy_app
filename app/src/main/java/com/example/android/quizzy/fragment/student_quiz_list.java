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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.QuizzQuestion;
import com.example.android.quizzy.adapter.QuizeListCompletedStudentAdapter;
import com.example.android.quizzy.adapter.QuizeListStudentAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.interfaces.OnQuizzClick;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.github.ybq.android.spinkit.SpinKitView;
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
    SpinKitView spinKit;
    @BindView(R.id.spin_kit_completed_quizz)
    SpinKitView spinKitCompletedQuizz;
    @BindView(R.id.rv_Student_done_Quizz)
    RecyclerView rvStudentDoneQuizz;
    @BindView(R.id.spin_kit_student_to_do)
    SpinKitView spinKitStudentToDo;
    @BindView(R.id.rv_Student_to_do_Quizz)
    RecyclerView rvStudentToDoQuizz;
    @BindView(R.id.tvNoQuizzCompleted)
    TextView tvNoQuizzCompleted;
    @BindView(R.id.tvNoQuizzToDo)
    TextView tvNoQuizzToDo;
    @BindView(R.id.dividerQuizzCompleted)
    View dividerQuizzCompleted;
    @BindView(R.id.QuizzCompletedLayout)
    LinearLayout QuizzCompletedLayout;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    private List<Quiz> completedList = new ArrayList<>();
    private List<Quiz> quizList = new ArrayList<>();
    private String studentName;
    private String studentUUID;
    private String teacherID;
    private QuizeListCompletedStudentAdapter completeAdapter;

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
        return view;
    }

    /**
     * load state
     * hide recycler  , text
     * show Spin
     */
    private void loadState() {
        showRv(View.GONE);
        setSpin(View.VISIBLE);
        controlTVNoData(View.GONE);
        controlDetails1(View.GONE);
    }

    private void controlDetails1(int gone) {
        dividerQuizzCompleted.setVisibility(gone);
        QuizzCompletedLayout.setVisibility(gone);
    }

    private void controlTVNoData(int gone) {
        tvNoQuizzCompleted.setVisibility(gone);
        tvNoQuizzToDo.setVisibility(gone);
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
                    Log.d(TAG, "no complete  " + dataSnapshot1);
                    temp = dataSnapshot1.getValue(Quiz.class);
                    if (temp != null && temp.getName() != null) {
                        if (temp.isShown()) {
                            if (!chechIfItInCompleteList(temp)) // check if it in complete list
                                quizList.add(temp);
                        }
                    }
                }
                if (rvStudentToDoQuizz != null) {
                    spinKitStudentToDo.setVisibility(View.GONE);
                    if (quizList.size() > 0) {
                        adapter.setList(quizList);
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

    /**
     * check if this quiz is in Complete list or not
     * by check key of it with Keys alreedy in Completed List
     * @param quiz quiz to be checked
     * @return true if it in complete , false otherwise
     */
    private boolean chechIfItInCompleteList(Quiz quiz) {
        for (Quiz q : completedList) {
            if (q.getKey().equals(quiz.getKey())) {
                return true;
            }
        }
        return false;
    }

    private void notFounddataState() {
        rvStudentToDoQuizz.setVisibility(View.GONE);
        tvNoQuizzToDo.setVisibility(View.VISIBLE);
    }

    private void foundDataState() {
        rvStudentToDoQuizz.setVisibility(View.VISIBLE);
        tvNoQuizzToDo.setVisibility(View.GONE);

    }

    private void showRv(int visible) {
        rvStudentDoneQuizz.setVisibility(visible);
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
                if (rvStudentDoneQuizz != null) { // in (active state )
                    completedList = new ArrayList<>(list);
                    spinKitCompletedQuizz.setVisibility(View.GONE);
                    if (completedList.size() > 0) { // there are a data to show
                        loadedStateComplete();
                        completeAdapter.addCompleteList(list);
                    } else {
                        emptyCompleteState();
                    }
                    startToDoRetrive();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void startToDoRetrive() {
        retriveQuizzList(teacherID);
    }

    private void loadedStateComplete() {
        rvStudentDoneQuizz.setVisibility(View.VISIBLE);
        tvNoQuizzCompleted.setVisibility(View.GONE);
        controlDetails1(View.VISIBLE);
    }

    private void emptyCompleteState() {
        rvStudentDoneQuizz.setVisibility(View.GONE);
        tvNoQuizzCompleted.setVisibility(View.VISIBLE);

        controlDetails1(View.GONE);
    }

    private void initRv() {
        //  adapter = new QuizeListStudentAdapter(getContext(), this);
        LinearLayoutManager managerCompleted = new LinearLayoutManager(getContext());
        completeAdapter = new QuizeListCompletedStudentAdapter(this);
        rvStudentDoneQuizz.setLayoutManager(managerCompleted);
        rvStudentDoneQuizz.setAdapter(completeAdapter);

        LinearLayoutManager managerToDo = new LinearLayoutManager(getContext());
        adapter = new QuizeListStudentAdapter(this);
        rvStudentToDoQuizz.setLayoutManager(managerToDo);
        rvStudentToDoQuizz.setAdapter(adapter);

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
        //// TODO: 12/14/2018 add a new check
        if (quiz.getQuestionList().get(0).getStudentAnswer() != null) {
            intent.putExtra(Constants.COMPLETED_Sate, true);
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
