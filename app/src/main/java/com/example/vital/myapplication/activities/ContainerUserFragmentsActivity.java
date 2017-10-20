package com.example.vital.myapplication.activities;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.vital.myapplication.FragmentLeaders;
import com.example.vital.myapplication.FragmentPersonalDate;
import com.example.vital.myapplication.FragmentScrollView;
import com.example.vital.myapplication.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


public class ContainerUserFragmentsActivity extends AppCompatActivity {

    private BottomBar bottomBar;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityscroll);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setCustomView(R.layout.toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.button_settings, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem item = menu.findItem(R.id.action_settings);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        boolean nav = ViewConfiguration.get(getApplicationContext()).hasPermanentMenuKey();
        if(!nav){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bottomBar.getLayoutParams();
            Resources resources = getApplicationContext().getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            layoutParams.setMargins(0, 0, 0, height);
            bottomBar.setLayoutParams(layoutParams);
        }

//        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelected(@IdRes int tabId) {
//                switch(tabId){
//                    case R.id.tab_home : getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentScrollView()).commit();
//                        item.setEnabled(false);
//                        item.getIcon().setAlpha(0);
//                        break;
//                    case R.id.tab_leaders : getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,new FragmentLeaders()).commit();
//                        item.setEnabled(false);
//                        item.getIcon().setAlpha(0);
//                        break;
//                    case R.id.tab_personaldata : getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragmentPersonalDate()).commit();
//                        item.setEnabled(true);
//                        item.getIcon().setAlpha(230);
//                        break;
//                }
//            }
//        });

        return true;
    }
}

