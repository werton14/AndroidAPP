package com.example.vital.myapplication;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
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

public class ActivityNickname extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private byte image [];
    private EditText firstNameEdit;
    private EditText secondNameEdit;
    private ImageButton chooseImageFromGalleryButton;
    private DatabaseReference uploadUrlReference;
    private DatabaseReference saveUrl;
    private DatabaseReference usernameReference;
    private DatabaseReference usernameListReference;
    private String fileName;


    final private int PHOTO_FROM_GALLERY_REQUEST = 233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityname);
        firebaseAuth = FirebaseAuth.getInstance();

        fileName = UUID.randomUUID().toString() + ".png";
        uploadUrlReference = FirebaseDatabase.getInstance().getReference().child("userProfilePhotoUrl").push();
        saveUrl = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profilePhotoUrl");
        usernameReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username");
        usernameListReference = FirebaseDatabase.getInstance().getReference().child("usernames");
        image = null;
        firebaseStorage = FirebaseStorage.getInstance();
        chooseImageFromGalleryButton = (ImageButton) findViewById(R.id.choose_image_from_gallery);
        chooseImageFromGalleryButton.setOnClickListener(getGalleryButtonOnClickListener());
        firstNameEdit = (EditText) findViewById(R.id.firstname);

        Button finish = (Button) findViewById(R.id.finish);
        finish.setOnClickListener(getFinishButtonOnClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_FROM_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            cropPic(selectedImage);
        }if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri croppedImage = result.getUri();
            savePic(croppedImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getNickName(){
        firstNameEdit = (EditText) findViewById(R.id.firstname);
        String firstName = firstNameEdit.getText().toString().trim();

        secondNameEdit = (EditText) findViewById(R.id.secondname);
        String secondName = secondNameEdit.getText().toString().trim();

        return firstName + ' ' + secondName;
    }

    private void cropPic(Uri selectedImage){
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

    private void savePic(Uri selectedImage){
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
        image = baos.toByteArray();
    }

    private UploadTask uploadPic(StorageReference reference){
        UploadTask uploadTask = reference.putBytes(image);
        return  uploadTask;
    }

    private boolean fieldsIsComplete(){
        return firstNameEdit.getText().toString().trim().length() > 3 &&
                secondNameEdit.getText().toString().trim().length() > 3;
    }

    private View.OnClickListener getFinishButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StorageReference reference = firebaseStorage.getReference().child("userProfileImage").child(fileName);
                final UploadTask uploadPic = uploadPic(reference);
                uploadPic.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            uploadUrlReference.setValue(fileName);
                            saveUrl.setValue(uploadUrlReference.getKey());
                            usernameReference.setValue(firstNameEdit.getText().toString());
                            usernameListReference.child(firstNameEdit.getText().toString()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                            Intent intent = new Intent(getApplicationContext(), ActivityChoose.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        };
    }

    private View.OnClickListener getGalleryButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_FROM_GALLERY_REQUEST);
            }
        };
    }
}
