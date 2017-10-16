package com.example.vital.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by qwert on 11.02.2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private OnFragmentCreatedListener fragmentCreatedListener;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0 : fragment = FragmentScroll.newInstance();
                break;
            case 1 : fragment = FragmentChoose.newInstance();
                break;
            case 2 : fragment = FragmentCamera.newInstance();
                break;
        }
        fragmentCreatedListener.onFragmentCreated(fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }


    public interface OnFragmentCreatedListener{
        void onFragmentCreated(Fragment fragment);
    }

    public void setFragmentCreatedListener(OnFragmentCreatedListener fragmentCreatedListener) {
        this.fragmentCreatedListener = fragmentCreatedListener;
    }
}