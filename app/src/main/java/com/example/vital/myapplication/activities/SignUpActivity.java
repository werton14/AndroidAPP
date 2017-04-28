package com.example.vital.myapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vital.myapplication.FirebaseInfo;
import com.example.vital.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    private EditText editNickname;
    private EditText editEmail;
    private EditText editPassword;
    private ImageButton chooseProfileImageButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseInfo firebaseInfo;
    private DatabaseReference profileImageDbReference;
    private StorageReference profileImageSReference;
    private DatabaseReference nicknameDbReference;

    private Uri localProfileImageUri;

    final private int PHOTO_FROM_GALLERY_REQUEST = 233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityname);

        localProfileImageUri = null;

        firebaseInfo = FirebaseInfo.getInstance();
        firebaseAuth = firebaseInfo.getFirebaseAuth();

        editNickname = (EditText) findViewById(R.id.nickname_edit_text);
        editEmail = (EditText) findViewById(R.id.email_edit_text_on_signUp);
        editPassword = (EditText) findViewById(R.id.password_edit_text_on_signUp);
        chooseProfileImageButton = (ImageButton) findViewById(R.id.choose_image_from_gallery);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_FROM_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            startCropActivity(selectedImage);
        }if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            localProfileImageUri = result.getUri();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCropActivity(Uri selectedImage){
        CropImage.activity(selectedImage).setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.OVAL).setAspectRatio(1, 1).start(this);
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private byte[] getImageBA(Uri selectedImage){
        Bitmap img = null;
        try {
            img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = getCroppedBitmap(img);
        chooseProfileImageButton.setImageBitmap(img);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private UploadTask uploadPic(byte[] imageBA){
        UploadTask uploadTask = profileImageSReference.putBytes(imageBA);
        return  uploadTask;
    }

    private void createUserWithEmailAndPassword(String email, String password){
        Task<AuthResult> createUserTask = firebaseAuth.createUserWithEmailAndPassword(email, password);
        createUserTask.addOnSuccessListener(makeOnSuccessListener());
        createUserTask.addOnFailureListener(makeOnFailureListener());
    }

    private OnSuccessListener makeOnSuccessListener(){
        return new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                updateUserData();
                if(localProfileImageUri != null) uploadPic(getImageBA(localProfileImageUri));
                updateUserNickname();

            }
        };
    }

    private OnFailureListener makeOnFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }

    private void updateUserData(){
        String profileImageFileName = UUID.randomUUID().toString() + ".png";
        String competitiveImageFileName = UUID.randomUUID().toString() + ".jpg";

        profileImageDbReference = firebaseInfo.getCurrentUserProfileImageDbReference();
        nicknameDbReference = firebaseInfo.getCurrentUserNicknameDbReference();
        DatabaseReference competitiveImageDbReference = firebaseInfo.getCurrentUserCompetitiveImageDbReference();

        profileImageSReference = firebaseInfo.getProfileImagesSReference().child(profileImageFileName);

        profileImageDbReference.setValue(profileImageFileName);
        competitiveImageDbReference.setValue(competitiveImageFileName);
    }

    private void updateUserNickname(){
        if(nicknameIsComplete()) {
            final String nickname = editNickname.getText().toString().trim();
            nicknameDbReference.setValue(nickname).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        DatabaseReference reference = firebaseInfo.getNicknamesDbReference();
                        reference.child(nickname).setValue(firebaseInfo.getCurrentUser().getUid());
                        toChooseActivity();
                    } else {
                        editNickname.setError("A user with this name already exist!");
                    }
                }
            });
        }
    }

    private boolean nicknameIsComplete(){
        String nickname = editNickname.getText().toString().trim();
        if(nickname.length() < 3){
            editNickname.setError("Nickname < 3");
            return false;
        }else if(nickname.length() > 12){
            editNickname.setError("Nickname > 12");
            return false;
        }
        return true;
    }

    private void toChooseActivity(){
        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
        startActivity(intent);
    }

    public void onFinishButtonClick(View view){
        if(!firebaseInfo.isSignedIn()) {
            createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString());
        }else {
            if(localProfileImageUri != null) uploadPic(getImageBA(localProfileImageUri));
            updateUserNickname();
        }

    }

    public void onChooseProfileImageButtonClick(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_FROM_GALLERY_REQUEST);
    }

}
