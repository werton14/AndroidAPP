package com.example.vital.myapplication;


import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class FragmentPrototypeScroll extends Fragment {

    private ImageButton userProfileImageButton;
    private TextView userNicknameTextView;
    private ImageView currentImage;
    private ImageButton likeImageButton;
    private TextView likeTextView;
    private ImageButton downloadPicture;
    private String photoId;
    private String photoStorageId;

    private DatabaseReference imageViewsReference;
    private DatabaseReference imageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_prototype_scroll, container, false);

        userProfileImageButton = (ImageButton) view.findViewById(R.id.userProfilePicture);
        userNicknameTextView = (TextView) view.findViewById(R.id.userNickname);
        currentImage = (ImageView) view.findViewById(R.id.currentPhoto);
        likeImageButton = (ImageButton) view.findViewById(R.id.likeImageButton);
        likeTextView = (TextView) view.findViewById(R.id.likeTextView);
        downloadPicture = (ImageButton) view.findViewById(R.id.downloadImageButton);

        imageViewsReference = FirebaseDatabase.getInstance().getReference().child("views");
        imageReference = FirebaseDatabase.getInstance().getReference().child("image");

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

    private void setMainPhoto(){
        imageReference = imageReference.child(photoId);
        imageReference.child("photoStorageId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(photoStorageId == null){
                    photoStorageId = dataSnapshot.getValue(String.class);
                    Log.w("Tag", photoStorageId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
