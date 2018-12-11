package com.example.android.quizzy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.quizzy.R;
import com.example.android.quizzy.interfaces.OnQuizzReportClick;
import com.example.android.quizzy.model.StudentGradeItem;
import com.example.android.quizzy.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class ReportQuizzesDetailTeacherAdapter extends RecyclerView.Adapter<ReportQuizzesDetailTeacherAdapter.ViewHolder> {


    private List<StudentGradeItem> list = new ArrayList<>();
    private OnQuizzReportClick quizzReportClick;

    public ReportQuizzesDetailTeacherAdapter(OnQuizzReportClick context) {
        quizzReportClick = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.studentgradeitwm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        StudentGradeItem item = list.get(position);
        holder.tvSTudentGrade.setText(Constants.gradesAsString[item.getGrade()]);
        holder.tvSTudentName.setText(item.getName());
        holder.tvSTudentPercentage.setText(String.valueOf(item.getPercentage()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizzReportClick.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(StudentGradeItem item) {
        list.add(item);
        notifyItemInserted(list.size() - 1);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSTudentName)
        TextView tvSTudentName;
        @BindView(R.id.tvSTudentGrade)
        TextView tvSTudentGrade;
        @BindView(R.id.tvSTudentPercentage)
        TextView tvSTudentPercentage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
