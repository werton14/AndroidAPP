package com.example.vital.myapplication.activities;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.WrapperListAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.util.LruCache;
import com.example.vital.myapplication.OnSwipeTouchListener;
import com.example.vital.myapplication.R;
import com.example.vital.myapplication.ShadowLayout;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

/**
 * Created by werton on 24.06.17.
 */

public class FullScreenPictureActivity extends AppCompatActivity {

    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private static final TimeInterpolator sAccelerator = new AccelerateInterpolator();
    private static final int ANIM_DURATION = 500;


    private ImageView mImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_picture);
        mImageView = (ImageView) findViewById(R.id.imageView);
//        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.topLevelLayout);
//        ImageButton button = new ImageButton(this);
//        button.setLayoutParams(new RelativeLayout.LayoutParams(65, 65));
//        button.setImageResource(R.mipmap.ic_arrow_back);
//        button.setBackgroundColor(android.R.color.black);
//        button.setScaleType(ImageButton.ScaleType.FIT_CENTER);
//        relativeLayout.addView(button);
        Bundle bundle = getIntent().getExtras();
        Uri imageUri = Uri.parse(bundle.getString("imageUri"));
        Glide.with(getApplicationContext()).load(imageUri).into(mImageView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView textView = (TextView)toolbar.findViewById(R.id.mytext);
        textView.setVisibility(View.INVISIBLE);
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.black));

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

        mImageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
            @Override
            public void onSwipeRight() {
                finish();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
            }
        });
    }
}
