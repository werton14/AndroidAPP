package com.example.vital.myapplication.activities;

import android.util.Size;

import java.util.List;

/**
 * Created by werton on 04.05.17.
 */

public class Image {
    private String userId;
    private long likeCount;
    private int width;
    private int height;

    public Image(String userId, int width, int height){
        this.userId = userId;
        this.likeCount = 0L;
        this.width = width;
        this.height = height;
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
