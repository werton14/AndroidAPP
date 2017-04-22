package com.example.vital.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ActivityStart extends AppCompatActivity {

    FirebaseInfo firebaseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitystart);

        firebaseInfo = FirebaseInfo.getInstance();

        if(firebaseInfo.isSignedIn()) toChooseActivity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    public void onLogInButtonClick(View view){
        toSignInActivity();
    }

    public void onSignInButtonClick(View view){
        toSignUpActivity();
    }

    private void toSignInActivity(){
        Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
        startActivity(intent);
    }

    private void toSignUpActivity(){
        Intent intent = new Intent(getApplicationContext(), ActivitySignUp.class);
        startActivity(intent);
    }

    private void toChooseActivity(){
        Intent intent = new Intent(getApplicationContext(), ActivityChoose.class);
        startActivity(intent);
    }

}
