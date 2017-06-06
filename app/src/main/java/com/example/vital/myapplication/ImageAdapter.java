package com.example.vital.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vital.myapplication.activities.Image;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by werton on 03.06.17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    private List<Image> mImage;
    private List<User> mUser;
    private Context context;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    public ImageAdapter(Context context, List<Image> images, List<User> users){
        this.mImage = images;
        this.mUser = users;
        this.context = context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if(viewType==TYPE_MOVIE){

            return new ViewHolder(inflater.inflate(R.layout.image,parent,false));

        }else{
            return new LoadHolder(inflater.inflate(R.layout.progress_bar,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder instanceof ViewHolder)
        {
            Log.w("BindViewHolder", String.valueOf(position));
            Image image = mImage.get(position);
            User user = mUser.get(position);
            FirebaseInfo firebaseInfo = FirebaseInfo.getInstance();

            final ImageButton profileImageButton = holder.profileImageButton;
            firebaseInfo.getProfileImagesSReference().child(user.getProfileImageFileName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri)
                            .resize(profileImageButton.getWidth(), profileImageButton.getHeight()).into(profileImageButton);
                }
            });

            final ImageView competitiveImageView = holder.competitiveImageView;
            final int curentHeight = (competitiveImageView.getWidth() * image.getHeight()) / image.getWidth();
            competitiveImageView.setMinimumHeight(curentHeight);
            firebaseInfo.getImagesSReference().child(user.getCompetitiveImageFileName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri)
                            .resize(competitiveImageView.getWidth(), competitiveImageView.getHeight()).into(competitiveImageView);
                }
            });

            TextView nicknameTextView = holder.nicknameTextView;
            nicknameTextView.setText(user.getNickname());

            TextView likeTextView = holder.likeTextView;
            likeTextView.setText(String.valueOf(image.getLikeCount()));
        }else if (holder instanceof LoadHolder){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }




    }

    @Override
    public int getItemViewType(int position) {
        int viewtype = TYPE_MOVIE;
        if (position < mUser.size()){
            viewtype = TYPE_MOVIE;
        }else if (position  == mUser.size()){
            viewtype = TYPE_LOAD;
        }
        return viewtype;
    }

    @Override
    public int getItemCount() {
        return mImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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

    interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
    private class LoadHolder extends ViewHolder {
        public LoadHolder(View itemview) {
            super(itemview);
        }
    }
}
