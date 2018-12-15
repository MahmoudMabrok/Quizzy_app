package com.example.android.quizzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.util.Constants;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Entry extends AppCompatActivity {

    @BindView(R.id.btnIoenTeacher)
    Button btnIoenTeacher;
    @BindView(R.id.btnIoenStudent)
    Button btnIoenStudent;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);

    }

    String name = "Mahmoud";
    String teacherID = "0114919427";


    String eqmilStudent = "d@gmail.com";
    String eqmilTeacher = "aa@gmail.com";
    String pass = "123456";

    @Override
    protected void onResume() {
        super.onResume();/*

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(eqmil , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String uuid = task.getResult().getUser().getUid() ;
                    HashMap<String , String> data = new HashMap<>();
                    data.put(Constants.STUDENT_NAME , name);
                    data.put(Constants.STUDENT_UUID , uuid);
                    data.put(Constants.STUDENT_Teacher_uuid , teacherID);
                    FirebaseDatabase.getInstance().getReference()
                            .child(Constants.USERS_KEY)
                            .child(Constants.STUDENTS_KEY)
                            .child(uuid)
                            .setValue(data);
                     FirebaseDatabase.getInstance().getReference()
                            .child(Constants.USERS_KEY).child(Constants.TEACHERS_KEY).child(teacherID).child(Constants.STUDENTS_KEY).child(uuid).setValue(data);

                }
            }
        });
*/
        /*FirebaseAuth.getInstance().signInWithEmailAndPassword(eqmil , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Entry.this, "LOGGED", Toast.LENGTH_SHORT).show();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY).child(Constants.STUDENTS_KEY).child(task.getResult().getUser().getUid());
                    Disposable subscribe = RxFirebaseDatabase.observeSingleValueEvent(reference).subscribe(e -> {
                        Toast.makeText(Entry.this, "GETvalue", Toast.LENGTH_SHORT).show();
                        String Student_name = (String) e.child(Constants.STUDENT_NAME).getValue();
                        String TeacherUUID = (String) e.child(Constants.STUDENT_Teacher_uuid).getValue();
                        Intent intent = new Intent(Entry.this  ,StudentActivity.class);
                        intent.putExtra(Constants.STUDENT_NAME , Student_name);
                        intent.putExtra(Constants.STUDENT_Teacher_uuid , TeacherUUID);
                        startActivity(intent);

                    });


                }   else{
                    Toast.makeText(Entry.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/

    }

    @OnClick(R.id.btnIoenTeacher)
    public void onBtnIoenTeacherClicked() {
        String TeacherTelephone = "011";
        name = "MR mabrok";
        Intent intent = new Intent(Entry.this, TeacherHome.class);
        intent.putExtra(Constants.TELEPHONE_NUMBER_KEY, TeacherTelephone);
        intent.putExtra(Constants.TEACHER_NAME, name);
        startActivity(intent);

/*
        FirebaseAuth.getInstance().signInWithEmailAndPassword(eqmilTeacher , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                }   else{
                    Toast.makeText(Entry.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
    }

    @OnClick(R.id.btnIoenStudent)
    public void onBtnIoenStudentClicked() {
        String TeacherTelephone = "011";
        name = "Mahmoud";
        Intent intent = new Intent(Entry.this, StudentActivity.class);
        intent.putExtra(Constants.TEACHER_TELEPHONE_NUMBER_KEY, TeacherTelephone);
        intent.putExtra(Constants.STUDENT_NAME_KEY, name);
        startActivity(intent);

    }

    private void showSpin() {
        spinKit.setVisibility(View.VISIBLE);
    }
}
