package com.example.vital.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.vital.myapplication.FirebaseInfo;
import com.example.vital.myapplication.R;

public class StartActivity extends AppCompatActivity {

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

    public void onToSignInButtonClick(View view){
        toSignInActivity();
    }

    public void onToSignUpButtonClick(View view){
        toSignUp();
    }

    private void toSignInActivity(){
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }

    private void toSignUp(){
        Intent intent = new Intent(getApplicationContext(), NicknameActivity.class);
        startActivity(intent);
    }

    private void toChooseActivity(){
        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
        startActivity(intent);
    }

}
