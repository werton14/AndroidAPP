package com.example.vital.myapplication.activities;

import android.app.Activity;
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

import com.example.vital.myapplication.R;
import com.example.vital.myapplication.activities.ChooseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;



public class NicknameActivity extends AppCompatActivity {

    private EditText nickNameEdit;
    private ImageButton chooseImageFromGalleryButton;

    private StorageReference storageReference;
    private DatabaseReference uploadUrlReference;
    private DatabaseReference usernameReference;
    private DatabaseReference usernameListReference;
    private UploadTask uploadProfileImage;
    private String userId;
    private String storageFileName;

    final private int PHOTO_FROM_GALLERY_REQUEST = 233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityname);

        nickNameEdit = (EditText) findViewById(R.id.nicknameEdit);
        chooseImageFromGalleryButton = (ImageButton) findViewById(R.id.choose_image_from_gallery);
        initFirebaseComponent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_FROM_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            startCropActivity(selectedImage);
        }if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri croppedImage = result.getUri();
            uploadProfileImage = uploadPic(getImageBA(croppedImage));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initFirebaseComponent(){
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageFileName = UUID.randomUUID().toString() + ".png";
        uploadProfileImage = null;
        uploadUrlReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("profilePhotoUrl");
        usernameReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("username");
        usernameListReference = FirebaseDatabase.getInstance().getReference().child("usernames");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private String getNickName(){
         return nickNameEdit.getText().toString().trim();
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
        chooseImageFromGalleryButton.setImageBitmap(img);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private UploadTask uploadPic(byte[] imageBA){
        StorageReference reference = storageReference.child("userProfileImage").child(storageFileName);
        UploadTask uploadTask = reference.putBytes(imageBA);
        return  uploadTask;
    }

    private boolean nickNamefieldIsComplete(){
        return getNickName().trim().length() > 3;
    }

    public void onFinishButtonClick(View view){
        if (uploadProfileImage != null && uploadProfileImage.isSuccessful() && nickNamefieldIsComplete()) {
            uploadUrlReference.setValue(storageFileName);
            final Activity activity = this;
            usernameReference.setValue(getNickName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        usernameListReference.child(getNickName()).setValue(userId);
                        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
                        startActivity(intent);
                        activity.finish();
                    }else{
                        nickNameEdit.setError("A user with this name already exist!");
                    }
                }
            });

        }
    }

    public void onChooseImageFromGalleryButtonClick(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_FROM_GALLERY_REQUEST);
    }
}