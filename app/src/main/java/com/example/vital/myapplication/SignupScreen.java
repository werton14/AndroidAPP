package com.example.vital.myapplication;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText editNickname;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editPasswordRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        editNickname = (EditText) findViewById(R.id.Nickname);
        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);
        editPasswordRepeat = (EditText) findViewById(R.id.repeat_password);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(getApplicationContext(), Choose_screen.class);
                    startActivity(intent);
                }
            }
        };

        Button singInButton = (Button) findViewById(R.id.sing_in);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);

        singInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editNickname.getText().toString())) {
                    editNickname.setError("This field cannot be empty!");
                }
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
                }if(!TextUtils.isEmpty(editNickname.getText().toString()) && !TextUtils.isEmpty(editEmail.getText().toString())
                        && !TextUtils.isEmpty(editPassword.getText().toString()) && !TextUtils.isEmpty(editPasswordRepeat.getText().toString())
                        && editPassword.getText().toString().compareTo(editPasswordRepeat.getText().toString()) == 0) {
                    createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    private void createUserWithEmailAndPassword(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
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
}
