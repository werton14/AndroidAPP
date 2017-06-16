package com.example.vital.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vital.myapplication.FirebaseInfo;
import com.example.vital.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;


/**
 * I
 * Created by vital on 11.12.2016.
 */

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylogin);

        email = (EditText) findViewById(R.id.email_edit_text_on_signIn);
        password = (EditText) findViewById(R.id.password_edit_text_on_signIn);
        firebaseAuth = FirebaseInfo.getInstance().getFirebaseAuth();
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }

    public void onSignInButtonClick(View view){
        if(isOnline()) {
            if (isEmailCompleted() && isPasswordCompleted()) {
                Task<AuthResult> authResultTask = firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString());
                authResultTask.addOnFailureListener(makeOnFailureListener());
                authResultTask.addOnSuccessListener(makeOnSuccessListener());
            } else {
                if (!isEmailCompleted()) email.setError("This field cannot be empty!");
                if (!isPasswordCompleted()) password.setError("This field cannot be empty!");
            }
        }else {
            Toast.makeText(getApplicationContext(), "Check your internet connection, and try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void toChooseActivity(){
        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
        startActivity(intent);
    }

    private OnFailureListener makeOnFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseAuthException authException = (FirebaseAuthException) e;
                switch (authException.getErrorCode()){
                    case "ERROR_INVALID_EMAIL" : email.setError(e.getMessage());
                        break;
                    case "ERROR_USER_NOT_FOUND" : email.setError(e.getMessage());
                        break;
                    case "ERROR_USER_DISABLED" : email.setError(e.getMessage());
                        break;
                    case "ERROR_USER_TOKEN_EXPIRED" : email.setError(e.getMessage());
                        break;
                    case  "ERROR_WRONG_PASSWORD" : password.setError(e.getMessage());
                }
            }
        };
    }

    private OnSuccessListener makeOnSuccessListener(){
        return new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                toChooseActivity();
            }
        };
    }

    private boolean isEmailCompleted(){
        return !TextUtils.isEmpty(email.getText().toString());

    }

    private boolean isPasswordCompleted(){
        return !TextUtils.isEmpty(password.getText().toString());
    }

    @Override
    public void onBackPressed() {
        toStartActivity();
    }

    private void toStartActivity(){
        Intent intent = new Intent(this.getApplicationContext(), StartActivity.class);
        startActivity(intent);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
