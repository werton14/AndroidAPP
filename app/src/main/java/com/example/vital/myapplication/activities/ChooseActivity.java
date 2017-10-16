package com.example.vital.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.vital.myapplication.FirebaseInfo;
import com.example.vital.myapplication.FragmentScroll;
import com.example.vital.myapplication.R;
import com.example.vital.myapplication.SectionsPagerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ChooseActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private Uri fileForNewPhoto;
    private FragmentScroll fragmentScroll;

    private FirebaseInfo firebaseInfo;
    private Activity activity;

    static final int GET_PHOTO_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activitychoose);
        activity = this;

        firebaseInfo = FirebaseInfo.getInstance();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            fragmentScroll.getLinearLayout().setLayoutParams(params);
        }

        onPageChangeListener = this.getOnPageListener();
        mViewPager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_PHOTO_REQUEST && resultCode == RESULT_OK){
            uploadPic(fileForNewPhoto);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    private ViewPager.OnPageChangeListener getOnPageListener() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(1);
            }
        };
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 2) {
                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileForNewPhoto = Uri.fromFile(getOutputMediaFile());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileForNewPhoto);
                    startActivityForResult(intent, GET_PHOTO_REQUEST);
                    handler.postDelayed(runnable, 1000);*/
                    //getPhoto();
                    /*requestWindowFeature(Window.FEATURE_NO_TITLE);
                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/                  getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                }if(position == 1){
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                }
                if (position == 0) {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

//    private void getPhoto(){
//        MagicalCamera magicalCamera = new MagicalCamera(this, MagicalCamera.TAKE_PHOTO, null);
//        magicalCamera.takeFragmentPhoto(mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem()));
//    }



    void uploadPic(Uri selectedImage){
        Bitmap img  = null;
        try {
            img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final int width = img.getWidth();
        final int height = img.getHeight();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.WEBP, 30, baos);
        final byte imageBA[] = baos.toByteArray();

        final String competitiveImageFileName = UUID.randomUUID().toString() + ".webp";

        StorageReference competitiveImageSReference = firebaseInfo.getImagesSReference().child(competitiveImageFileName);
        competitiveImageSReference.putBytes(imageBA).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                DatabaseReference imageDbReference = firebaseInfo.getImagesDbReference().push();
                Image image = new Image(competitiveImageFileName, firebaseInfo.getCurrentUserId(), width, height);
                imageDbReference.setValue(image);
                DatabaseReference viewsDbReference = firebaseInfo.getViewsDbReference();
                viewsDbReference.child(imageDbReference.getKey()).child("view").setValue(0L);
                viewsDbReference.child(imageDbReference.getKey()).child("time").setValue(ServerValue.TIMESTAMP);
            }
        });
    }


    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
}
