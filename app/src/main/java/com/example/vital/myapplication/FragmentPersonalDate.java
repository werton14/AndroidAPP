package com.example.vital.myapplication;

import android.content.Context;
import android.net.Uri;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
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
    private GridView gridLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseInfo = FirebaseInfo.getInstance();
        View view = inflater.inflate(R.layout.activity_personal_date, container, false);
        profileImageView = (CircleImageView) view.findViewById(R.id.personal_profile_image);
        nicknameTextView = (TextView) view.findViewById(R.id.nickname);
        descriptionTextView = (TextView) view.findViewById(R.id.description);
        gridLayout = view.findViewById(R.id.profile_grid_layout);
        gridLayout.setAdapter(new ImageAdapterGridView(getActivity().getApplicationContext()));
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
        Picasso.with(getActivity().getApplicationContext()).load(user.getProfileImageFileName()).into(profileImageView);

    }

    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return 40; // count of rows
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;

            if (convertView == null) {
                mImageView = new ImageView(mContext);
                mImageView.setLayoutParams(new GridView.LayoutParams(130, 130));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(10, 10, 10, 10);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(R.mipmap.ic_cake);
            return mImageView;
        }
    }

}

