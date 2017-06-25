package com.example.vital.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vital.myapplication.FirebaseInfo;
import com.example.vital.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by werton on 01.05.17.
 */

public class NicknameActivity extends AppCompatActivity {

    private EditText editNickname;
    private CircleImageView chooseProfileImageButton;
    private DatabaseReference nicknamesDbReference;
    private String nickname;
    private Uri profileImageUri;

    final private int PHOTO_FROM_GALLERY_REQUEST = 233;

    private boolean startAnim;
    private boolean imageIsChoosed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityname);

        nicknamesDbReference = FirebaseInfo.getInstance().getNicknamesDbReference();

        editNickname = (EditText) findViewById(R.id.nickname_edit_text_on_nickname);
        chooseProfileImageButton = (CircleImageView) findViewById(R.id.choose_image_from_gallery);

        startAnim = true;
        imageIsChoosed = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_FROM_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            startCropActivity(selectedImage);
        }if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            profileImageUri = result.getUri();
            chooseProfileImageButton.setImageURI(profileImageUri);
            imageIsChoosed = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        if(startAnim){
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            startAnim = false;
        }else {
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        }
        super.onResume();
    }

    private void startCropActivity(Uri selectedImage){
        CropImage.activity(selectedImage).setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.OVAL).setAspectRatio(1, 1).start(this);
    }

    private void toSignUpActivity(){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("nickname", nickname);
        intent.putExtra("profileImageUri", profileImageUri.toString());
        startActivity(intent);
    }

    private void checkNicknameAvailable(){
        final String n = editNickname.getText().toString().trim();
        if(n.length() < 3){
            editNickname.setError("Nickname short!");
        }else if(n.length() > 25){
            editNickname.setError("Nickname long!");
        }else {
            nicknamesDbReference.child(n).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() == null){
                        nickname = n;
                        toSignUpActivity();
                    }else{
                        editNickname.setError("This nickname already exist!");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onNextButtonClick(View view){

        if(isOnline()) {
            if(imageIsChoosed) checkNicknameAvailable();
            else Toast.makeText(getApplicationContext(), "Please, choose profile image.", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(getApplicationContext(), "Check your internet connection, and try again.", Toast.LENGTH_SHORT).show();
    }

    public void onChooseProfileImageButtonClick(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_FROM_GALLERY_REQUEST);
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
