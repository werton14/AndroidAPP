package com.example.vital.myapplication;

/**
 * Created by werton on 04.05.17.
 */

public class User {

    private String nickname;
    private String profileImageFileName;

    public User(){
        nickname = null;
        profileImageFileName = null;
    }

    public User(String nickname, String profileImageFileName){
        this.nickname = nickname;
        this.profileImageFileName = profileImageFileName;

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImageFileName() {
        return profileImageFileName;
    }

    public void setProfileImageFileName(String profileImageFileName) {
        this.profileImageFileName = profileImageFileName;
    }

}
