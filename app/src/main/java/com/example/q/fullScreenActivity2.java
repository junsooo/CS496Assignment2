package com.example.q.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

/**
 * Created by q on 2017-01-02.
 */

public class fullScreenActivity2 extends Activity {

    private fullScreenAdapter2 adapter;
    private ViewPager viewPager;
    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        activity=this;
        viewPager = (ViewPager) findViewById(R.id.pager);
        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        adapter = new fullScreenAdapter2(this);

        viewPager.setAdapter(adapter);
        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}