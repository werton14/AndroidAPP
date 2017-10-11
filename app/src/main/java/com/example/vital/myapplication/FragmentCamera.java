package com.example.vital.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FragmentCamera extends Fragment {

    private CameraManager cameraManager;
    private SurfaceView surfaceView;
    private String backCameraId;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragmentcamera, container, false);
        Button tempButton = (Button) rootView.findViewById(R.id.take_photo);
        surfaceView = (SurfaceView) rootView.findViewById(R.id.camera_preview);

        cameraManager = rootView.getContext().getSystemService(CameraManager.class);
        backCameraId = getBackCameraId();
        CameraCharacteristics cameraCharacteristics = null;
        try {
            cameraCharacteristics = cameraManager.getCameraCharacteristics(backCameraId);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        StreamConfigurationMap configs = cameraCharacteristics
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (ActivityCompat.checkSelfPermission(rootView.getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        try {
            cameraManager.openCamera(backCameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    try {
                        CaptureRequest.Builder captureRequest = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        Surface surface = surfaceView.getHolder().getSurface();
                        captureRequest.addTarget(surface);
                        captureRequest.build();
                        camera.createCaptureSession(Collections.singletonList(surface),
                                new CameraCaptureSession.StateCallback() {
                                    @Override
                                    public void onConfigured(@NonNull CameraCaptureSession session) {
                                    }

                                    @Override
                                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                                    }
                                }, null);
                        Log.w("onBuild", "hire me please");
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {

                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return  rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String getBackCameraId(){
        String[] cameraIds = null;
        try {
            cameraIds = cameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        for(String cameraId : cameraIds){
            CameraCharacteristics characteristics = null;
            try {
                characteristics = cameraManager.getCameraCharacteristics(cameraId);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            int cameraOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
            if(cameraOrientation == CameraCharacteristics.LENS_FACING_BACK) return cameraId;
        }
        return null;
    }


}

