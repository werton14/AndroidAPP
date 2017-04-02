package com.example.vital.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityStart extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitystart);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = getAuthStateListener();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    private FirebaseAuth.AuthStateListener getAuthStateListener(){
        return new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(getApplicationContext(), ActivityChoose.class);
                    startActivity(intent);
                }
            }
        };
    }

    public void onLogInButtonClick(View view){
        Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
        startActivity(intent);
    }

    public void onSignInButtonClick(View view){
        Intent intent = new Intent(getApplicationContext(), ActivitySignUp.class);
        startActivity(intent);
    }
}
