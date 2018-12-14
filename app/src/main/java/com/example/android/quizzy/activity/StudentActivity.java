package com.example.android.quizzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.fragment.AwardFragment;
import com.example.android.quizzy.fragment.StudentReports;
import com.example.android.quizzy.fragment.student_quiz_list;
import com.example.android.quizzy.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.containerStudent)
    FrameLayout containerStudent;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    FragmentManager manager = getSupportFragmentManager();
    private FragmentTransaction transition;

    public String studentID;
    public String studentName;
    public String teacherUUID;

    private static final String TAG = "StudentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

 //       checkLoginState(); // used to check state of curent user
      //  studentID = FirebaseAuth.getInstance().getCurrentUser().getUid();
     //   show(studentID);
      //  studentID = "9lHyq4mnSaTd1cURH5v5jGm52Mw1";
        studentID = "EHefJOONtBO6fU6GVHVpAjHnoa92";
       // Log.d(TAG, "onCreate: " + studentID);
        Intent intent = getIntent();
        studentName = intent.getStringExtra(Constants.STUDENT_NAME_KEY);
        teacherUUID = intent.getStringExtra(Constants.TEACHER_TELEPHONE_NUMBER_KEY);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);

        TextView textView = navView.getHeaderView(0).findViewById(R.id.nav_user_name);
        textView.setText(studentName);
        openQuizzListFragment();

    }

    private void checkLoginState() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            openMainActivity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            getPreferences(MODE_PRIVATE).edit().clear().apply();
            openMainActivity();
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_quiz:
                openQuizzListFragment();
                break;

            case R.id.nav_reorts:
                openReports();
                break;
            case R.id.nav_award:
                openAward();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openQuizzListFragment() {
        transition = manager.beginTransaction();
        transition.setCustomAnimations(R.anim.slide_up, 0);
        student_quiz_list teacher = new student_quiz_list();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.STUDENT_UUID, studentID);
        bundle.putString(Constants.STUDENT_NAME, studentName);
        bundle.putString(Constants.STUDENT_Teacher_uuid, teacherUUID);
        teacher.setArguments(bundle);
        transition.replace(R.id.containerStudent, teacher).commit();
        //  transition.addToBackStack(student_quiz_list.TAG);
    }


    private void openAward() {
        transition = manager.beginTransaction();
        AwardFragment awardFragment = new AwardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TEACHER_TELEPHONE_NUMBER_KEY, teacherUUID);
        awardFragment.setArguments(bundle);
        transition.replace(R.id.containerStudent, awardFragment).commit();
        transition.addToBackStack(null);
    }

    private void openReports() {
        transition = manager.beginTransaction();
        StudentReports studentReports = new StudentReports();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.STUDENT_NAME, studentName);
        bundle.putString(Constants.STUDENT_Teacher_uuid, teacherUUID);
        bundle.putString(Constants.STUDENT_UUID, studentID);
        studentReports.setArguments(bundle);
        transition.replace(R.id.containerStudent, studentReports).commit();
        transition.addToBackStack(StudentReports.TAG);
    }


    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void show(String studentName) {
        Toast.makeText(this, studentName, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }

            //      super.onBackPressed();
        }
    }



}
