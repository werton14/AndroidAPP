package com.example.vital.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class ActivityChoose extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth mAuth;
    private byte image[];
    private String fileName;

    static final int GET_PHOTO_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activitychoose);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        onPageChangeListener = this.getOnPageListener();
        mAuth = FirebaseAuth.getInstance();
        authStateListener = this.getAuthStateListener();
        fileName = UUID.randomUUID().toString() + ".jpg";
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewPager.setOnPageChangeListener(onPageChangeListener);
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewPager.removeOnPageChangeListener(onPageChangeListener);
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_PHOTO_REQUEST && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Log.w("Hi", uri.toString());
            //savePic(data.getData());
            //uploadPic();
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
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, GET_PHOTO_REQUEST);
                    handler.postDelayed(runnable, 1000);
                }
                if (position == 0) {
                    Intent intent = new Intent(getApplicationContext(), ActivityScroll.class);
                    startActivity(intent);
                    handler.postDelayed(runnable, 1000);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }


    private FirebaseAuth.AuthStateListener getAuthStateListener(){
        return new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(getApplicationContext(), ActivityStart.class);
                    startActivity(intent);
                }
            }
        };
    }

    void uploadPic(){
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("image").child(fileName);
        UploadTask uploadTask = reference.putBytes(image);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                DatabaseReference savePicList = FirebaseDatabase.getInstance().getReference().child("image").push();
                savePicList.child("userId").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                DatabaseReference savePicId = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                savePicId.child("photoId").setValue(savePicId.getKey());
            }
        });
    }

    private void savePic(Uri selectedImage){
        Bitmap img = null;
        try {
            img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        image = baos.toByteArray();
    }
}
