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

public class FragmentPrototypeScroll extends Fragment {

    private ImageButton userProfileImageButton;
    private TextView userNicknameTextView;
    private ImageView currentImage;
    private ImageButton likeImageButton;
    private TextView likeTextView;
    private ImageButton downloadPicture;
    private View view;

    private Image image;

    private User user;



    private DatabaseReference imageDbReference;
    private DatabaseReference imageViewsDbReference;
    private FragmentPrototypeScroll nextFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.image, container, false);


       /* userProfileImageButton = (ImageButton) view.findViewById(R.id.userProfilePicture);
        userNicknameTextView = (TextView) view.findViewById(R.id.userNickname);
        currentImage = (ImageView) view.findViewById(R.id.currentPhoto);
        likeImageButton = (ImageButton) view.findViewById(R.id.likeImageButton);
        likeTextView = (TextView) view.findViewById(R.id.likeTextView);
        downloadPicture = (ImageButton) view.findViewById(R.id.downloadImageButton);
        firebaseInfo = FirebaseInfo.getInstance();
*/

        return view;
    }
/*

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
                iterateImageViews();
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
                setNickname();
                setImageHeight();
                setLikeCount();
                setProfileImage();
                setImage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setNickname(){
        userNicknameTextView.setText(user.getNickname());
    }

    private void setImageHeight(){
        float p = (float) image.getHeight() / image.getWidth();
        int h = (int)(p * currentImage.getWidth());
        currentImage.setMinimumHeight(h);
    }

    private void setLikeCount(){
        likeTextView.setText(String.valueOf(image.getLikeCount()));
    }

    private void setProfileImage(){
        firebaseInfo.getProfileImagesSReference().child(user.getProfileImageFileName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
*/
/*
                Picasso.with(view.getContext()).load(uri).resize(userProfileImageButton.getWidth(),
                        userProfileImageButton.getHeight()).into(userProfileImageButton);
*//*

            }
        });
    }

    private void setImage(){
        firebaseInfo.getImagesSReference().child(user.getCompetitiveImageFileName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
*/
/*
                Picasso.with(view.getContext()).load(uri)
                        .resize(currentImage.getWidth(), currentImage.getHeight()).into(currentImage);
*//*

            }
        });
    }

    private void iterateImageViews(){
        imageViewsDbReference.child("time").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                viewIsReadyListener.OnViewIsReady();
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

    public void addViewIsReadyListener(ViewIsReadyListener viewIsReadyListener) {
        this.viewIsReadyListener = viewIsReadyListener;
    }

    public void setNextFragment(FragmentPrototypeScroll nextFragment) {
        this.nextFragment = nextFragment;
    }

    public interface ViewIsReadyListener{
        public void OnViewIsReady();
    }
*/

}
