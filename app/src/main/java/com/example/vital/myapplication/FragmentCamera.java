package com.example.vital.myapplication;

import android.Manifest;
import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.transition.AutoTransition;
import android.support.transition.ChangeBounds;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionSet;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vital.myapplication.activities.Image;
import com.github.florent37.camerafragment.widgets.RecordButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class FragmentCamera extends Fragment{

    private RelativeLayout rootLayout;
    private ImageSurfaceView mImageSurfaceView;
    private int checkFlashState = 0;
    private int currentCameraId = 0;
    private Camera camera;
    private RecordButton tempButton;
    private ImageButton gallery;
    private ImageButton close;
    private ImageButton confirm;
    private ImageButton flash;
    private ImageButton switchCamera;
    private FrameLayout cameraPreviewLayout;
    final private int PHOTO_FROM_GALLERY_REQUEST = 233;
    private boolean tempButtonPressed = false;
    private float checkChangeOfAngle = -1;
    private float fromSwitchCamera = 0;
    private float toSwitchCamera = 0;
    private float fromRotation = 0;
    private float toRotation = 0;
    private int angle = 0;
    private int animRotation = -1;
    private byte [] imageByteArray = null;
    private byte [] imageByte = null;
    private OrientationEventListener orientationEventListener;
    private View rootView;
    private boolean galaryIsShowed = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragmentcamerap2, container, false);

        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {

            } else {
                FragmentCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        2);
            }
        }else {
            configureFragment();
        }

        return  rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.w("denied", "denied");

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            configureFragment();
        } else {
            Toast.makeText(getContext(), "Access for camera denied, please allow access for camera!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    void configureFragment(){
        rootLayout = (RelativeLayout) rootView.findViewById(R.id.fragmentt);
        tempButton = (RecordButton) rootView.findViewById(R.id.record_button);
        cameraPreviewLayout = (FrameLayout) rootView.findViewById(R.id.cp2);
        flash = (ImageButton) rootView.findViewById(R.id.flashButton);
        switchCamera = (ImageButton) rootView.findViewById(R.id.switchCamera);
        gallery = (ImageButton) rootView.findViewById(R.id.gallery);
        close = (ImageButton) rootView.findViewById(R.id.close);
        confirm = (ImageButton) rootView.findViewById(R.id.check);
        flash.setBackgroundColor(Color.TRANSPARENT);
        switchCamera.setBackgroundColor(Color.TRANSPARENT);
        gallery.setBackgroundColor(Color.TRANSPARENT);
        close.setBackgroundColor(Color.TRANSPARENT);
        confirm.setBackgroundColor(Color.TRANSPARENT);
        tempButton.setBackgroundResource(R.drawable.take_photo_button);
        tempButton.setBackgroundResource(R.drawable.circle_frame_background);
        close.setEnabled(false);
        confirm.setEnabled(false);
        camera = checkDeviceCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_RED_EYE);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
//        parameters.setAutoWhiteBalanceLock(true); /// heraborationalitisamarilolatirewenekorp
        camera.setParameters(parameters);
        mImageSurfaceView = new ImageSurfaceView(getActivity().getApplicationContext(),
                camera, getActivity(), Camera.CameraInfo.CAMERA_FACING_BACK);
        cameraPreviewLayout.addView(mImageSurfaceView);

        orientationEventListener = new OrientationEventListener(getActivity().getApplicationContext(),
                SensorManager.SENSOR_DELAY_UI) {
            @Override
            public void onOrientationChanged(int orientation) {

                if(orientation > 335 || orientation < 25){
                    angle = 0;
                }else if(orientation < 115 && orientation > 65){
                    angle = -90;
                    if (fromRotation == 180){
                        fromRotation = -180;
                    }
                }else if(orientation < 295 && orientation > 245){
                    angle = 90;
                    if (fromRotation == -180){
                        fromRotation = 180;
                    }
                }else if(orientation < 205 && orientation > 155){
                    if (fromRotation == 90) {
                        angle = 180;
                    }else if (fromRotation == -90){
                        angle = -180;
                    }

                }if(angle != animRotation && !tempButtonPressed){
                    animRotation = angle;
                    toRotation = animRotation;
                    final RotateAnimation rotateAnim = new RotateAnimation(fromRotation, toRotation,
                            switchCamera.getWidth()/2, switchCamera.getHeight()/2);
                    rotateAnim.setDuration(150);
                    rotateAnim.setFillAfter(true);
                    fromRotation = toRotation;
                    gallery.startAnimation(rotateAnim);
                    flash.startAnimation(rotateAnim);
                    switchCamera.startAnimation(rotateAnim);
                }
            }
        };
        orientationEventListener.enable();

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery.clearAnimation();
                flash.clearAnimation();
                switchCamera.clearAnimation();
                camera.takePicture(null, null, pictureCallback);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirmationButton();
                camera.startPreview();
            }
        });

        Transition transition = new AutoTransition();
        TransitionManager.go(new Scene(rootLayout), transition);
        TransitionManager.go(new Scene(cameraPreviewLayout), transition);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = null;
                if(!galaryIsShowed) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            150);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    layout.setLayoutParams(params);

                    cameraPreviewLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            getActivity().getWindow().getDecorView().getHeight() - 150));

                    galaryIsShowed = true;
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            0);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    layout.setLayoutParams(params);

                    cameraPreviewLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            getActivity().getWindow().getDecorView().getHeight()));
                    galaryIsShowed = false;
                }
            }
        });

        cameraPreviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("loh","loh");
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
                switchingCamera();
                if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                }
                else {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                cameraPreviewLayout.removeAllViews();
                camera = camera.open(currentCameraId);
                mImageSurfaceView = new ImageSurfaceView(getActivity().getApplicationContext(),
                        camera, getActivity(), currentCameraId);
                cameraPreviewLayout.addView(mImageSurfaceView); // artem zyeballaaa

            }
        });

        boolean nav = ViewConfiguration.get(getActivity().getApplicationContext())
                .hasPermanentMenuKey();
        if(!nav){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tempButton.getLayoutParams();
            Resources resources = getContext().getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            layoutParams.setMargins(0, 0, 0, height);
            tempButton.setLayoutParams(layoutParams);
        }

        final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.container);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseInfo instance = FirebaseInfo.getInstance();
                final String fileName = UUID.randomUUID().toString() + ".jpeg";
                final StorageReference imageRef = instance.getImagesSReference().child(fileName);
                imageRef.putBytes(imageByteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                                imageByteArray = null;
                                viewPager.setCurrentItem(1);
                            }
                        });
                    }
                });
            }
        });

