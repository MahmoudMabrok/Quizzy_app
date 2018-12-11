package com.example.android.quizzy.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.model.NotifactionItem;
import com.example.android.quizzy.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class NotifactionAdapter extends RecyclerView.Adapter<NotifactionAdapter.ViewHolder> {

    private List<NotifactionItem> NotificationList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final NotifactionItem notifactionItem = NotificationList.get(position);
        StringBuilder builder = new StringBuilder();
        builder.append(notifactionItem.getStudentName());
        builder.append(" has solved  ");
        builder.append(notifactionItem.getQuizzName());
        builder.append("  ");
        builder.append("with Grade ");

        builder.append(Constants.gradesAsString[notifactionItem.getGrade()]);
        holder.tvNotification.setText(builder.toString());

        if (notifactionItem.isSeen()) {
            holder.tvNotification.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.viewed));
        } else {
            holder.tvNotification.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.not_viewed));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "AAA", Toast.LENGTH_SHORT).show();
                //// TODO: 11/23/2018  call repo
                new DataRepo().addNotification(notifactionItem);
                //// TODO: 11/23/2018  open Quizz Question 
            }
        });

    }

    @Override
    public int getItemCount() {
        return NotificationList.size();
    }

    public void add(NotifactionItem item) {
        NotificationList.add(0, item);
        notifyItemInserted(0);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvNotification)
        TextView tvNotification;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
