package com.example.vital.myapplication;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

/**
 * Created by qwert on 4/9/2017.
 */

public class ImageData {

    private DatabaseReference DBReference;
    private DatabaseReference imageIdDBReference;
    private DatabaseReference likeCountDBReference;
    private StorageReference imageStorageIdSReference;
    private DatabaseReference userIdDBReference;
    private DatabaseReference nicknameDBReference;
    private StorageReference profileImageSReference;

    ImageData(){
        DBReference = FirebaseDatabase.getInstance().getReference();
        synchronized (this) {
            DBReference.child("views").orderByValue().limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    imageIdDBReference = dataSnapshot.getChildren().iterator().next().getRef();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }
}
