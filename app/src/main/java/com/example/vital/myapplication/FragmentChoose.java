package com.example.vital.myapplication;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class FragmentChoose extends Fragment {

    private FirebaseInfo firebaseInfo;
    private User currentUser;
    private String task;
    private int taskNumber;
    private boolean taskIsCompleted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragmentchoose, container, false);

        firebaseInfo = FirebaseInfo.getInstance();

        getUser();

        return  rootView;
    }

    public static FragmentChoose newInstance(){
        return new FragmentChoose();
    }

    private  void getUser(){
        DatabaseReference usersReference = firebaseInfo.getUsersDbReference();
        String currentUserId = firebaseInfo.getCurrentUserId();
        usersReference.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                getTask();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private  void getTask(){
        final DatabaseReference taskReference = firebaseInfo.getTaskDbReference();
        taskReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                task = dataSnapshot.child("text").getValue(String.class);
                taskNumber = dataSnapshot.child("number").getValue(int.class);
                if(currentUser != null && taskNumber == currentUser.getTaskNumber()){
                    taskIsCompleted = true;
                }else{
                    taskIsCompleted = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
