package com.example.android.quizzy.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.adapter.QuestionQuizAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.model.AttemptedQuiz;
import com.example.android.quizzy.model.NotifactionItem;
import com.example.android.quizzy.model.Question;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.example.android.quizzy.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * now handle case is to solve a non-attemped
 */
public class QuizzQuestion extends AppCompatActivity {

    private static final String TAG = "QuizzQuestion";
    @BindView(R.id.rvQuizzQuestionList)
    RecyclerView rvQuizzQuestionList;
    DataRepo repo;
    Quiz quiz;
    @BindView(R.id.quizzSubmit)
    Button quizzSubmit;
    @BindView(R.id.timeQuiz)
    TextView timeQuiz;
    private QuestionQuizAdapter adapter;

    //add name to message
    private String successString = "Congratulations \nKeep Going";
    private String failString = "OOOH Sorry  \nStudy More Next Time";

    String sID;
    String quizeID;
    String teacher;
    String studentName;

    CountDownTimer countTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_question);
        ButterKnife.bind(this);

        sID = getIntent().getStringExtra(Constants.STUDENT_UUID);
        quizeID = getIntent().getStringExtra(Constants.Quizz_id);
        teacher = getIntent().getStringExtra(Constants.STUDENT_Teacher_uuid);
        studentName = getIntent().getStringExtra(Constants.STUDENT_NAME);

        Log.d(TAG, "onCreate: Student name " + studentName);
        initRv();
        repo = new DataRepo();

        if (!getIntent().getBooleanExtra("s", false)) { //case of new quizz (non-solved quizz)
            repo.getSpecificQuizRef(teacher, quizeID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "LLL " + dataSnapshot);
                    quiz = dataSnapshot.getValue(Quiz.class);
                    setQuestionList(quiz.getQuestionList());
                    setTimer(quiz.getHour(), quiz.getMinute());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            repo.getStudentCompletedQuizz(teacher, sID, quizeID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    quiz = dataSnapshot.getValue(Quiz.class);
                    Log.d(TAG, "onDataChange: " + dataSnapshot);
                    adapter.setList(quiz.getQuestionList());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            quizzSubmit.setVisibility(View.GONE);
        }

    }

    private void setTimer(int hour, int minute) {
        minute += (hour * 60);
        countTimer = new CountDownTimer((1000 * 60 * minute), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String time = Utils.timeToString(millisUntilFinished);
                timeQuiz.setText(time);
            }

            @Override
            public void onFinish() {
                if (!isFinishing()) {
                    timeQuiz.setText(Utils.timeToString(0));
                    show("Bye");
                    onViewClicked();
                    quizzSubmit.setVisibility(View.GONE);
                }
            }
        };
        countTimer.start();
    }

    private void setQuestionList(List<Question> questionList) {
        adapter.setList(questionList);
    }


    private void initRv() {
        adapter = new QuestionQuizAdapter(this);
        rvQuizzQuestionList.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvQuizzQuestionList.setLayoutManager(manager);


    }


    @OnClick(R.id.quizzSubmit)
    public void onViewClicked() {
        countTimer.cancel();
        AttemptedQuiz attemptedQuiz = new AttemptedQuiz();
        //  attemptedQuiz.setQuizzName(quiz.getName());
        attemptedQuiz.setStudentUUID(sID);
        NotifactionItem notifactionItem = new NotifactionItem();
        notifactionItem.setQuizzName(quiz.getName());

        notifactionItem.setQuizzID(quizeID);
        notifactionItem.setStudentName(studentName);

        List<Question> questionList = adapter.getList();
        float score = 0;
        for (Question question : questionList) {
            if (question.getStudentAnswer() != null ) {
                if (question.getStudentAnswer().equals(question.getCorrectAnswer())) {
                    question.setState(true);
                    score++;
                }
            }
        }
        Log.d(TAG, "score: " + score);

        quiz.setQuestionList(questionList);
        quiz.setScore((int) score);
        double percentage = (score / quiz.getQuestionList().size() * 1.0) * 100;
        Log.d(TAG, "score : " + percentage);
        quiz.setPercentage((int) percentage);
        attemptedQuiz.setPercentage((int) percentage);

        if (percentage < 50) {
            quiz.setGrade(Constants.FAILED);
            attemptedQuiz.setGrade(Constants.FAILED);
            notifactionItem.setGrade(Constants.FAILED);
        } else if (percentage < 65) {
            quiz.setGrade(Constants.ACCEPT);
            attemptedQuiz.setGrade(Constants.ACCEPT);
            notifactionItem.setGrade(Constants.ACCEPT);
        } else if (percentage < 75) {
            quiz.setGrade(Constants.GOOD);
            attemptedQuiz.setGrade(Constants.GOOD);
            notifactionItem.setGrade(Constants.GOOD);
        } else if (percentage < 85) {
            quiz.setGrade(Constants.VGOOD);
            attemptedQuiz.setGrade(Constants.VGOOD);
            notifactionItem.setGrade(Constants.VGOOD);
        } else {
            quiz.setGrade(Constants.Excellent);
            attemptedQuiz.setGrade(Constants.Excellent);
            notifactionItem.setGrade(Constants.Excellent);
        }

        if (percentage >= 50) {
            attemptedQuiz.setState(true);
            Utils.makeAlert(this, successString, (int) percentage).show();

        } else {

            Utils.makeAlert(this, failString, (int) percentage).show();
        }

        attemptedQuiz.setStudentName(studentName);
        attemptedQuiz.setQuestionArrayList(questionList);
        repo.addQuizTOCompleteList(quiz, sID);
        repo.addAttemted(attemptedQuiz, quizeID, teacher);
        show("Quizz Solved ");
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                finishActvityWithAnim();
            }
        }.start();
    }

    private void finishActvityWithAnim() {
        finish();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down); // animation with finish the activity
    }

    private void show(String quizz_solved_) {
        Toast.makeText(this, quizz_solved_, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
  //      super.onBackPressed();
        if (countTimer != null) {
            onViewClicked();
        }else{
           finishActvityWithAnim();
        }
    }
}
