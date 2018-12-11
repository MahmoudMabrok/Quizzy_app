package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.StudentActivity;
import com.example.android.quizzy.adapter.NotifactionAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.model.NotifactionItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


    @BindView(R.id.recyclerViewNotivation)
    RecyclerView recyclerViewNotivation;
    Unbinder unbinder;

    private DataRepo dataRepo = new DataRepo();

    public NotificationFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "NotificationFragment";
    private NotifactionAdapter adapter = new NotifactionAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRv();
        return view;
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerViewNotivation.setLayoutManager(manager);
        recyclerViewNotivation.setAdapter(adapter);

        dataRepo.getNotificationRef(dataRepo.getUUID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<NotifactionItem> list = new ArrayList<>();
                NotifactionItem item;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    item = dataSnapshot1.getValue(NotifactionItem.class);
                    if (item != null) {
                        list.add(item);
                    }
                }
                for (NotifactionItem item1 : list) {
                    adapter.add(item1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
