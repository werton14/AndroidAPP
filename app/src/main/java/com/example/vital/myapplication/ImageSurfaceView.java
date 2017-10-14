package com.example.vital.myapplication;

/**
 * Created by werton on 03.10.17.
 */

import android.app.Activity;
import android.hardware.Camera;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import static android.os.Build.VERSION.SDK;

public class ImageSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private int cameraRotation;
    private Activity activity;

    public ImageSurfaceView(Context context, Camera camera, int cameraRotation, Activity activity) {
        super(context);
        this.camera = camera;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.cameraRotation = cameraRotation;
        this.activity = activity;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            this.camera.setPreviewDisplay(holder);
            this.camera.setDisplayOrientation(cameraRotation);
            this.camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters parameters = camera.getParameters();
        Log.w("sizes", "width: " + String.valueOf(width) + " height: " + String.valueOf(height));

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        switch(rotation){
            case Surface.ROTATION_0:
                parameters.setPreviewSize(height, width);
                Log.w("rotation", "0");
                break;

            case Surface.ROTATION_90:
                parameters.setPreviewSize(width, height);
                Log.w("rotation", "90");
                break;

            case Surface.ROTATION_180:
                parameters.setPreviewSize(height, width);
                Log.w("rotation", "180");
                break;

            case Surface.ROTATION_270:
                parameters.setPreviewSize(width, height);
                Log.w("rotation", "270");
                break;
        }
        camera.setParameters(parameters);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.camera.stopPreview();
        this.camera.release();
    }
}