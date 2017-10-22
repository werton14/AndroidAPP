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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FragmentScroll extends Fragment {

    private FrameLayout frameToolbar;
    private View view;
    private MyBottomNavigationView bottomBar;
    private UserPageAdapter userPageAdapter;
    private ViewPager.OnPageChangeListener onPageChangeListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activityscroll2, container, false);
        frameToolbar = (FrameLayout) view.findViewById(R.id.frameToolbar);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        bottomBar = (MyBottomNavigationView) view.findViewById(R.id.bottomBar);

        getFragmentManager().beginTransaction()
                .replace(R.id.contentContainer, FragmentScrollView.newInstace()).commit();
        boolean nav = ViewConfiguration.get(getContext()).hasPermanentMenuKey();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bottomBar.getLayoutParams();
        if(!nav){
            Resources resources = getContext().getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            layoutParams.setMargins(0, 0, 0, height);
        }

         int height = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 47, getResources().getDisplayMetrics());
        bottomBar.setLayoutParams(layoutParams);
        layoutParams.height = height;
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                changeFragment(item.getItemId());
                item.setChecked(true);
                return false;
            }
        });


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            frameToolbar.setLayoutParams(params);
        }

        return view;
    }

    private void changeFragment(int id) {
        Fragment fragment = null;
        switch (id){
            case R.id.home:
                fragment = FragmentScrollView.newInstace();
                break;
            case R.id.leaders:
                fragment = FragmentLeaders.newInstance();
                break;
            case  R.id.data:
                fragment = FragmentPersonalDate.newInstance();
                break;
        }
        getFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment).commit();
    }
    public static FragmentScroll newInstance(){
        return new FragmentScroll();
    }

}
