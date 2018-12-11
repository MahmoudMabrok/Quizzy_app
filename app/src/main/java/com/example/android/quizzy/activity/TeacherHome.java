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
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.fragment.NotificationFragment;
import com.example.android.quizzy.fragment.QuizzDetailTeacherReport;
import com.example.android.quizzy.fragment.QuizzListTeacher;
import com.example.android.quizzy.fragment.ReportsTeacherFragment;
import com.example.android.quizzy.fragment.ShowSolvedQuiz;
import com.example.android.quizzy.model.AttemptedQuiz;
import com.example.android.quizzy.model.Data;
import com.example.android.quizzy.util.Constants;
import com.example.android.quizzy.util.Utils;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeacherHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transition;

    public Data dataSendedToQuizDetail;
    public String name = "AAA";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teacher_home, menu);
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

    public static final String TAG = "TeacherHome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        ButterKnife.bind(this);

        key = getIntent().getStringExtra(Constants.TEACHER_TELEPHONE_NUMBER_KEY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        openQuizzListFragment();

        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        textView.setText(key);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else {
            super.onBackPressed();
        }
        show("" + getFragmentManager().getBackStackEntryCount());
    }

    private void show(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private String key = "0114919427";

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

            case R.id.nav_notification:
                openNotification();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openNotification() {
        transition = manager.beginTransaction();
        transition.setCustomAnimations(R.anim.slide_up, 0);
        NotificationFragment teacher = new NotificationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TEACHERS_KEY, key);
        teacher.setArguments(bundle);

        transition.replace(R.id.container, teacher).commit();
    }

    private void openQuizzListFragment() {
        transition = manager.beginTransaction();
        transition.setCustomAnimations(R.anim.slide_up, 0);
        QuizzListTeacher teacher = new QuizzListTeacher();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TEACHERS_KEY, key);
        teacher.setArguments(bundle);
        transition.replace(R.id.container, teacher).commit();

    }

    private void openReports() {
        transition = manager.beginTransaction();
        transition.setCustomAnimations(R.anim.slide_up, 0);
        ReportsTeacherFragment teacher = new ReportsTeacherFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TEACHERS_KEY, key);
        teacher.setArguments(bundle);
        transition.replace(R.id.container, teacher).commit();
        transition.addToBackStack(ReportsTeacherFragment.TAG);
    }

    public void openQuizzDetail(Data data) {
        dataSendedToQuizDetail = data;
        transition = manager.beginTransaction();
        transition.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        QuizzDetailTeacherReport teacher = new QuizzDetailTeacherReport();
        transition.replace(R.id.container, teacher).commit();
        transition.addToBackStack(null);
    }

    public AttemptedQuiz attemptedQuiz;

    public void openSolvedQuizz(AttemptedQuiz quiz) {
        attemptedQuiz = quiz;
        transition = manager.beginTransaction();
        transition.setCustomAnimations(0, R.anim.slide_down);
        ShowSolvedQuiz teacher = new ShowSolvedQuiz();
        transition.replace(R.id.container, teacher).commit();
        transition.addToBackStack(null);

    }


    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
