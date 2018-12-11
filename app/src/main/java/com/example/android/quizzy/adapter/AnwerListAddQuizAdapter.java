package com.example.android.quizzy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.android.quizzy.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class AnwerListAddQuizAdapter extends RecyclerView.Adapter<AnwerListAddQuizAdapter.ViewHolder> {

    private List<String> anserList;
    private String correct;

    public AnwerListAddQuizAdapter() {
        anserList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anser_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String s = anserList.get(position);
        holder.tvAnswer.setText(s);

        if (s.equals(correct)) {
            holder.cbAnswer.setChecked(true);
        } else {
            holder.cbAnswer.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cbAnswer.isChecked()) {
                    holder.cbAnswer.setChecked(false);
                } else {
                    holder.cbAnswer.setChecked(true);
                    correct = anserList.get(position);
                }
                notifyDataSetChanged();
            }
        });

        holder.cbAnswer.setClickable(false);

    }

    public String getCorrect() {
        return correct;
    }

    public List<String> getAnserList() {
        return anserList;
    }


    @Override
    public int getItemCount() {
        return anserList.size();
    }

    public void add(String s) {
        anserList.add(s);
        notifyItemInserted(anserList.size() - 1);
    }

    public void deleteAll() {
        anserList.clear();
        notifyDataSetChanged();
    }

    public void setList(List<String> answerList) {
        if (answerList != null) {
            for (String s : answerList) {
                add(s);
            }
        }

    }

    public void remove(int pos) {
        String anser = anserList.get(pos);
        anserList.remove(pos);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvAnswer)
        TextView tvAnswer;

        @BindView(R.id.cbAnswer)
        CheckBox cbAnswer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
