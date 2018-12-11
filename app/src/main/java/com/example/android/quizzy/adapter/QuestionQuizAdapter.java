package com.example.android.quizzy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.quizzy.R;
import com.example.android.quizzy.model.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class QuestionQuizAdapter extends RecyclerView.Adapter<QuestionQuizAdapter.ViewHolder> {


    private List<Question> questionList;

    public QuestionQuizAdapter() {
        questionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_question_item, parent, false);
        return new ViewHolder(view);
    }

    private static final String TAG = "QuestionQuizAdapter";

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String strNum = "Question " + (position + 1);
        holder.tvQuestionNum.setText(strNum);
        final Question question = questionList.get(position);
        holder.tvQuestionTitle.setText(question.getQuestion());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.itemView.getContext(), android.R.layout.simple_dropdown_item_1line, question.getAnswerList());
        holder.spAnswerList.setAdapter(adapter);
        holder.spAnswerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String answer = (String) holder.spAnswerList.getSelectedItem();
                Log.d(TAG, "onItemSelected: " + answer);
                question.setStudentAnswer(answer);
                questionList.set(position, question);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.spAnswerList.setEnabled(true);
        //case solved quizz
        String answer = question.getStudentAnswer();
        Log.d(TAG, "onBindViewHolder: " + answer);
        if (answer != null) {
            int pos = question.getAnswerList().indexOf(answer);
            Log.d(TAG, "onBindViewHolder:  pos" + pos);
            holder.spAnswerList.setSelection(pos);

            if (question.getCorrectAnswer().equals(answer)) {
                holder.stateOK.setVisibility(View.VISIBLE);
                holder.stateOFF.setVisibility(View.GONE);
            } else {
                holder.stateOFF.setVisibility(View.VISIBLE);
                holder.stateOK.setVisibility(View.GONE);
            }

            holder.spAnswerList.setEnabled(false);
        }

      /*  final String s = anserList.get(position);
        holder.tvAnswer.setText(s);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cbAnswer.isChecked()) {
                    // checkedList.remove(s);
                    holder.cbAnswer.setChecked(false);
                } else {
                    //checkedList.add(s);
                    holder.cbAnswer.setChecked(true);
                }
            }
        });*/
/*
        holder.cbAnswer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkedList.add(s);
                } else {
                    checkedList.remove(s);
                }
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }


    public void setList(List<Question> question) {
        questionList = new ArrayList<>(question);
        notifyDataSetChanged();
    }

    public List<Question> getList() {
        return questionList;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvQuestionNum)
        TextView tvQuestionNum;
        @BindView(R.id.tvQuestionTitle)
        TextView tvQuestionTitle;
        @BindView(R.id.spAnswerList)
        Spinner spAnswerList;
        @BindView(R.id.rvQuizzAnwerQuestion)
        RecyclerView rvQuizzAnwerQuestion;


        @BindView(R.id.stateOK)
        TextView stateOK;
        @BindView(R.id.stateOFF)
        TextView stateOFF;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
