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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.util.LruCache;
import com.example.vital.myapplication.R;
import com.example.vital.myapplication.ShadowLayout;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

/**
 * Created by werton on 24.06.17.
 */

public class FullScreenPictureActivity extends Activity {

    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private static final TimeInterpolator sAccelerator = new AccelerateInterpolator();
    private static final int ANIM_DURATION = 500;

    private BitmapDrawable mBitmapDrawable;
    private ColorMatrix colorizerMatrix = new ColorMatrix();
    ColorDrawable mBackground;
    int mLeftDelta;
    int mTopDelta;
    float mWidthScale;
    float mHeightScale;
    private ImageView mImageView;
    private FrameLayout mTopLevelLayout;
    private ShadowLayout mShadowLayout;
    private int mOriginalOrientation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_picture);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mTopLevelLayout = (FrameLayout) findViewById(R.id.topLevelLayout);
        mShadowLayout = (ShadowLayout) findViewById(R.id.shadowLayout);

        Bundle bundle = getIntent().getExtras();
        Uri imageUri = Uri.parse(bundle.getString("imageUri"));


    }
}
