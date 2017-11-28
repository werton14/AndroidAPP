package com.example.vital.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.FeatureInfo;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwert on 11.02.2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
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
                        if(taskNumber == user.getTaskNumber()){

                            fragments.remove(fragments.size() - 1);
                            fragments.add(new FragmentMakedPicture());
                        } else {
                            fragments.remove(fragments.size() - 1);
                            fragments.add(FragmentCamera.newInstance());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}