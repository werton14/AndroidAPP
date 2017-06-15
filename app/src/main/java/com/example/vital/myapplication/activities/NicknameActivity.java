package com.example.vital.myapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.vital.myapplication.FirebaseInfo;
import com.example.vital.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by werton on 01.05.17.
 */

public class NicknameActivity extends AppCompatActivity {

    private EditText editNickname;
    private CircleImageView chooseProfileImageButton;
    private DatabaseReference nicknamesDbReference;
    private String nickname;
    private Uri localProfileImageUri;

    final private int PHOTO_FROM_GALLERY_REQUEST = 233;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityname);

        localProfileImageUri = null;
        nicknamesDbReference = FirebaseInfo.getInstance().getNicknamesDbReference();

        editNickname = (EditText) findViewById(R.id.nickname_edit_text_on_nickname);
        chooseProfileImageButton = (CircleImageView) findViewById(R.id.choose_image_from_gallery);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_FROM_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            startCropActivity(selectedImage);
        }if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            localProfileImageUri = result.getUri();
            getImageBA(localProfileImageUri);
            //TODO // FIXME: 02.05.17 
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
        /*img = getCroppedBitmap(img);*/
        chooseProfileImageButton.setImageBitmap(img);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        return baos.toByteArray();
    }

    private void toSignUpActivity(){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        if(localProfileImageUri != null) intent.putExtra("imageBA", getImageBA(localProfileImageUri));
        else intent.putExtra("imageBA", (byte[]) null);
        intent.putExtra("nickname", nickname);
        startActivity(intent);
    }

    private void checkNicknameAvailable(){
        final String n = editNickname.getText().toString().trim();
        if(n.length() < 3){
            editNickname.setError("Nickname short!");
        }else if(n.length() > 25){
            editNickname.setError("Nickname long!");
        }else {
            nicknamesDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(n)) {
                        nickname = n;
                        toSignUpActivity();
                    }else {
                        editNickname.setError("User with this nickname already exist!");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void onNextButtonClick(View view){
        checkNicknameAvailable();
    }

    public void onChooseProfileImageButtonClick(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_FROM_GALLERY_REQUEST);
    }
}
