package com.example.vital.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
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

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import static com.example.vital.myapplication.R.id.tab_home;


public class ActivityScroll extends AppCompatActivity {

    private InteractiveScrollView scrollView;
    private LinearLayout scrollLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityscroll);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setCustomView(R.layout.toolbar);

        scrollLayout = (LinearLayout) findViewById(R.id.scrollLayout);
        scrollView = (InteractiveScrollView) findViewById(R.id.scrollview);

        /*BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener(){
            @Override
                public void OnTabSelect(@IdRes int tabId) {                     хэштэг виталя доделай
                if (tabId == R.id.tab_home){

                }
            }
                                         }*/

       /* addNewPhoto();
        addNewPhoto();*/

       /* scrollView.setOnBottomReachedListener(new InteractiveScrollView.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {         Мс курага
                addNewPhoto();
            }
        });*/

    }

    /*private  void addNewPhoto(){
        ViewGroup container = null;
        Bundle instance = null;
        scrollLayout.addView(new ActivityPrototypeScroll().onCreateView(getLayoutInflater(), container, instance));
    }*/

}

