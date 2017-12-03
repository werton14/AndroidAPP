package com.example.vital.myapplication.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vital.myapplication.ImageAdapterGridView;
import com.example.vital.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Artem on 12.11.2017.
 */

public class ForeignPersonalData extends AppCompatActivity {

    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private TextView descriptionTextView;
    private TextView nicknameTextView;
    private CircleImageView profileImageView;
    private GridView gridView;

    private String nickname;
    private String description;
    private String userProfileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_date);

        relativeLayout = (RelativeLayout) findViewById(R.id.activity_personal_date);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutDataa);
        descriptionTextView = (TextView) findViewById(R.id.description);
        nicknameTextView = (TextView) findViewById(R.id.nickname);
        profileImageView = (CircleImageView) findViewById(R.id.personal_profile_image);

        Bundle extras = getIntent().getExtras();

        nickname = extras.getString("nickname");
        description = extras.getString("description");
        userProfileImageUri = extras.getString("userProfileImageUri");

        Log.w("nbvgh", nickname + " " + description +
         " " + userProfileImageUri);


        nicknameTextView.setText(nickname.toString());
        descriptionTextView.setText(description.toString());
        Glide.with(getApplicationContext()).load(userProfileImageUri).into(profileImageView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            linearLayout.setVisibility(View.VISIBLE);
        } else {

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(params);
            linearLayout.setVisibility(View.VISIBLE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridView = findViewById(R.id.profile_grid_layout);
        gridView.setAdapter(new ImageAdapterGridView(getApplicationContext()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
