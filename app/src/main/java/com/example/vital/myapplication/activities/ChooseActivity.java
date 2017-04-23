package com.example.vital.myapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;

import com.example.vital.myapplication.R;
import com.example.vital.myapplication.SectionsPagerAdapter;
import com.example.vital.myapplication.activities.StartActivity;
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

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth mAuth;
    private DatabaseReference savePicList;
    private DatabaseReference savePicId;
    private DatabaseReference imageViewsReference;
    private String storageFileName;
    private String userId;

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

        initFirebaseComponent();
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
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileForNewPhoto = Uri.fromFile(getOutputMediaFile());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileForNewPhoto);
                    startActivityForResult(intent, GET_PHOTO_REQUEST);
                    handler.postDelayed(runnable, 1000);
                }
                if (position == 0) {
                    Intent intent = new Intent(getApplicationContext(), ContainerUserFragmentsActivity.class);
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
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    void uploadPic(Uri selectedImage){
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
        byte image[] = baos.toByteArray();

        StorageReference reference = FirebaseStorage.getInstance().getReference().child("image").child(storageFileName);
        UploadTask uploadTask = reference.putBytes(image);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                savePicList.child("userId").setValue(userId);
                savePicList.child("photoStorageId").setValue(storageFileName);
                savePicList.child("likesCount").setValue(0);
                savePicId.setValue(savePicList.getKey());
                long t = 0;
                imageViewsReference.child(savePicList.getKey()).setValue(t);
            }
        });
    }

    private void initFirebaseComponent(){
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageFileName = UUID.randomUUID().toString() + ".jpg";
        mAuth = FirebaseAuth.getInstance();
        authStateListener = getAuthStateListener();
        savePicList = FirebaseDatabase.getInstance().getReference().child("image").push();
        savePicId = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("photoId");
        imageViewsReference = FirebaseDatabase.getInstance().getReference().child("views");
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
