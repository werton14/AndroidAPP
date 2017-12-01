package com.example.vital.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by werton on 01.12.17.
 */

public class ImageAdapterGridView extends BaseAdapter {
    private Context mContext;

    public ImageAdapterGridView(Context c) {
        mContext = c;
    }

    public int getCount() {
        return 40; // count of rows
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView mImageView;

        if (convertView == null) {
            mImageView = new ImageView(mContext);
            mImageView.setLayoutParams(new GridView.LayoutParams(130, 130));
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setPadding(10, 10, 10, 10);
        } else {
            mImageView = (ImageView) convertView;
        }
        mImageView.setImageResource(R.mipmap.ic_cake);
        return mImageView;
    }
}
