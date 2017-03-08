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

import com.google.android.gms.tasks.OnSuccessListener;
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
    private UserProfileChangeRequest.Builder userProfileChangeRequest;
    private byte image [];
    private Bitmap img;
    private EditText firstNameEdit;
    private EditText secondNameEdit;

    final private int PHOTO_FROM_GALLERY_REQUEST = 233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityname);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        img = null;
        image = null;
        firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference reference = firebaseStorage.getReference().child("userProfileImage").child("work.jpg");

        final ImageButton chooseImageFromGalleryButton = (ImageButton) findViewById(R.id.choose_image_from_gallery);
        chooseImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_FROM_GALLERY_REQUEST);
            }
        });

        userProfileChangeRequest = new UserProfileChangeRequest.Builder();
        Button finish = (Button) findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadTask uploadTask = reference.putBytes(image);
                chooseImageFromGalleryButton.setImageBitmap(img);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(firstNameEdit.getText().toString().trim().length() > 3 &&
                                secondNameEdit.getText().toString().trim().length() > 3) {
                            userProfileChangeRequest.setDisplayName(getNickName());
                            userProfileChangeRequest.setPhotoUri(taskSnapshot.getDownloadUrl());
                            firebaseUser.updateProfile(userProfileChangeRequest.build());
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null)
        if(requestCode == PHOTO_FROM_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            cropPhoto(selectedImage);
        }if(requestCode == UCrop.REQUEST_CROP && requestCode == RESULT_OK){
            Uri selectedImage = UCrop.getOutput(data);
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
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getNickName(){
        firstNameEdit = (EditText) findViewById(R.id.firstname);
        String firstName = firstNameEdit.getText().toString();

        secondNameEdit = (EditText) findViewById(R.id.secondname);
        String secondName = secondNameEdit.getText().toString();

        return firstName + ' ' + secondName;
    }

    private void cropPhoto(Uri selectedImage){
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        UCrop.of(selectedImage, selectedImage).withAspectRatio(16, 9).withMaxResultSize(1000, 1000).withOptions(options).start(ActivityNickname.this);
    }
}
