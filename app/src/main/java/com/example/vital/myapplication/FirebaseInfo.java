package com.example.vital.myapplication;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by qwert on 4/19/2017.
 */

public class FirebaseInfo {
    private static FirebaseInfo instance;

    private final String VIEWS_DATABASE_KEY = "views";
    private final String IMAGES_DATABASE_KEY = "images";
    private final String USERS_DATABASE_KEY = "users";
    private final String TASK_DATABASE_KEY = "task";
    private final String PROFILE_IMAGE_DATABASE_KEY = "profileImage";
    private final String NICKNAMES_DATABASE_KEY = "nicknames";
    private final String NICKNAME_DATABASE_KEY = "nickname";
    private final String COMPETITIVE_IMAGE_DATABASE_KEY = "competitiveImage";
    private final String WHO_LIKED_IMAGE_DATABASE_KEY = "whoLikedImage";
    private final String IMAGES_STORAGE_KEY = "images";
    private final String PROFILE_IMAGE_STORAGE_KEY = "profileImages";

    private boolean isSignedIn;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;
    private DatabaseReference taskDbReference;
    private DatabaseReference viewsDbReference;
    private DatabaseReference imagesDbReference;
    private DatabaseReference usersDbReference;
    private DatabaseReference nicknamesDbReference;
    private DatabaseReference currentUserDbReference;
    private DatabaseReference currentUserProfileImageDbReference;
    private DatabaseReference currentUserNicknameDbReference;
    private DatabaseReference currentUserCompetitiveImageDbReference;
    private DatabaseReference whoLikedImageDbReference;

    private StorageReference storageReference;
    private StorageReference imagesSReference;
    private StorageReference profileImagesSReference;

    private String currentUserId;

    private FirebaseInfo(){
        firebaseAuth = FirebaseAuth.getInstance();
        isSignedIn = firebaseAuth.getCurrentUser() != null;
        firebaseAuth.addAuthStateListener(makeAuthStateListener());

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        viewsDbReference = databaseReference.child(VIEWS_DATABASE_KEY);
        taskDbReference = databaseReference.child(TASK_DATABASE_KEY);
        imagesDbReference = databaseReference.child(IMAGES_DATABASE_KEY);
        usersDbReference = databaseReference.child(USERS_DATABASE_KEY);
        nicknamesDbReference = databaseReference.child(NICKNAMES_DATABASE_KEY);

        whoLikedImageDbReference = databaseReference.child(WHO_LIKED_IMAGE_DATABASE_KEY);

        imagesSReference = storageReference.child(IMAGES_STORAGE_KEY);
        profileImagesSReference = storageReference.child(PROFILE_IMAGE_STORAGE_KEY);

    }

    public static FirebaseInfo getInstance(){
        if(instance == null){
            synchronized (FirebaseInfo.class){
                if(instance == null){
                    instance = new FirebaseInfo();
                }
            }
        }
        return instance;
    }

    public DatabaseReference getUsersDbReference() {
        return usersDbReference;
    }

    public DatabaseReference getImagesDbReference() {

        return imagesDbReference;
    }

    public DatabaseReference getViewsDbReference() {

        return viewsDbReference;
    }

    public DatabaseReference getCurrentUserDbReference() {
        return currentUserDbReference;
    }

    public DatabaseReference getCurrentUserCompetitiveImageDbReference() {
        return currentUserCompetitiveImageDbReference;
    }

    public DatabaseReference getCurrentUserNicknameDbReference() {

        return currentUserNicknameDbReference;
    }

    public DatabaseReference getCurrentUserProfileImageDbReference() {

        return currentUserProfileImageDbReference;
    }

    public FirebaseUser getCurrentUser() {

        return currentUser;
    }

    public DatabaseReference getDatabaseReference() {

        return databaseReference;
    }

    public DatabaseReference getNicknamesDbReference() {
        return nicknamesDbReference;
    }

    public DatabaseReference getTaskDbReference() {
        return taskDbReference;
    }

    public StorageReference getProfileImagesSReference() {
        return profileImagesSReference;
    }

    public StorageReference getImagesSReference() {
        return imagesSReference;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }

    public boolean isSignedIn() {
        return isSignedIn;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    private FirebaseAuth.AuthStateListener makeAuthStateListener(){
        return new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    isSignedIn = true;
                    updateFirebaseInfoForCurrentUser();
                }else{
                    isSignedIn = false;
                    updateFirebaseInfoForCurrentUser();
                }
            }
        };
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public DatabaseReference getWhoLikedImageDbReference() {
        return whoLikedImageDbReference;
    }

    private void updateFirebaseInfoForCurrentUser(){
        if(isSignedIn){
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            currentUserId = currentUser.getUid();
            currentUserDbReference = usersDbReference.child(currentUser.getUid());
            currentUserProfileImageDbReference = currentUserDbReference.child(PROFILE_IMAGE_DATABASE_KEY);
            currentUserNicknameDbReference = currentUserDbReference.child(NICKNAME_DATABASE_KEY);
            currentUserCompetitiveImageDbReference = currentUserDbReference.child(COMPETITIVE_IMAGE_DATABASE_KEY);
        }else{
            currentUser = null;
            currentUserId = null;
            currentUserDbReference = null;
            currentUserProfileImageDbReference = null;
            currentUserNicknameDbReference = null;
            currentUserCompetitiveImageDbReference = null;
        }
    }
}
