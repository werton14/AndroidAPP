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
import android.view.Window;
import android.view.WindowManager;

import com.example.vital.myapplication.FirebaseInfo;
import com.example.vital.myapplication.R;

public class StartActivity extends AppCompatActivity {

    FirebaseInfo firebaseInfo;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.background_gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.dark));
            window.setNavigationBarColor(activity.getResources().getColor(R.color.dark));
            window.setBackgroundDrawable(background);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Для градиента Statusbar*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/

        setStatusBarGradiant(this);

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
