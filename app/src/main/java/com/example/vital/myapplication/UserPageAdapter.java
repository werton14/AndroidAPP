package com.example.vital.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by werton on 13.10.17.
 */

public class UserPageAdapter extends FragmentPagerAdapter {
    FragmentManager fragmentManager;
    public UserPageAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 : return  FragmentScrollView.newInstace();
            case 1 : return  FragmentLeaders.newInstance();
            case 2 : return  FragmentPersonalDate.newInstace();
        }
        return  null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}