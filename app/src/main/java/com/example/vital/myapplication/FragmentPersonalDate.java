package com.example.vital.myapplication;

import android.net.Uri;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentPersonalDate extends Fragment {

    private FirebaseInfo firebaseInfo;
    private CircleImageView profileImageView;
    private TextView nicknameTextView;
    private TextView descriptionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseInfo = FirebaseInfo.getInstance();
        View view = inflater.inflate(R.layout.activity_personal_date, container, false);
        profileImageView = (CircleImageView) view.findViewById(R.id.personal_profile_image);
        nicknameTextView = (TextView) view.findViewById(R.id.nickname);
        descriptionTextView = (TextView) view.findViewById(R.id.description);
        getUser();
        return view;
    }

    public static FragmentPersonalDate newInstance(){
        return new FragmentPersonalDate();
    }

    private void getUser (){
        firebaseInfo.getCurrentUserDbReference().addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                nicknameTextView.setText(user.getNickname());
                descriptionTextView.setText(user.getDescription());
                downloadImage(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void downloadImage (User user){
        Picasso.with(getContext()).load(user.getProfileImageFileName()).resize(profileImageView.getWidth(), profileImageView.getHeight()).into(profileImageView);

    }

}

