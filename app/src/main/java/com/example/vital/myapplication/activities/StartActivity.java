package com.example.vital.myapplication.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.example.vital.myapplication.FirebaseInfo;
import com.example.vital.myapplication.R;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

public class StartActivity extends AppCompatActivity {

    FirebaseInfo firebaseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitystart);

        firebaseInfo = FirebaseInfo.getInstance();

        if(firebaseInfo.isSignedIn()) toChooseActivity();

        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
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
