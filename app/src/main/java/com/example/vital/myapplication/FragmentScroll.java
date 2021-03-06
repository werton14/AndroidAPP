package com.example.vital.myapplication;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

public class FragmentScroll extends Fragment {

    private RelativeLayout frameToolbar;
    private View view;
    private MyBottomNavigationView bottomBar;
    private UserPageAdapter userPageAdapter;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private CustomViewPager viewPager;
    private ImageButton imageButton;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activityscroll2, container, false);
        frameToolbar = (RelativeLayout) view.findViewById(R.id.frameToolbar);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        imageButton = (ImageButton) getActivity().findViewById(R.id.settings);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        bottomBar = (MyBottomNavigationView) view.findViewById(R.id.bottomBar);
        viewPager = (CustomViewPager) view.findViewById(R.id.contentContainer);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.getMenu().getItem(position).setChecked(true);
//                switch (position){
//                    case 0:
//
//                        break;
//
//                    case 1:
//
//                        break;
//
//                    case 2:
//
////                        imageButton.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        boolean nav = ViewConfiguration.get(getActivity().getApplicationContext()).hasPermanentMenuKey();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bottomBar.getLayoutParams();

        Log.w("loh", String.valueOf(nav));
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
                switch (item.getItemId()){
                    case R.id.home:
                        if(viewPager.getCurrentItem() == 0) {
                            FragmentScrollView fragmentScrollView =
                                    (FragmentScrollView)viewPagerAdapter.getItem(0);
                            fragmentScrollView.scrollToTop();
                        } else {
                            viewPager.setCurrentItem(0);
                        }

                        break;
                    case R.id.leaders:
                        if(viewPager.getCurrentItem() == 1) {
                            FragmentLeaders fragmentLeaders =
                                    (FragmentLeaders) viewPagerAdapter.getItem(1);
                            fragmentLeaders.scrollToTop();
                        } else {
                            viewPager.setCurrentItem(1);
                        }
                        break;
                    case  R.id.data:
                        viewPager.setCurrentItem(2);
                        break;
                }
                item.setChecked(true);
                return false;
            }
        });


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    72);   //можно на один пониить
            frameToolbar.setLayoutParams(params);
        }

        return view;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.button_settings, menu);
//    }

    public static FragmentScroll newInstance(){
        return new FragmentScroll();
    }

}
