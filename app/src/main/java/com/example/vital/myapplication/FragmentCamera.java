package com.example.vital.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.florent37.camerafragment.widgets.RecordButton;

import static android.content.ContentValues.TAG;

public class FragmentCamera extends Fragment{

    private ImageSurfaceView mImageSurfaceView;
    private Camera camera;

    private FrameLayout cameraPreviewLayout;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragmentcamerap2, container, false);
        RecordButton tempButton = (RecordButton) rootView.findViewById(R.id.record_button);
        cameraPreviewLayout = (FrameLayout) rootView.findViewById(R.id.cp2);
        tempButton.setBackgroundResource(R.drawable.take_photo_button);
        camera = checkDeviceCamera();
        mImageSurfaceView = new ImageSurfaceView(getContext(), camera);
        if(cameraPreviewLayout == null) Log.w(TAG, "PreviewLayout is null");
        cameraPreviewLayout.addView(mImageSurfaceView);

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
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
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

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

}

