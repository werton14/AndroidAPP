package com.example.vital.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class SignupScreen extends AppCompatActivity {

    private FirebaseAuth mAught;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        Button singInButton = (Button) findViewById(R.id.sing_in);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);

        singInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString());
            }
        });
    }

    private void createUserWithEmailAndPassword(String email, String password){
        mAught = FirebaseAuth.getInstance();

        mAught.createUserWithEmailAndPassword(email, password);
    }
}
