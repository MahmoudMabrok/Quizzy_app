package com.example.android.quizzy.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.example.android.quizzy.R;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughActivity;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughCard;

import java.util.ArrayList;
import java.util.List;

public class  WalkThroughActivty extends FancyWalkthroughActivity {

    Resources resources;

    FancyWalkthroughCard page1;
    FancyWalkthroughCard page2;
    FancyWalkthroughCard page3;
    FancyWalkthroughCard page4;

    List<FancyWalkthroughCard> pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = this.getResources();

        page1 = new FancyWalkthroughCard(resources.getString(R.string.page1_title), resources.getString(R.string.page1_description));
        page2 = new FancyWalkthroughCard(resources.getString(R.string.page2_title), resources.getString(R.string.page2_description));
        page3 = new FancyWalkthroughCard(resources.getString(R.string.page3_title), resources.getString(R.string.page3_description));
        page4 = new FancyWalkthroughCard(resources.getString(R.string.page4_title), resources.getString(R.string.page4_description));

        pages = new ArrayList<>();

        //Add cards to the list
        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        pages.add(page4);

        setOnboardPages(pages);

        setImageBackground(R.drawable.teacher);
    }

    @Override
    public void onFinishButtonPressed() {
        //Navigate to Login Activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
