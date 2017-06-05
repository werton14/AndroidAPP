package com.example.vital.myapplication;


import com.example.vital.myapplication.activities.Image;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ImageDownloader {

    private ArrayDeque<List<Object>> imageQuere
    private FirebaseInfo firebaseInfo;
    private OnSuccessListener<Void> onTimestampUpdate;

    private int unUpdatedTimestampCount = 0;

    public ImageDownloader(){

        firebaseInfo = FirebaseInfo.getInstance();

        onTimestampUpdate = new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                unUpdatedTimestampCount--;
                if(unUpdatedTimestampCount == 0) findImage();
            }
        };
    }

    public void findImage(){
        firebaseInfo.getViewsDbReference().orderByChild("time").limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                List<DataSnapshot> snapshots = new ArrayList<DataSnapshot>();
                iterator.forEachRemaining(snapshots::add);
                Collections.sort(snapshots, new Comparator<DataSnapshot>() {
                    @Override
                    public int compare(DataSnapshot o1, DataSnapshot o2) {
                        return o1.child("view").getValue(long.class) > o2.child("view").getValue(long.class) ? -1 :
                                (o1.child("view").getValue(long.class) < o2.child("view").getValue(long.class)) ? 1 : 0;
                    }
                });

                List<DatabaseReference> imageDbReferenceList = new ArrayList<DatabaseReference>();
                for(int i = 0; i < imageDbReferenceList.size() / 2; i++){
                    unUpdatedTimestampCount++;
                    DatabaseReference imageViewsDbReference = snapshots.get(i).getRef();
                    imageViewsDbReference.child("time").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(onTimestampUpdate);
                    iterateImageViews(imageViewsDbReference);
                    String str = imageViewsDbReference.getKey();
                    imageDbReferenceList.add(firebaseInfo.getImagesDbReference().child(str);
                }
                getImage(imageDbReferenceList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getImage(List<DatabaseReference> imageDbReferenceList){
        for(DatabaseReference imageDbReference: imageDbReferenceList) {
            imageDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Image image = dataSnapshot.getValue(Image.class);
                    getUser(image);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void getUser(Image image){
        firebaseInfo.getUsersDbReference().child(image.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void iterateImageViews(DatabaseReference imageViewsDbReference){
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



}
