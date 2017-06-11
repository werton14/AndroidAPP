package com.example.vital.myapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;

import com.example.vital.myapplication.FirebaseInfo;
import com.example.vital.myapplication.R;
import com.example.vital.myapplication.SectionsPagerAdapter;
import com.example.vital.myapplication.User;
import com.example.vital.myapplication.activities.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
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

    private FirebaseInfo firebaseInfo;

    static final int GET_PHOTO_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activitychoose);

        firebaseInfo = FirebaseInfo.getInstance();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

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
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileForNewPhoto = Uri.fromFile(getOutputMediaFile());
                    //intent.setType("image/*");
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



    void uploadPic(Uri selectedImage){
        Bitmap img = null;
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
        img.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        final byte imageBA[] = baos.toByteArray();

        final String competitiveImageFileName = UUID.randomUUID().toString() + ".jpg";

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
