package com.example.vital.myapplication.activities;

import android.util.Size;

import java.util.List;

/**
 * Created by werton on 04.05.17.
 */

public class Image {
    private String competitiveImageFileName;
    private String userId;
    private long likeCount;
    private int width;
    private int height;

    public Image(){
        competitiveImageFileName = null;
        userId = null;
        likeCount = -1L;
        width = -1;
        height = -1;
    }

    public Image(String competitiveImageFileName, String userId, int width, int height){
        this.competitiveImageFileName = competitiveImageFileName;
        this.userId = userId;
        this.likeCount = 0L;
        this.width = width;
        this.height = height;
    }

    public String getCompetitiveImageFileName() {
        return competitiveImageFileName;
    }

    public void setCompetitiveImageFileName(String competitiveImageFileName) {
        this.competitiveImageFileName = competitiveImageFileName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCoutn) {
        this.likeCount = likeCoutn;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
