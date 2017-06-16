package com.example.vital.myapplication.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vital.myapplication.R;
import com.example.vital.myapplication.RegistrationData;
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

import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;

    private FirebaseAuth firebaseAuth;
    private StorageReference profileImageSReference;
    private String nickname;
    private byte []profileImageBA;
    private RegistrationData registrationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysignup);

        firebaseAuth = FirebaseAuth.getInstance();
        nickname = getIntent().getStringExtra("nickname");
        profileImageBA = getIntent().getByteArrayExtra("imageBA");

        editEmail = (EditText) findViewById(R.id.email_edit_text_on_signUp);
        editPassword = (EditText) findViewById(R.id.password_edit_text_on_signUp);

        Intent intent = getIntent();
        registrationData = (RegistrationData) intent.getSerializableExtra("RegistrationData");
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
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
        String profileImageFileName = UUID.randomUUID().toString() + ".png";
        profileImageSReference = FirebaseStorage.getInstance().getReference().child("profileImages").child(profileImageFileName);

        DatabaseReference userDbReference = FirebaseDatabase.getInstance().getReference().child("users");
        User user = new User(nickname, profileImageFileName);
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

    private void toChooseActivity(){
        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onFinishButtonClick(View view){
        createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString());
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
}
