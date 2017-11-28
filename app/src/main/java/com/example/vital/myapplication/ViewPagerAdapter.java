package com.example.vital.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by werton on 28.11.17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(FragmentScrollView.newInstace());
        fragments.add(FragmentLeaders.newInstance());
        fragments.add(FragmentPersonalDate.newInstance());
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
