package com.example.vital.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by werton on 28.11.17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(FragmentScrollView.newInstance());
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
