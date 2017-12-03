package com.example.vital.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.FeatureInfo;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwert on 11.02.2017.
 */

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments = new ArrayList<>();
    FragmentManager fragmentManager;
    Fragment lastFragment = null;

    public SectionsPagerAdapter(FragmentManager fm, ViewPager viewPager) {
        super(fm);
        this.fragmentManager = fm;

        fragments.add(FragmentScroll.newInstance());
        fragments.add(FragmentChoose.newInstance());
        fragments.add(FragmentCamera.newInstance());



        FirebaseInfo.getInstance().getCurrentUserDbReference()
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                checkUserCompleteTask(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public Fragment getItem(int position) {
        Log.w("hitrov", String.valueOf(position));
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    private void checkUserCompleteTask(final User user){
        FirebaseInfo.getInstance().getDatabaseReference().child("task").child("number")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int taskNumber = dataSnapshot.getValue(int.class);
                        lastFragment = fragments.get(2);
                        if(taskNumber == user.getTaskNumber()){
                            Log.w("hitrov:taskNumber", String.valueOf(taskNumber));

                            fragments.remove(fragments.size() - 1);
                            fragments.add(new FragmentMakedPicture());
                        } else {
                            fragments.remove(fragments.size() - 1);
                            fragments.add(FragmentCamera.newInstance());
                        }

                        notifyDataSetChanged();
                       /* FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.remove(lastFragment);
                        fragmentTransaction.add(R.id.container, fragments.get(2));
                        fragmentTransaction.commit();*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemPosition(Object object) {
        int index = fragments.indexOf(object);

        if(index == -1){
            return PagerAdapter.POSITION_NONE;
        } else {
            return index;
        }
    }
}