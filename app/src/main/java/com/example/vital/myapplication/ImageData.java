package com.example.vital.myapplication;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.Exchanger;

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
    private StorageReference profileImageSReference;



    ImageData(){

        DBReference = FirebaseDatabase.getInstance().getReference();
        SReference = FirebaseStorage.getInstance().getReference();

        imageIdDBReference = DBReference.child("image").child(getImageId());
        likeCountDBReference = imageIdDBReference.child("likesCount");
        userIdDBReference = DBReference.child("users").child(getUserId());

        imageIdSReference = SReference.child("image").child(getImageFileName());
        profileImageSReference = SReference.child("userProfileImage").child(getProfileImageFileName());

    }

    private String getImageId(){
        final Exchanger<String> res = new Exchanger<>();
        DBReference.child("views").orderByValue().limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    res.exchange(dataSnapshot.getChildren().iterator().next().getRef().getKey());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        try {
            return res.exchange(null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getImageFileName(){
        return getDataFromDB(imageIdDBReference.child("photoStorageId"));
    }

    private String getUserId(){
        return getDataFromDB(imageIdDBReference.child("userId"));
    }

    private String getProfileImageFileName(){
        return getDataFromDB(userIdDBReference.child("profilePhotoUrl"));
    }

    private String getDataFromDB(DatabaseReference reference){
        final Exchanger<String> res = new Exchanger<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    res.exchange(dataSnapshot.getValue(String.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        try {
            return res.exchange(null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Uri getUriFromStorage(StorageReference reference){
        final Exchanger<Uri> res = new Exchanger<>();
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    res.exchange(uri);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            return res.exchange(null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNickname(){
        return getDataFromDB(userIdDBReference.child("username"));
    }

    public String getLikeCount() {
        return getDataFromDB(likeCountDBReference);
    }

    public Uri getImageUri(){
        return getUriFromStorage(imageIdSReference);
    }

    public Uri getProfileImageUri() {
        return getUriFromStorage(profileImageSReference);
    }

    public boolean isLike(){
        return false;
    }

    public void like(){

    }

}
