package com.example.vital.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vital.myapplication.activities.Image;
import com.github.florent37.camerafragment.widgets.RecordButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FragmentCamera extends Fragment{

    private ImageSurfaceView mImageSurfaceView;
    private int checkFlashState = 0;
    private int currentCameraId = 0;
    private Camera camera;
    private RecordButton tempButton;
    private ImageButton gallery;
    private ImageButton close;
    private ImageButton check;
    private ImageButton flash;
    private ImageButton switchCamera;
    private FrameLayout cameraPreviewLayout;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragmentcamerap2, container, false);
        tempButton = (RecordButton) rootView.findViewById(R.id.record_button);
        cameraPreviewLayout = (FrameLayout) rootView.findViewById(R.id.cp2);
        flash = (ImageButton) rootView.findViewById(R.id.flashButton);
        flash.setBackgroundColor(Color.TRANSPARENT);
        switchCamera = (ImageButton) rootView.findViewById(R.id.switchCamera);
        switchCamera.setBackgroundColor(Color.TRANSPARENT);
        camera = checkDeviceCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        gallery = (ImageButton) rootView.findViewById(R.id.gallery);
        close = (ImageButton) rootView.findViewById(R.id.close);
        check = (ImageButton) rootView.findViewById(R.id.check);
        close.setEnabled(false);
        check.setEnabled(false);
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_RED_EYE);
        parameters.setAutoWhiteBalanceLock(false);           //автоматический баланс белого хз работает ли
        camera.setParameters(parameters);
        gallery.setBackgroundColor(Color.TRANSPARENT);
        close.setBackgroundColor(Color.TRANSPARENT);
        check.setBackgroundColor(Color.TRANSPARENT);
        tempButton.setBackgroundResource(R.drawable.take_photo_button);
        tempButton.setBackgroundResource(R.drawable.circle_frame_background);
        mImageSurfaceView = new ImageSurfaceView(getContext(), camera, getActivity(), Camera.CameraInfo.CAMERA_FACING_BACK);
        cameraPreviewLayout.addView(mImageSurfaceView);

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addConfirmationButton();
                camera.takePicture(null, null, pictureCallback);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConirmationButton();
                camera.startPreview();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cameraPreviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(autoFocusCallback);
            }
        });

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (checkFlashState){
                    case 0 :
                        setFlash(Camera.Parameters.FLASH_MODE_OFF);
                        changeFlashMode();
                        flash.setImageResource(R.mipmap.ic_flash_off);
                        break;

                    case 1:
                        setFlash(Camera.Parameters.FLASH_MODE_ON);
                        changeFlashMode();
                        flash.setImageResource(R.mipmap.ic_flash_on);
                        break;

                    case 2 :
                        setFlash(Camera.Parameters.FLASH_MODE_AUTO);
                        changeFlashMode();
                        flash.setImageResource(R.mipmap.ic_flash_auto);
                        break;
                }
            }
        });

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                }
                else {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }

                cameraPreviewLayout.removeAllViews();
                camera = camera.open(currentCameraId);
                mImageSurfaceView = new ImageSurfaceView(getContext(), camera, getActivity(), currentCameraId);
                cameraPreviewLayout.addView(mImageSurfaceView); // artem zyeballaaa

            }
        });

        boolean nav = ViewConfiguration.get(getContext()).hasPermanentMenuKey();
        if(!nav){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tempButton.getLayoutParams();
            Resources resources = getContext().getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            layoutParams.setMargins(0, 0, 0, height);
            tempButton.setLayoutParams(layoutParams);
        }

        return  rootView;
    }

    private void addConfirmationButton() {
        tempButton.setEnabled(false);
        flash.setEnabled(false);
        gallery.setEnabled(false);
        switchCamera.setEnabled(false);
        tempButton.setVisibility(View.INVISIBLE);
        gallery.setVisibility(View.INVISIBLE);
        switchCamera.setVisibility(View.INVISIBLE);
        flash.setVisibility(View.INVISIBLE);
        check.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        check.setEnabled(true);
        close.setEnabled(true);
    }
    private void deleteConirmationButton() {
        tempButton.setEnabled(true);
        flash.setEnabled(true);
        gallery.setEnabled(true);
        switchCamera.setEnabled(true);
        tempButton.setVisibility(View.VISIBLE);
        gallery.setVisibility(View.VISIBLE);
        switchCamera.setVisibility(View.VISIBLE);
        flash.setVisibility(View.VISIBLE);
        check.setVisibility(View.INVISIBLE);
        close.setVisibility(View.INVISIBLE);
        check.setEnabled(false);
        close.setEnabled(false);
    }

    public static FragmentCamera newInstance(){
        return new FragmentCamera();
    }


    private Camera checkDeviceCamera(int id){
        Camera mCamera = null;

            try {
                mCamera = Camera.open(id);
            } catch (Exception e) {
                e.printStackTrace();
            }

        return mCamera;
    }

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            camera.autoFocus(null);
        }
    };

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            final FirebaseInfo instance = FirebaseInfo.getInstance();
            final String fileName = UUID.randomUUID().toString() + ".jpeg";
            final StorageReference imageRef = instance.getImagesSReference().child(fileName);
            imageRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Image image = new Image(fileName, instance.getCurrentUserId(), 1080, 1080);
                            image.setImageUri(uri.toString());
                            DatabaseReference push = instance.getImagesDbReference().push();
                            push.setValue(image);
                            instance.getViewsDbReference().child(push.getKey()).child("time").setValue(ServerValue.TIMESTAMP);
                            instance.getViewsDbReference().child(push.getKey()).child("view").setValue(0l);
                        }
                    });
                }
            });


        }
    };

    private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return resizedBitmap;
    }

    void changeFlashMode(){

        if (checkFlashState == 2){
            checkFlashState = 0;
        }else {
            checkFlashState++;
        }
    }

    void setFlash(String value){
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(value);
        camera.setParameters(parameters);
    }

}

