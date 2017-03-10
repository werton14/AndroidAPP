package com.example.vital.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ActivityNickname extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser firebaseUser;
    private byte image [];
    private EditText firstNameEdit;
    private EditText secondNameEdit;
    private ImageButton chooseImageFromGalleryButton;

    final private int PHOTO_FROM_GALLERY_REQUEST = 233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityname);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        image = null;
        firebaseStorage = FirebaseStorage.getInstance();

        chooseImageFromGalleryButton = (ImageButton) findViewById(R.id.choose_image_from_gallery);
        chooseImageFromGalleryButton.setOnClickListener(getGalleryButtonOnClickListener());

        Button finish = (Button) findViewById(R.id.finish);
        finish.setOnClickListener(getFinishButtonOnClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_FROM_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            chooseImageFromGalleryButton.setImageURI(selectedImage);
            cropPic(selectedImage);
        }if(requestCode == UCrop.REQUEST_CROP && requestCode == RESULT_OK){
            Uri selectedImage = UCrop.getOutput(data);
            savePic(selectedImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getNickName(){
        firstNameEdit = (EditText) findViewById(R.id.firstname);
        String firstName = firstNameEdit.getText().toString().trim();

        secondNameEdit = (EditText) findViewById(R.id.secondname);
        String secondName = secondNameEdit.getText().toString().trim();

        return firstName + ' ' + secondName;
    }

    private void cropPic(Uri selectedImage){
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        Uri whereWrite = null;
        UCrop.of(selectedImage, whereWrite).start(ActivityNickname.this);
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

    private UploadTask uploadPic(StorageReference reference){
        UploadTask uploadTask = reference.putBytes(image);
        return  uploadTask;
    }

    private boolean fieldIsComplete(){
        return firstNameEdit.getText().toString().trim().length() > 3 &&
                secondNameEdit.getText().toString().trim().length() > 3;
    }

    private View.OnClickListener getFinishButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference reference = firebaseStorage.getReference().child("userProfileImage").child("work.jpg");
                UploadTask uploadPic = uploadPic(reference);
                if (uploadPic.isSuccessful() && fieldIsComplete()) {
                    UserProfileChangeRequest.Builder userProfileChangeRequest = new UserProfileChangeRequest.Builder();
                    userProfileChangeRequest.setDisplayName(getNickName());
                    userProfileChangeRequest.setPhotoUri(uploadPic.getResult().getDownloadUrl());
                    firebaseUser.updateProfile(userProfileChangeRequest.build());
                }
            }
        };
    }

    private View.OnClickListener getGalleryButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_FROM_GALLERY_REQUEST);
            }
        };
    }
}
