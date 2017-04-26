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
    private View view;
    private ImageData imageData;

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
        imageData = new ImageData();

        Picasso.with(getContext()).load(imageData.getImageUri()).resize(view.getWidth(),view.getHeight() -100).into(currentImage);
        Picasso.with(getContext()).load(imageData.getProfileImageUri()).into(userProfileImageButton);
        userNicknameTextView.setText(imageData.getNickname());

        return view;
    }

}
