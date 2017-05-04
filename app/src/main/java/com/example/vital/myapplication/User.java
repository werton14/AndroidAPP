package com.example.vital.myapplication;

/**
 * Created by werton on 04.05.17.
 */

public class User {

    private String nickname;
    private String profileImageFileName;
    private String competitiveImageFileName;

    public User(){
        nickname = null;
        profileImageFileName = null;
        competitiveImageFileName = null;
    }

    public User(String nickname, String profileImageFileName, String competitiveImageFileName){
        this.nickname = nickname;
        this.profileImageFileName = profileImageFileName;
        this.competitiveImageFileName = competitiveImageFileName;
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

    public String getCompetitiveImageFileName() {
        return competitiveImageFileName;
    }

    public void setCompetitiveImageFileName(String competitiveImageFileName) {
        this.competitiveImageFileName = competitiveImageFileName;
    }
}
