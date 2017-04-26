package com.example.vital.myapplication.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vital.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private EditText editEmail;
    private EditText editPassword;
    private ImageButton chooseProfileImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityname);

        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);
        chooseProfileImageButton = (ImageButton) findViewById(R.id.choose_image_from_gallery);

    }

    private void createUserWithEmailAndPassword(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private FirebaseAuth.AuthStateListener getAuthStateListener(){
        return new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(getApplicationContext(), NicknameActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(authStateListener != null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void onFinishButtonClick(View view){
        if (TextUtils.isEmpty(editEmail.getText().toString())) {
            editEmail.setError("This field cannot be empty!");
        }
        if (TextUtils.isEmpty(editPassword.getText().toString())) {
            editPassword.setError("This field cannot be empty!");
        }
        if (TextUtils.isEmpty(editPasswordRepeat.getText().toString())) {
            editPasswordRepeat.setError("This field cannot be empty!");
        }if(editPassword.getText().toString().compareTo(editPasswordRepeat.getText().toString()) != 0){
            Log.w("EP", editPassword.getText().toString());
            Log.w("EPR", editPasswordRepeat.getText().toString());
            Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
        }if(!TextUtils.isEmpty(editEmail.getText().toString())
                && !TextUtils.isEmpty(editPassword.getText().toString()) && !TextUtils.isEmpty(editPasswordRepeat.getText().toString())
                && editPassword.getText().toString().compareTo(editPasswordRepeat.getText().toString()) == 0) {
            createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString());
        }
    }
}
