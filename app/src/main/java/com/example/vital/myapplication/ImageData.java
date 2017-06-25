package com.example.vital.myapplication;

import android.net.Uri;

import com.example.vital.myapplication.activities.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by werton on 25.06.17.
 */

public class ImageData {

    private List<String> imageIds = new ArrayList<String>();
    private List<Image> images = new ArrayList<Image>();
    private List<User> users = new ArrayList<User>();
    private List<Uri> imageUris = new ArrayList<Uri>();
    private List<Uri> profileUris = new ArrayList<Uri>();

    ImageData(){}

    public void addImageId(String id){
        imageIds.add(id);
    }

    public void addImage(Image image){
        images.add(image);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addImageUri(Uri imageUri){
        imageUris.add(imageUri);
    }

    public void addProfileUri(Uri profileUri){
        profileUris.add(profileUri);
    }

    public List<String> getImageIds() {
        return imageIds;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Uri> getImageUris() {
        return imageUris;
    }

    public List<Uri> getProfileUris() {
        return profileUris;
    }
}
