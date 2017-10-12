package com.example.vital.myapplication;

/**
 * Created by werton on 03.10.17.
 */

import android.hardware.Camera;
import android.content.Context;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import static android.os.Build.VERSION.SDK;

public class ImageSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera camera;
    private SurfaceHolder surfaceHolder;

    public ImageSurfaceView(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                this.camera.setDisplayOrientation(90);
            }
            this.camera.setPreviewDisplay(holder);
            this.camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters parameters = camera.getParameters();
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            parameters.setPreviewSize(height, width);
        }else {
            parameters.setPreviewSize(width, height);
        }
        camera.setParameters(parameters);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.camera.stopPreview();
        this.camera.release();
    }
}