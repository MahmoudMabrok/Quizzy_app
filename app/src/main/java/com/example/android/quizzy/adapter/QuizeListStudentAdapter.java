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

public class QuizeListStudentAdapter extends RecyclerView.Adapter<QuizeListStudentAdapter.ViewHolder> {

    Quiz quiz;
    private Context context;
    private List<Quiz> quizList;
    private OnQuizzClick onQuizzClick;

    public QuizeListStudentAdapter(OnQuizzClick click) {
        this.context = context;
        onQuizzClick = click;
        quizList = new ArrayList<>();
    }

    public void setList(List<Quiz> list) {
        quizList = new ArrayList<>(list);
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
        Quiz quiz = quizList.get(position);
        holder.tvQuizName.setText(quiz.getName());
        holder.tvQuizTeacherName.setText(quiz.getTeacherKey());
        String text = "N/A";
        holder.tvQuizTotalScore.setText(text);
        holder.tvQuizState.setTextColor(Color.DKGRAY);
        holder.tvQuizState.setBackgroundColor(Color.WHITE);

        final Quiz finalQuiz = quiz;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuizzClick.onQuizzClick(quizList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
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

