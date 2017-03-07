package com.example.vital.myapplication;

import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ActivityNickname extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private UserProfileChangeRequest.Builder userProfileChangeRequest;

    private EditText firstNameEdit;
    private EditText secondNameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityname);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firstNameEdit = (EditText) findViewById(R.id.firstname);
        String firstName = firstNameEdit.getText().toString();

        secondNameEdit = (EditText) findViewById(R.id.secondname);
        String secondName = secondNameEdit.getText().toString();

        String nickName = firstName + ' ' + secondName;

        userProfileChangeRequest = new UserProfileChangeRequest.Builder();
        userProfileChangeRequest.setDisplayName(nickName);

        Button finish = (Button) findViewById(R.id.finish);
        final EditText nickname = (EditText) findViewById(R.id.nickname);



    }
}
