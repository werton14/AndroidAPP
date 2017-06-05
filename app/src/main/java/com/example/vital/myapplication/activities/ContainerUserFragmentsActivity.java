package com.example.vital.myapplication.activities;

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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.vital.myapplication.FragmentLeaders;
import com.example.vital.myapplication.FragmentPersonalDate;
import com.example.vital.myapplication.FragmentScrollView;
import com.example.vital.myapplication.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import static com.example.vital.myapplication.R.id.tab_home;


public class ContainerUserFragmentsActivity extends AppCompatActivity {

    private BottomBar bottomBar;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityscroll);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setCustomView(R.layout.toolbar);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch(tabId){
                    case R.id.tab_home : getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentScrollView()).commit();
                        break;
                    case R.id.tab_leaders : getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,new FragmentLeaders()).commit();
                        break;
                    case R.id.tab_personaldata : getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentPersonalDate()).commit();
                        break;
                }
            }
        });
    }
}

