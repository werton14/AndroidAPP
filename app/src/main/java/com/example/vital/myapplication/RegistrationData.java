package com.example.vital.myapplication;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by werton on 16.06.17.
 */

public class RegistrationData implements Serializable {

    private String nickname;
    private String email;
    private String password;
    private Uri profileImageUri;

    public RegistrationData(){
        nickname = "";
        email = "";
        password = "";
        profileImageUri = null;

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Uri getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(Uri profileImageUri) {
        this.profileImageUri = profileImageUri;
    }
}
