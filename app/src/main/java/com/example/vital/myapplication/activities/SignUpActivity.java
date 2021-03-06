package com.example.vital.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vital.myapplication.R;
import com.example.vital.myapplication.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private LinearLayout linearLayout;

    private FirebaseAuth firebaseAuth;
    private StorageReference profileImageSReference;
    private String nickname;
    private Uri profileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysignup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else{
            linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSignUp);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    72);
            linearLayout.setLayoutParams(params);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView textView = (TextView)toolbar.findViewById(R.id.mytext);
        textView.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();

        editEmail = (EditText) findViewById(R.id.email_edit_text_on_signUp);
        editPassword = (EditText) findViewById(R.id.password_edit_text_on_signUp);

        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        profileImageUri = Uri.parse(intent.getStringExtra("profileImageUri"));
        if(profileImageUri == null) Log.w("fukaka", "imageUri");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        nickname = intent.getStringExtra("nickname");
        profileImageUri = Uri.parse(intent.getStringExtra("profileImageUri"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }

    private UploadTask uploadPic(byte[] imageBA){
        String profileImageFileName = UUID.randomUUID().toString() + ".webp";
        profileImageSReference = FirebaseStorage.getInstance().getReference().child("profileImages").child(profileImageFileName);

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
                uploadPic(getImageBA(profileImageUri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        updateUserData();
                    }
                });
            }
        };
    }

    private OnFailureListener makeOnFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseAuthException authException = (FirebaseAuthException) e;
                switch (authException.getErrorCode()){
                    case "ERROR_INVALID_EMAIL" : editEmail.setError(e.getMessage());
                        break;
                    case "ERROR_EMAIL_ALREADY_IN_USE" : editEmail.setError(e.getMessage());
                        break;
                    case "ERROR_WEAK_PASSWORD" : editPassword.setError(e.getMessage());
                        break;
                }
            }
        };
    }

    private void updateUserData() {
        profileImageSReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                DatabaseReference userDbReference = FirebaseDatabase.getInstance().getReference().child("users");
                User user = new User(nickname, uri.toString(), " ");
                final String userId = firebaseAuth.getCurrentUser().getUid();
                userDbReference.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference nicknamesDbReference = FirebaseDatabase.getInstance().getReference().child("nicknames");

                        nicknamesDbReference.child(nickname).setValue(userId);
                        toChooseActivity();
                    }
                });
            }
        });

    }

    private void toChooseActivity(){
        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onFinishButtonClick(View view){
        if(isOnline()) {
            if (TextUtils.isEmpty(editEmail.getText().toString()))
                editEmail.setError("This field cannot be empty!");
            else if (TextUtils.isEmpty(editPassword.getText().toString()))
                editPassword.setError("This field cannot be empty!");
            else
                createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString());
        }else{
            Toast.makeText(getApplicationContext(), "Check your internet connection, and try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        toNicknameActivity();
    }

    @Override
    public boolean onSupportNavigateUp() {
        toNicknameActivity();
        return true;
    }

    private void toNicknameActivity(){
        Intent intent = new Intent(this.getApplicationContext(), NicknameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private byte[] getImageBA(Uri selectedImage){
        Bitmap img = null;
        try {
            img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.WEBP, 30, baos);
        return baos.toByteArray();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
