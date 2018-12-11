package com.example.android.quizzy.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.adapter.QuestionListAdapterAddQuiz;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.fragment.QuestionAddFragment;
import com.example.android.quizzy.fragment.TimePickerQuizzFragment;
import com.example.android.quizzy.interfaces.onQuestionAdd;
import com.example.android.quizzy.model.Question;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * add and edit quiz created by teacher
 * Input: is key of teacher to add quiz in its child node
 * EditState: when accept quiz key so it be edited
 */
public class AddEditQuiz extends AppCompatActivity implements onQuestionAdd, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.edQuizName)
    EditText edQuizName;
    @BindView(R.id.questionContainer)
    LinearLayout questionContainer;
    @BindView(R.id.rvQustionListTeacher)
    RecyclerView rvQustionListTeacher;
    @BindView(R.id.btnSaveQuizz)
    Button btnSaveQuizz;
    @BindView(R.id.fabAddQuestion)
    FloatingActionButton fabAddQuestion;
    @BindView(R.id.quizzAndAddLayout)
    RelativeLayout quizzAndAddLayout;
    @BindView(R.id.btnSetTime)
    Button btnSetTime;
    @BindView(R.id.quizzHours)
    TextView quizzHours;
    @BindView(R.id.quizzMin)
    TextView quizzMin;

    private List<Question> questionList = new ArrayList<>();
    private FragmentManager manager = getSupportFragmentManager();
    private FragmentTransaction transaction;
    private QuestionListAdapterAddQuiz adapter;

    private String teacherKey;
    private DataRepo dataRepo;

    private String quizzKey;
    private boolean isUpdate;


    private int hours, minuts;
    Animation animationDown;
    Animation animationUP;
    private boolean isShown;
    private Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_detali_teacher);
        ButterKnife.bind(this);

        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        animationUP = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        dataRepo = new DataRepo();
        Intent intent = getIntent();
        teacherKey = intent.getStringExtra("t_key");
        if (teacherKey != null) {
            quizzKey = intent.getStringExtra("q_key");
            if (quizzKey != null) {
                isUpdate = true;
                fetchQustionList();
                btnSaveQuizz.setText(getResources().getText(R.string.update));
            }
        } else {
            finish();
        }

        initRv();
        replaceQuestionFragment();
    }

    private static final String TAG = "AddEditQuiz";

    private void fetchQustionList() {
        dataRepo.getSpecificQuizRef(teacherKey, quizzKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                edQuizName.setText(name);
                 quiz = dataSnapshot.getValue(Quiz.class);

                hours = quiz.getHour();
                minuts = quiz.getMinute();
                isShown = quiz.isShown();
                setHourMintInView();

                Log.d(TAG, "onDataChange:  1 " + dataSnapshot);
                Question question;
                questionList = new ArrayList<>();
                //   List<String> strings = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child(Constants.QUIZZ_QUESTION_LIST).getChildren()) {
                    //QuestionToSerialize question2 = snapshot.getValue(QuestionToSerialize.class);
                    question = snapshot.getValue(Question.class);
                    Log.d(TAG, "onDataChange: 2 " + snapshot);
                    if (question != null) {
                        questionList.add(question);
                        //  Log.d(TAG, "onDataChange: " + question.toString());
                    }
                }
                if (questionList.size() > 0) {
                    adapter.setQuestionList(questionList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Question getQuestionFromJson(DataSnapshot snapshot) {
        Question question = new Question();
        question.setQuestion((String) snapshot.child("question").getValue());
        question.setCorrectAnswer((String) snapshot.child("correctAnswer").getValue());
        List<String> ansewrs = new ArrayList<>();
        for (DataSnapshot snapshot1 : snapshot.child(Constants.Question_answer_list).getChildren()) {
            ansewrs.add((String) snapshot1.getValue());
        }
        question.setAnswerList(ansewrs);
        return question;
    }

    private void replaceQuestionFragment() {
        transaction = manager.beginTransaction();
        QuestionAddFragment fragment = new QuestionAddFragment();
        transaction.replace(R.id.questionContainer, fragment).commit();
    }

    private void initRv() {
        adapter = new QuestionListAdapterAddQuiz();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvQustionListTeacher.setLayoutManager(manager);
        rvQustionListTeacher.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                Question question = questionList.get(pos);
                if (direction == ItemTouchHelper.RIGHT) { // update
                    adapter.remove(pos);
                    questionList.remove(pos);
                    replaceQuestionFragment(question);
                } else {
                    questionList.remove(question);
                    adapter.remove(pos);
                }
            }
        }).attachToRecyclerView(rvQustionListTeacher);
    }

    private void replaceQuestionFragment(Question question) {
        transaction = manager.beginTransaction();
        QuestionAddFragment fragment = new QuestionAddFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constants.answerList, (ArrayList<String>) question.getAnswerList());
        bundle.putString(Constants.question, question.getQuestion());
        fragment.setArguments(bundle);
        transaction.replace(R.id.questionContainer, fragment).commit();
        transaction.addToBackStack(null);
        setViewToAddOrUpdateQuestion();
    }

    @OnClick(R.id.btnSaveQuizz)
    public void onViewClicked() {
        String quizName = edQuizName.getText().toString();
        if (!TextUtils.isEmpty(quizName)) {
            if (questionList.size() > 0) {
                Quiz newQuizz = new Quiz();
                if (quiz != null){
                    newQuizz = quiz ;
                }
                newQuizz.setName(quizName);
                newQuizz.setQuestionList(questionList);
                newQuizz.setTeacherKey(teacherKey);
                newQuizz.setHour(hours);
                newQuizz.setMinute(minuts);
                newQuizz.setShown(isShown);
                if (isUpdate) {
                    newQuizz.setKey(quizzKey);
                    show("updated");
                } else {
                    show("added");
                }
                dataRepo.addQuiz(newQuizz);
                // blankFields();
                finish();
                overridePendingTransition(0, R.anim.slide_down);
            } else {
                show("Please add Question");
            }
        } else {
            show("Enter Quiz Name");
        }
    }

    private void blankFields() {
        questionList.clear();
        adapter.clear();
        edQuizName.setText("");
    }

    private void show(String a) {
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }


    /**
     * when add fragment finish and sent the Question
     * hidden it
     *
     * @param question
     */
    @Override
    public void onQuestionAdd(Question question) {
        if (question.getAnswerList().size() > 0) {
            questionList.add(question);
            adapter.addQuestion(question);
            //make animation with gone visibility
            setViewAfterAddQuestion();
        }
    }

    private void setViewAfterAddQuestion() {
        questionContainer.setVisibility(View.GONE);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        questionContainer.setAnimation(animationDown);

        quizzAndAddLayout.setVisibility(View.VISIBLE);
        animationUP = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        quizzAndAddLayout.setAnimation(animationUP);

    }

    @OnClick(R.id.fabAddQuestion)
    public void onViewClicked11() {
        setViewToAddOrUpdateQuestion();
    }

    /**
     * used to make an animation  when call addQuestion Fragment in case of add or update
     * it first hide quize name and button layout and then show fragment
     */
    private void setViewToAddOrUpdateQuestion() {
        //make animation with gone visibility
        questionContainer.setVisibility(View.VISIBLE);
        animationUP = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        questionContainer.setAnimation(animationUP);

        quizzAndAddLayout.setVisibility(View.GONE);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        quizzAndAddLayout.setAnimation(animationDown);

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            setViewAfterAddQuestion();
        } else {
            super.onBackPressed();
        }
    }


    private void setHourMintInView() {
        quizzHours.setText(String.valueOf(hours) + "H");
        quizzMin.setText(String.valueOf(minuts + "M"));
    }

    @OnClick(R.id.btnSetTime)
    public void onViewClickedSetTime() {
        DialogFragment dialogFragment = new TimePickerQuizzFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("H", hours);
        bundle.putInt("M", minuts);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "Set Time For Quizz");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hours = hourOfDay;
        minuts = minute;
        setHourMintInView();

    }
}
