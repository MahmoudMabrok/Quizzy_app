package com.example.android.quizzy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.interfaces.OnQuizzClick;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;

import java.util.ArrayList;
import java.util.List;

import az.plainpie.PieView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class QuizeListCompletedStudentAdapter extends RecyclerView.Adapter<QuizeListCompletedStudentAdapter.ViewHolder> {

    Quiz quiz;
    private Context context;
    private List<Quiz> completeList;
    private OnQuizzClick onQuizzClick;

    public QuizeListCompletedStudentAdapter(OnQuizzClick click) {
        onQuizzClick = click;
    }

    public void setList(List<Quiz> list, List<Quiz> completeList) {
        this.completeList = new ArrayList<>(completeList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_quiz_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tvQuizState.setText("Not Attempted"); //in case of deleted items and re-binded
        Quiz quiz = completeList.get(position);
        holder.tvQuizName.setText(quiz.getName());
        holder.tvQuizTeacherName.setText(quiz.getTeacherKey());
        String text = "N/A";
        text = (quiz.getScore()) + " / " + quiz.getQuestionList().size();
        holder.tvQuizTotalScore.setText(text);
        holder.piStudent.setPercentage(quiz.getPercentage());
        if (quiz.getGrade() == Constants.FAILED) {
            holder.tvQuizState.setTextColor(Color.YELLOW);
            holder.tvQuizState.setBackgroundColor(Color.RED);
            holder.tvQuizState.setText("Failed");
        } else {
            holder.tvQuizState.setTextColor(Color.GREEN);
            holder.tvQuizState.setBackgroundColor(Color.BLACK);
            holder.tvQuizState.setText("Succeded");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuizzClick.onQuizzClick(quiz);
            }
        });

    }


    @Override
    public int getItemCount() {
        return completeList.size();
    }

    public void addCompleteList(List<Quiz> completedList) {
        this.completeList = new ArrayList<>(completedList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvQuizName)
        TextView tvQuizName;
        @BindView(R.id.tvQuizTeacherName)
        TextView tvQuizTeacherName;
        @BindView(R.id.tvQuizTotalScore)
        TextView tvQuizTotalScore;
        @BindView(R.id.piStudent)
        PieView piStudent;
        @BindView(R.id.tvQuizState)
        TextView tvQuizState;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
