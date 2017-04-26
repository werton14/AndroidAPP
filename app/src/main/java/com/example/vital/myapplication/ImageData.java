package com.example.vital.myapplication;

import android.net.Uri;
import android.os.Looper;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.CountDownLatch;
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

        getImageId();
        imageIdDBReference = DBReference.child("image").child(getImageId());
        likeCountDBReference = imageIdDBReference.child("likesCount");
        userIdDBReference = DBReference.child("users").child(getUserId());

        imageIdSReference = SReference.child("image").child(getImageFileName());
        profileImageSReference = SReference.child("userProfileImage").child(getProfileImageFileName());

    }

    private String getImageId() {
        final CountDownLatch latch = new CountDownLatch(1);
        final String res[] = new String[1];
        res[0] = null;
        Query query = DBReference.child("views").orderByValue().limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                res[0] = dataSnapshot.getChildren().iterator().next().getRef().getKey();
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res[0];

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
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] res = new String[1];
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    res[0] = dataSnapshot.getValue(String.class);
                    latch.countDown();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res[0];
    }

    private Uri getUriFromStorage(StorageReference reference){
        final CountDownLatch latch = new CountDownLatch(1);
        final Uri res[] = new Uri[1];
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                res[0] = uri;
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res[0];
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
