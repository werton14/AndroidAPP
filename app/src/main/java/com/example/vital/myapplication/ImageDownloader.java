package com.example.vital.myapplication;


import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vital.myapplication.activities.Image;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class ImageDownloader {

    private Image image;
    private User user;
    private DatabaseReference imageDbReference;
    private DatabaseReference imageViewsDbReference;
    private FirebaseInfo firebaseInfo;
    private OnDataDownloadedListener onDataDownloadedListener;

    public ImageDownloader(){

        firebaseInfo = FirebaseInfo.getInstance();
    }

    public void findImage(){
        firebaseInfo.getViewsDbReference().orderByChild("time").limitToFirst(2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                DataSnapshot firstSnapshot = iterator.next();
                DataSnapshot secondSnapshot = iterator.next();
                DataSnapshot d;
                if(firstSnapshot.child("view").getValue(long.class) < secondSnapshot.child("view").getValue(long.class)){
                    d = firstSnapshot;
                }else if(firstSnapshot.child("view").getValue(long.class) > secondSnapshot.child("view").getValue(long.class)){
                    d = secondSnapshot;
                }else{
                    d = firstSnapshot;
                }
                imageViewsDbReference = d.getRef();
                String str = imageViewsDbReference.getKey();
                imageDbReference = firebaseInfo.getImagesDbReference().child(str);
                getImage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getImage(){
        imageDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                image = dataSnapshot.getValue(Image.class);
                getUser();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUser(){
        firebaseInfo.getUsersDbReference().child(image.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                iterateImageViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void iterateImageViews(){
        imageViewsDbReference.child("time").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onDataDownloadedListener.onDataDownloaded(image, user);
            }
        });
        imageViewsDbReference.child("view").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                long viewsCount = (long) mutableData.getValue();
                viewsCount++;
                mutableData.setValue(viewsCount);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }


    public OnDataDownloadedListener getOnDataDownloadedListener() {
        return onDataDownloadedListener;
    }

    public void setOnDataDownloadedListener(OnDataDownloadedListener onDataDownloadedListener) {
        this.onDataDownloadedListener = onDataDownloadedListener;
    }


    interface OnDataDownloadedListener{
        public void onDataDownloaded(Image image, User user);
    }
}
