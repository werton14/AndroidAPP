package com.example.vital.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by qwert on 11.02.2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private OnFragmentScrollCreatedListener fragmentScrollCreatedListener;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 : FragmentScroll fragmentScroll = FragmentScroll.newInstance();
                fragmentScrollCreatedListener.onFragmentScrollCreated(fragmentScroll);
                return fragmentScroll;
            case 1 :  return FragmentChoose.newInstance();
            case 2 :  return FragmentCamera.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    public interface OnFragmentScrollCreatedListener {
        void onFragmentScrollCreated(FragmentScroll fragment);
    }

    public void setFragmentScrollCreatedListener(OnFragmentScrollCreatedListener fragmentScrollCreatedListener) {
        this.fragmentScrollCreatedListener = fragmentScrollCreatedListener;
    }
}