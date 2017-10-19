package com.example.vital.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vital.myapplication.activities.Image;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultAdapter;
import com.github.florent37.camerafragment.widgets.FlashSwitchView;
import com.github.florent37.camerafragment.widgets.RecordButton;

import java.io.IOException;
import java.security.Policy;

import static android.content.ContentValues.TAG;

public class FragmentCamera extends Fragment{

    private ImageSurfaceView mImageSurfaceView;
    private int checkFlashState = 0;
    private int currentCameraId = 0;
    private Camera camera;
    private ImageButton flash;
    private ImageButton switchCamera;
    private FrameLayout cameraPreviewLayout;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragmentcamerap2, container, false);
        final RecordButton tempButton = (RecordButton) rootView.findViewById(R.id.record_button);
        cameraPreviewLayout = (FrameLayout) rootView.findViewById(R.id.cp2);
        flash = (ImageButton) rootView.findViewById(R.id.flashButton);
        flash.setBackgroundColor(Color.TRANSPARENT);
        switchCamera = (ImageButton) rootView.findViewById(R.id.switchCamera);
        switchCamera.setBackgroundColor(Color.TRANSPARENT);
        camera = checkDeviceCamera();

        final Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_RED_EYE);
        camera.setParameters(parameters);

        tempButton.setBackgroundResource(R.drawable.take_photo_button);
        tempButton.setBackgroundResource(R.drawable.circle_frame_background);
        mImageSurfaceView = new ImageSurfaceView(getContext(), camera, getActivity());
        if(cameraPreviewLayout == null) Log.w(TAG, "PreviewLayout is null");
        cameraPreviewLayout.addView(mImageSurfaceView);

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
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
                        flash.setImageResource(R.drawable.ic_flash_off_white_24dp);
                        break;

                    case 1:
                        setFlash(Camera.Parameters.FLASH_MODE_ON);
                        changeFlashMode();
                        flash.setImageResource(R.drawable.ic_flash_on_white_24dp);
                        break;

                    case 2 :
                        setFlash(Camera.Parameters.FLASH_MODE_AUTO);
                        changeFlashMode();
                        flash.setImageResource(R.drawable.ic_flash_auto_white_24dp);
                        break;
                }
            }
        });

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                camera.stopPreview();
                camera.release();

                if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                }
                else {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }

                camera = Camera.open(currentCameraId);

                try {

                    camera.setPreviewDisplay(mImageSurfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                camera.startPreview();

            }
        });

        return  rootView;
    }


    public static FragmentCamera newInstance(){
        return new FragmentCamera();
    }


    private Camera checkDeviceCamera(){
        Camera mCamera = null;

            try {
                mCamera = Camera.open(0);
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
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if(bitmap==null){
                Toast.makeText(getContext(), "Captured image is empty", Toast.LENGTH_LONG).show();
                return;
            }
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

