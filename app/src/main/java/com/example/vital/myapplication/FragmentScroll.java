package com.example.vital.myapplication;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by qwert on 12.02.2017.
 */

public class FragmentScroll extends Fragment {

    private LinearLayout linearLayout;
    private View view;
    private MyBottomNavigationView bottomBar;
    private UserPageAdapter userPageAdapter;
    private ViewPager.OnPageChangeListener onPageChangeListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activityscroll, container, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayoutScroll);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        bottomBar = (MyBottomNavigationView) view.findViewById(R.id.bottomBar);
        getFragmentManager().beginTransaction()
                .replace(R.id.contentContainer, FragmentScrollView.newInstace()).commit();
        boolean nav = ViewConfiguration.get(getContext()).hasPermanentMenuKey();

        if(!nav){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bottomBar.getLayoutParams();
            Resources resources = getContext().getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            layoutParams.setMargins(0, 0, 0, height);
            bottomBar.setLayoutParams(layoutParams);
        }

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                changeFragment(item.getItemId());
                return false;
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(params);
        }

        return view;
    }

    private void changeFragment(int id) {
        Fragment fragment = null;
        switch (id){
            case R.id.homee:
                fragment = FragmentScrollView.newInstace();
                break;
            case R.id.leadeers:
                fragment = FragmentLeaders.newInstance();
                break;
            case  R.id.dataa:
                fragment = FragmentPersonalDate.newInstance();
                break;
        }
        getFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment).commit();
    }
    public static FragmentScroll newInstance(){
        return new FragmentScroll();
    }

}
