package com.example.vital.myapplication;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

/**
 * Created by qwert on 12.02.2017.
 */

public class FragmentScroll extends Fragment {

    private View view;
    private BottomBar bottomBar;
    private UserPageAdapter userPageAdapter;
    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activityscroll, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        userPageAdapter = new UserPageAdapter(getFragmentManager());

        viewPager = (ViewPager) view.findViewById(R.id.contentContainer);
        viewPager.setAdapter(userPageAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.button_settings, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem item = menu.findItem(R.id.action_settings);
        bottomBar = (BottomBar) view.findViewById(R.id.bottomBar);

        boolean nav = ViewConfiguration.get(getContext()).hasPermanentMenuKey();
        if(!nav){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bottomBar.getLayoutParams();
            Resources resources = getContext().getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            layoutParams.setMargins(0, 0, 0, height);
            bottomBar.setLayoutParams(layoutParams);
        }

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch(tabId){
                    case R.id.tab_home : viewPager.setCurrentItem(0);
                        item.setEnabled(false);
                        item.getIcon().setAlpha(0);
                        break;
                    case R.id.tab_leaders : viewPager.setCurrentItem(1);
                        item.setEnabled(false);
                        item.getIcon().setAlpha(0);
                        break;
                    case R.id.tab_personaldata : viewPager.setCurrentItem(2);
                        item.setEnabled(true);
                        item.getIcon().setAlpha(230);
                        break;
                }
            }
        });
    }
}
