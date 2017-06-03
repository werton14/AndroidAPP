package com.example.vital.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vital.myapplication.activities.Image;

import java.util.List;

/**
 * Created by werton on 03.06.17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private List<Image> mImage;
    private List<User> mUser;

    public ImageAdapter(List<Image> images, List<User> users){
        this.mImage = images;
        this.mUser = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View imageView = inflater.inflate(R.layout.image, parent, false);

        ViewHolder viewHolder = new ViewHolder(imageView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mImage.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageButton profileImageButton;
        public ImageButton optionImageButton;
        public ImageButton likeImageButton;
        public ImageButton downloadImageButton;
        public ImageView competitiveImageView;
        public TextView nicknameTextView;
        public TextView likeTextView;

        public ViewHolder(View itemView){
            super(itemView);

            profileImageButton = (ImageButton) itemView.findViewById(R.id.profile_image_button);
            optionImageButton = (ImageButton) itemView.findViewById(R.id.option_image_button);
            likeImageButton = (ImageButton) itemView.findViewById(R.id.like_image_button);
            downloadImageButton = (ImageButton) itemView.findViewById(R.id.download_image_button);
            competitiveImageView = (ImageView) itemView.findViewById(R.id.competitive_image_view);
            nicknameTextView = (TextView) itemView.findViewById(R.id.nickname_text_view);
            likeTextView = (TextView) itemView.findViewById(R.id.like_text_view);
        }
    }
}
