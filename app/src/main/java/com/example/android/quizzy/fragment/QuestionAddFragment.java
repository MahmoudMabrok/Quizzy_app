package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.AddEditQuiz;
import com.example.android.quizzy.adapter.AnwerListAddQuizAdapter;
import com.example.android.quizzy.model.Question;
import com.example.android.quizzy.util.Constants;
import com.example.android.quizzy.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionAddFragment extends Fragment {


    @BindView(R.id.edQuestionAddFragment)
    EditText edQuestionAddFragment;
    @BindView(R.id.edAnswerAddFragment)
    EditText edAnswerAddFragment;
    @BindView(R.id.btnAddAnswer)
    Button btnAddAnswer;
    @BindView(R.id.rvAnsWersAddQuizz)
    RecyclerView rvAnsWersAddQuizz;
    @BindView(R.id.btnAddQuestion)
    Button btnAddQuestion;
    Unbinder unbinder;

    AnwerListAddQuizAdapter adapter;
    boolean isUpdate;

    public QuestionAddFragment() {
        // Required empty public constructor
    }

    private List<String> answerList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_add, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRv();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String question = bundle.getString(Constants.question);
            ArrayList<String> anserList = bundle.getStringArrayList(Constants.answerList);
            fillUi(question, anserList);
            isUpdate = true;
        }
        return view;
    }


    private void initRv() {
        adapter = new AnwerListAddQuizAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvAnsWersAddQuizz.setLayoutManager(manager);
        rvAnsWersAddQuizz.setAdapter(adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.RIGHT) {
                    String anser = answerList.get(pos);
                    answerList.remove(pos);
                    adapter.remove(pos);
                    edAnswerAddFragment.setText(anser);
                } else {
                    answerList.remove(pos);
                    adapter.remove(pos);
                }

            }
        }).attachToRecyclerView(rvAnsWersAddQuizz);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void fillUi(String question, ArrayList<String> anser) {
        edQuestionAddFragment.setText(question);
        answerList = new ArrayList<>(anser);
        adapter.setList(answerList);
    }

    private void fillUi(Question question) {
        edQuestionAddFragment.setText(question.getQuestion());
        show(question.getQuestion());
        answerList = new ArrayList<>(question.getAnswerList());
        adapter.setList(question.getAnswerList());
    }

    @OnClick(R.id.btnAddAnswer)
    public void onBtnAddAnswerClicked() {
        String anser = edAnswerAddFragment.getText().toString();
        if (!TextUtils.isEmpty(anser)) {
            adapter.add(anser);
            answerList.add(anser);
            edAnswerAddFragment.setText("");
            Utils.hideInputKeyboard(getContext());
        } else {
            show("Enter Answer");
        }
    }

    private void show(String a) {
        Toast.makeText(getContext(), a, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnAddQuestion)
    public void onBtnAddQuestionClicked() {
        String questionName = edQuestionAddFragment.getText().toString();
        if (!TextUtils.isEmpty(questionName)) {
            if (answerList.size() > 0) {
                Question question = new Question();
                question.setQuestion(questionName);
                question.setAnswerList(answerList);
                question.setWeight(1);
                if (adapter.getCorrect() != null) {
                    question.setCorrectAnswer(adapter.getCorrect());
                    addQuestion(question);
                    blankFields();
                } else {
                    show("select an Correct Answer");
                    return;
                }
            } else {
                show("Add Answers");
                return;
            }
        } else {
            show("Add Question Name");
        }
    }

    private void addQuestion(Question question) {
        if (question != null && question.getAnswerList().size() > 0) {
            ((AddEditQuiz) getActivity()).onQuestionAdd(question);
        } else {
            show("Error");
        }
    }

    private void blankFields() {
        edQuestionAddFragment.setText("");
        adapter.deleteAll();
        answerList.clear();
    }

}
