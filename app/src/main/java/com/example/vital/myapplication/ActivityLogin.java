package com.example.vital.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by vital on 11.12.2016.
 */

public class ActivityLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(getApplicationContext(), ActivityChoose.class);
                    startActivity(intent);
                }
            }
        };

        final Button signIn = (Button) findViewById(R.id.log_in);
        final EditText editEmail = (EditText) findViewById(R.id.Email);
        final EditText editPassword = (EditText) findViewById(R.id.Password);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editEmail.getText().toString())){
                    editEmail.setError("This field cannot be empty!");
                }if(TextUtils.isEmpty(editPassword.getText().toString())){
                    editPassword.setError("This field cannot be empty!");
                }if(!TextUtils.isEmpty(editEmail.getText().toString()) && !TextUtils.isEmpty(editPassword.getText().toString())){
                    signInWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void signInWithEmailAndPassword(final String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        });
    }
}
