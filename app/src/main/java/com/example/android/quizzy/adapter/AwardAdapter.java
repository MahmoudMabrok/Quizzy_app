package com.example.android.quizzy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.model.Award;
import com.example.android.quizzy.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class AwardAdapter extends RecyclerView.Adapter<AwardAdapter.ViewHolder> {

    private List<Award> awardList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.award_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Award award = awardList.get(position);
        holder.tvQuizzNameAWARD.setText(award.getQuizzName());
        holder.tvStudentNameAWARD.setText(award.getStudentName());
    }

    @Override
    public int getItemCount() {
        return awardList.size();
    }

    public void setList(List<Award> list) {
        awardList = new ArrayList<>();
        for (Award award : list) {
            addItem(award);
        }
    }

    private void addItem(Award award) {
        awardList.add(award);
        notifyItemInserted(awardList.size() - 1);
        notifyItemRangeChanged(0, awardList.size());
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvQuizzNameAWARD)
        TextView tvQuizzNameAWARD;
        @BindView(R.id.tvStudentNameAWARD)
        TextView tvStudentNameAWARD;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
