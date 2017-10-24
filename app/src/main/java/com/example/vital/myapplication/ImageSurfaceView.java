package com.example.vital.myapplication;

/**
 * Created by werton on 03.10.17.
 */

import android.app.Activity;
import android.hardware.Camera;
import android.content.Context;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION.SDK;

public class ImageSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private int rotation;
    private Activity activity;
    private int cameraRotation;
    private int imageRotation = -1;
    private Camera.CameraInfo cameraInfoBack;
    private Camera.CameraInfo cameraInfoFront;
    private OrientationEventListener orientationEventListener;
    private int cameraFacing;

    public ImageSurfaceView(Context context, final Camera camera, final Activity activity, final int cameraFacing) {
        super(context);
        this.camera = camera;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.cameraFacing = cameraFacing;
        this.activity = activity;
        this.rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        this.cameraRotation = getCorrectCameraOrientation(getCameraInfo(cameraFacing), camera);
        orientationEventListener = new OrientationEventListener(getContext(), SensorManager.SENSOR_DELAY_UI) {
            @Override
            public void onOrientationChanged(int orientation) {
                int angle = 0;
                if(orientation > 335 || orientation < 25){
                    Log.d(TAG, "onOrientationChanged: 1");
                    if(cameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        angle = cameraRotation;
                    }else {
                        angle = cameraRotation + 180;
                    }

                } else if(orientation < 115 && orientation > 65){
                    Log.d(TAG, "onOrientationChanged: 2");
                    angle = cameraRotation + 90;
                } else if(orientation < 295 && orientation > 245){
                    Log.d(TAG, "onOrientationChanged: 3");
                    angle = cameraRotation - 90;
                } else if(orientation < 205 && orientation > 155) {
                    Log.d(TAG, "onOrientationChanged: 4");
                    angle = cameraRotation;
                }
                if(angle == 360) angle = 0;
                if (angle > 360) angle = angle - 360;
                if(angle < 0) angle = angle + 360;
                Log.d(TAG, "onOrientationChanged: angle " + String.valueOf(angle));
                Log.d(TAG, "onOrientationChanged: cameraRotation " + String.valueOf(cameraRotation));
                if(angle != imageRotation){
                        imageRotation = angle;
                        Camera.Parameters parameters = camera.getParameters();
                        parameters.setRotation(imageRotation);
                        camera.setParameters(parameters);
                }
                Log.w("cameraRotation", String.valueOf(cameraRotation));
            }
        };
        orientationEventListener.enable();// artem zayebalaaaaa yebala
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            this.camera.setPreviewDisplay(holder);
            this.camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters parameters = camera.getParameters();
        Log.w("sizes", "width: " + String.valueOf(width) + " height: " + String.valueOf(height));
        Log.w("surfaceRotation", String.valueOf(rotation));
        if(cameraRotation == 90 || cameraRotation == 270){
            parameters.setPreviewSize(height, width);
        }else {
            parameters.setPreviewSize(width, height);
        }
        camera.setDisplayOrientation(cameraRotation);
        parameters.setPictureSize(1080, 1080);
        parameters.setJpegQuality(30);
        camera.setParameters(parameters);
        camera.startPreview();
    }
//TODO faceDetection
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        orientationEventListener.disable();
        this.camera.stopPreview();
        this.camera.release();
    }

    private Camera.CameraInfo getCameraInfo(int cameraFacing) {
        Camera.CameraInfo cameraInfo = null;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == cameraFacing) {
                cameraInfo = info;
            }
        }
        return cameraInfo;
    }

    public int getCorrectCameraOrientation(Camera.CameraInfo info, Camera camera) {

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch(rotation){
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;

        }

        int result;
        if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        }else{
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }
}