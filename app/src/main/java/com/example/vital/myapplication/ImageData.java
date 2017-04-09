package com.example.vital.myapplication;

import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by qwert on 4/9/2017.
 */

public class ImageData {

    private DatabaseReference DBReference;
    private StorageReference SReference;
    private DatabaseReference imageIdDBReference;
    private DatabaseReference likeCountDBReference;
    private StorageReference imageIdSReference;
    private DatabaseReference userIdDBReference;
    private DatabaseReference nicknameDBReference;
    private StorageReference profileImageSReference;


    ImageData(){
        DBReference = FirebaseDatabase.getInstance().getReference();
        SReference = FirebaseStorage.getInstance().getReference();
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

        likeCountDBReference = imageIdDBReference.child("likesCount");
        imageIdSReference = SReference.child("image").child(getImageFileName());
        userIdDBReference = DBReference.child("users").child(getUserId());
        nicknameDBReference = userIdDBReference.child("username");
        profileImageSReference = SReference.child("userProfileImage").child(getProfileImageFileName());

    }

    private String getImageFileName(){
        return null;
    }

    private String getUserId(){
        return null;
    }

    private String getProfileImageFileName(){
        return  null;
    }

    public String getNicname(){
        return  null;
    }

    public String getLikeCount() {
        return null;
    }

    public Uri getImageUri(){
        return null;
    }

    public Uri getProfileImageUri() {
        return null;
    }

    public boolean isLike(){
        return false;
    }

    public void like(){

    }

}
