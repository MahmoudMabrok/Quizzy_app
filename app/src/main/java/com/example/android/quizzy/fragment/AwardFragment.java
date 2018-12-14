package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.quizzy.R;
import com.example.android.quizzy.adapter.AwardAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.model.Award;
import com.example.android.quizzy.util.Constants;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AwardFragment extends Fragment {


    @BindView(R.id.spin_kit_award)
    SpinKitView spinKitAward;
    @BindView(R.id.tvNoDataAward)
    TextView tvNoDataAward;
    @BindView(R.id.rvAward)
    RecyclerView rvAward;
    Unbinder unbinder;
    private AwardAdapter adapter;
    private DataRepo repo;
    private String teacherUUID;

    public AwardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            teacherUUID = bundle.getString(Constants.TEACHER_TELEPHONE_NUMBER_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_award, container, false);
        unbinder = ButterKnife.bind(this, view);
        loadingState();
        initRv();
        loadData();
        return view;
    }

    private void initRv() {
        adapter = new AwardAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvAward.setLayoutManager(manager);
        rvAward.setAdapter(adapter);
    }


    private void loadData() {
        repo = new DataRepo();
        DatabaseReference reference = repo.getAwradRef(teacherUUID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isResumed()) {
                    List<Award> awardList = new ArrayList<>();
                    // Award award ;
                    for (DataSnapshot quizSnap : dataSnapshot.getChildren()) { // each one is quizz has award childeren
                        for (DataSnapshot awardSnap : quizSnap.getChildren()) { // each one is item of quizz
                            awardList.add(awardSnap.getValue(Award.class));
                        }
                    }

                    if (awardList.size() > 0) {
                        loadedWithDataState();
                        adapter.setList(awardList);
                    } else {
                        loadedWithoutDataState();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void loadingState() {
        controlRv(View.GONE);
        controlTV(View.GONE);
        controlSpin(View.VISIBLE);

    }

    private void loadedWithDataState() {
        controlRv(View.VISIBLE);
        controlTV(View.GONE);
        controlSpin(View.GONE);

    }

    private void loadedWithoutDataState() {
        controlRv(View.GONE);
        controlTV(View.VISIBLE);
        controlSpin(View.GONE);
    }

    private void controlRv(int state) {
        rvAward.setVisibility(state);
    }

    private void controlTV(int state) {
        tvNoDataAward.setVisibility(state);
    }

    private void controlSpin(int visible) {
        spinKitAward.setVisibility(visible);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
