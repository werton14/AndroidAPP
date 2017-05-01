package com.example.vital.myapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vital.myapplication.FirebaseInfo;
import com.example.vital.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;

    private FirebaseAuth firebaseAuth;
    private FirebaseInfo firebaseInfo;
    private DatabaseReference profileImageDbReference;
    private StorageReference profileImageSReference;
    private DatabaseReference nicknameDbReference;
    private String nickname;
    private byte []profileImageBA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysignup);

        firebaseInfo = FirebaseInfo.getInstance();
        firebaseAuth = firebaseInfo.getFirebaseAuth();

        nickname = getIntent().getStringExtra("nickname");
        profileImageBA = getIntent().getByteArrayExtra("imageBA");

        editEmail = (EditText) findViewById(R.id.email_edit_text_on_signUp);
        editPassword = (EditText) findViewById(R.id.password_edit_text_on_signUp);

    }

    private UploadTask uploadPic(byte[] imageBA){
        UploadTask uploadTask = profileImageSReference.putBytes(imageBA);
        return  uploadTask;
    }

    private void createUserWithEmailAndPassword(String email, String password){
        Task<AuthResult> createUserTask = firebaseAuth.createUserWithEmailAndPassword(email, password);
        createUserTask.addOnSuccessListener(makeOnSuccessListener());
        createUserTask.addOnFailureListener(makeOnFailureListener());
    }

    private OnSuccessListener makeOnSuccessListener(){
        return new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                updateUserData();
                if(profileImageBA != null) uploadPic(profileImageBA);
                updateUserNickname();

            }
        };
    }

    private OnFailureListener makeOnFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }

    private void updateUserData(){
        String profileImageFileName = UUID.randomUUID().toString() + ".png";
        String competitiveImageFileName = UUID.randomUUID().toString() + ".jpg";

        profileImageDbReference = firebaseInfo.getCurrentUserProfileImageDbReference();
        nicknameDbReference = firebaseInfo.getCurrentUserNicknameDbReference();
        DatabaseReference competitiveImageDbReference = firebaseInfo.getCurrentUserCompetitiveImageDbReference();

        profileImageSReference = firebaseInfo.getProfileImagesSReference().child(profileImageFileName);

        profileImageDbReference.setValue(profileImageFileName);
        competitiveImageDbReference.setValue(competitiveImageFileName);
    }

    private void updateUserNickname(){
        nicknameDbReference.setValue(nickname).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                toChooseActivity();
            }
        });
    }


    private void toChooseActivity(){
        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
        startActivity(intent);
    }

    public void onFinishButtonClick(View view){
        if(!firebaseInfo.isSignedIn()) {
            createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString());
        }else {
            updateUserNickname();
        }
    }
}