//        LayoutTransition layoutTransition = new LayoutTransition();
//        rootLayout.setLayoutTransition(layoutTransition);
//        layoutTransition.setDuration(2000);
//        layoutTransition.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
//        layoutTransition.enableTransitionType(LayoutTransition.APPEARING);
//        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode == PHOTO_FROM_GALLERY_REQUEST && resultCode == RESULT_OK){
           cameraPreviewLayout.removeAllViews();
           ImageView imageView = new ImageView(getContext());
           cameraPreviewLayout.addView(imageView);
           Uri uri = data.getData();
           imageView.setImageURI(uri);
           imageByte = getImageBA(uri);
       }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private byte[] getImageBA(Uri selectedImage){
        Bitmap img = null;
        try {
            img = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.WEBP, 30, baos);
        return baos.toByteArray();
    }

    private void switchingCamera() {
        if (angle != checkChangeOfAngle){
            checkChangeOfAngle = angle;
            fromSwitchCamera = angle;
            toSwitchCamera = angle - 180;
        }else {
            toSwitchCamera = toSwitchCamera - 180;
        }

        final RotateAnimation switchCameraRotate = new RotateAnimation(fromSwitchCamera,toSwitchCamera,
                switchCamera.getHeight()/2,switchCamera.getWidth()/2);
        switchCameraRotate.setDuration(300);
        switchCameraRotate.setFillAfter(true);
        switchCamera.startAnimation(switchCameraRotate);
        fromSwitchCamera = toSwitchCamera;
    }

    private void addConfirmationButton() {
        tempButtonPressed = true;
        tempButton.setEnabled(false);
        flash.setEnabled(false);
        gallery.setEnabled(false);
        switchCamera.setEnabled(false);
        tempButton.setVisibility(View.INVISIBLE);
        gallery.setVisibility(View.INVISIBLE);
        switchCamera.setVisibility(View.INVISIBLE);
        flash.setVisibility(View.INVISIBLE);
        confirm.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        confirm.setEnabled(true);
        close.setEnabled(true);
    }
    private void deleteConfirmationButton() {
        tempButtonPressed = false;
        tempButton.setEnabled(true);
        flash.setEnabled(true);
        gallery.setEnabled(true);
        switchCamera.setEnabled(true);
        tempButton.setVisibility(View.VISIBLE);
        gallery.setVisibility(View.VISIBLE);
        switchCamera.setVisibility(View.VISIBLE);
        flash.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.INVISIBLE);
        close.setVisibility(View.INVISIBLE);
        confirm.setEnabled(false);
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
            imageByteArray = data;
            addConfirmationButton();
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

