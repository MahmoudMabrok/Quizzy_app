package com.example.android.quizzy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.quizzy.R;
import com.example.android.quizzy.interfaces.OnQuizzClick;
import com.example.android.quizzy.model.Quiz;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Quiz quiz = completeList.get(position);
        holder.tvReportQuizzName.setText(quiz.getName());
        holder.quizzPercentage.setText(quiz.getPercentage() + "%");

        /*
        holder.tvQuizState.setText("Not Attempted"); //in case of deleted items and re-binded

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
*/

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
        @BindView(R.id.tvReportQuizzName)
        TextView tvReportQuizzName;
        @BindView(R.id.quizzPercentage)
        TextView quizzPercentage;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
