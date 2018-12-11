package com.example.android.quizzy.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.android.quizzy.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    Resources resources;

    AppIntroFragment page1;
    AppIntroFragment page2;
    AppIntroFragment page3;
    AppIntroFragment page4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = this.getResources();

        page1 = AppIntroFragment.newInstance(resources.getString(R.string.page1_title), resources.getString(R.string.page1_description), R.drawable.teacher, resources.getColor(R.color.colorPrimary));
        page2 = AppIntroFragment.newInstance(resources.getString(R.string.page2_title), resources.getString(R.string.page2_description), R.drawable.teacher, resources.getColor(R.color.colorPrimary));
        page3 = AppIntroFragment.newInstance(resources.getString(R.string.page3_title), resources.getString(R.string.page3_description), R.drawable.teacher, resources.getColor(R.color.colorPrimary));
        page4 = AppIntroFragment.newInstance(resources.getString(R.string.page4_title), resources.getString(R.string.page4_description), R.drawable.teacher, resources.getColor(R.color.colorPrimary));

        //Add pages
        addSlide(page1);
        addSlide(page2);
        addSlide(page3);
        addSlide(page4);

        //Show Skip & Done button
        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        navigateToLoginActivity();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        navigateToLoginActivity();
    }

    private void navigateToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
