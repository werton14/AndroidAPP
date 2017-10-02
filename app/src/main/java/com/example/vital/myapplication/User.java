package com.example.vital.myapplication;

/**
 * Created by werton on 04.05.17.
 */

public class User {

    private String nickname;
    private String profileImageFileName;
        private String description;
    private long taskNumber = 0;

    public User(){
        nickname = null;
        profileImageFileName = null;
        description = null;
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

    public long getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(long taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
