package com.example.android.quizzy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.interfaces.OnQuizzReportClick;
import com.example.android.quizzy.model.NotifactionItem;
import com.example.android.quizzy.model.ReportQuizzItem;
import com.example.android.quizzy.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class ReportQuizzesTeacherAdapter extends RecyclerView.Adapter<ReportQuizzesTeacherAdapter.ViewHolder> {

    private List<ReportQuizzItem> list = new ArrayList<>();
    private OnQuizzReportClick lisnter;

    public ReportQuizzesTeacherAdapter(OnQuizzReportClick lisnter) {
        this.lisnter = lisnter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_quizz_teacher_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ReportQuizzItem item = list.get(position);
        holder.tvReportQuizzName.setText(item.getName());
        holder.tvReportSuccess.setText("" + item.getSuccess());
        holder.tvReportFailed.setText("" + item.getFails());
        holder.tvReportNA.setText("" + item.getNa());

        if (lisnter != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisnter.onClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(ReportQuizzItem item) {
        list.add(item);
        notifyItemInserted(list.size() - 1);
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvReportQuizzName)
        TextView tvReportQuizzName;
        @BindView(R.id.tvReportFailed)
        TextView tvReportFailed;
        @BindView(R.id.tvReportSuccess)
        TextView tvReportSuccess;
        @BindView(R.id.tvReportNA)
        TextView tvReportNA;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
