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

import java.io.File;
import java.io.IOException;

public class FragmentPrototypeScroll extends Fragment {

    private ImageButton userProfileImageButton;
    private TextView userNicknameTextView;
    private ImageView currentImage;
    private ImageButton likeImageButton;
    private TextView likeTextView;
    private ImageButton downloadPicture;
    private String photoId;
    private String photoStorageId;
    private Uri mainImageDownloadUri;

    private DatabaseReference imageViewsReference;
    private DatabaseReference imageReference;
    private StorageReference mainImageStorageReference;
    private View view;

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

        imageViewsReference = FirebaseDatabase.getInstance().getReference().child("views");
        imageReference = FirebaseDatabase.getInstance().getReference().child("image");
        mainImageStorageReference = FirebaseStorage.getInstance().getReference().child("image");

        photoId = null;
        photoStorageId = null;
        getImage();

        return view;
    }

    private void getImage(){
        imageViewsReference.orderByValue().limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(photoId == null) {
                    photoId = dataSnapshot.getChildren().iterator().next().getKey();
                    iterateImageViewCounter();
                    getMainPhoto();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void iterateImageViewCounter(){
        imageViewsReference.child(photoId).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                long v = mutableData.getValue(long.class);
                mutableData.setValue(++v);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    private void getMainPhoto(){
        imageReference.child(photoId).child("photoStorageId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(photoStorageId == null){
                    photoStorageId = dataSnapshot.getValue(String.class);
                    getMainPhotoFromStorage();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMainPhotoFromStorage() {
        mainImageStorageReference.child(photoStorageId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(view.getContext()).load(uri).resize(view.getWidth(), view.getHeight()- 100).into(currentImage);
            }
        });
    }
}
