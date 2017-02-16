package com.example.vital.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by qwert on 11.02.2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 : return  new FragmentScroll();
            case 1 : return  new FragmentChoose();
            case 2 : return  new FragmentCamera();
        }
        return  null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}