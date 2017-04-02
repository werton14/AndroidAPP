package com.example.vital.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;


public class ActivityScroll extends AppCompatActivity {

    private InteractiveScrollView scrollView;
    private LinearLayout scrollLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityscroll);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setCustomView(R.layout.toolbar);

        scrollLayout = (LinearLayout) findViewById(R.id.scrollLayout);
        scrollView = (InteractiveScrollView) findViewById(R.id.scrollview);

        addNewPhoto();
        addNewPhoto();

        scrollView.setOnBottomReachedListener(new InteractiveScrollView.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                addNewPhoto();
            }
        });

    }

    private  void addNewPhoto(){
        ViewGroup container = null;
        Bundle instance = null;
        scrollLayout.addView(new ActivityPrototypeScroll().onCreateView(getLayoutInflater(), container, instance));
    }

}

