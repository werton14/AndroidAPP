package com.example.vital.myapplication;


import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.IOException;

public class FragmentPrototypeScroll extends Fragment {

    private ImageButton userProfileImageButton;
    private TextView userNicknameTextView;
    private ImageView currentImage;
    private ImageButton likeImageButton;
    private TextView likeTextView;
    private ImageButton downloadPicture;
    private View view;

    private FirebaseInfo firebaseInfo;
    private DatabaseReference userDbReference;
    private DatabaseReference imageDbReference;
    private DatabaseReference imageViewsDbReference;
    private StorageReference imageSReference;
    private StorageReference profileImageSReference;
    private String nickname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_prototype_scroll, container, false);

        userProfileImageButton = (ImageButton) view.findViewById(R.id.userProfilePicture);
        userNicknameTextView = (TextView) view.findViewById(R.id.userNickname);
        currentImage = (ImageView) view.findViewById(R.id.currentPhoto);
        likeImageButton = (ImageButton) view.findViewById(R.id.likeImageButton);
        likeTextView = (TextView) view.findViewById(R.id.likeTextView);
        downloadPicture = (ImageButton) view.findViewById(R.id.downloadImageButton);
        firebaseInfo = FirebaseInfo.getInstance();

        findImage();

        return view;
    }

    private void findImage(){
        firebaseInfo.getViewsDbReference().orderByValue().limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageViewsDbReference = dataSnapshot.getChildren().iterator().next().getRef();
                String str = imageViewsDbReference.getKey();
                imageDbReference = firebaseInfo.getImagesDbReference().child(str);
                getUser();
                getImage();
                iterateImageView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUser(){
        imageDbReference.child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String str = dataSnapshot.getValue(String.class);
                userDbReference = firebaseInfo.getUsersDbReference().child(str);
                getUserProfileImage();
                getNickname();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getImage(){
        imageDbReference.child("imageFileName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String str = dataSnapshot.getValue(String.class);
                imageSReference = firebaseInfo.getImagesSReference().child(str);
                setImage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserProfileImage(){
        userDbReference.child("profileImage").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String str = dataSnapshot.getValue(String.class);
                profileImageSReference = firebaseInfo.getProfileImagesSReference().child(str);
                setProfileImage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getNickname(){
        userDbReference.child("nickname").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nickname = dataSnapshot.getValue(String.class);
                setNickname();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setProfileImage(){
        profileImageSReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(view.getContext()).load(uri).resize(30, 30).into(userProfileImageButton);
            }
        });
    }

    private void setNickname(){
        userNicknameTextView.setText(nickname);
    }

    private void setImage(){
        imageSReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(view.getContext()).load(uri).transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        int sWidth = source.getWidth(), sHeight = source.getHeight();
                        float p = (float) sHeight / sWidth;
                        int h = (int)(p * currentImage.getWidth());
                        Bitmap res = Bitmap.createScaledBitmap(source, currentImage.getWidth(), h, false);
                        source.recycle();
                        return res;
                    }

                    @Override
                    public String key() {
                        return "scaledImage()";
                    }
                }).into(currentImage);
            }
        });
    }

    private void iterateImageView(){
        imageViewsDbReference.runTransaction(new Transaction.Handler() {
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
