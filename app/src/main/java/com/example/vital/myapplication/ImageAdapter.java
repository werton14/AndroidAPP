package com.example.vital.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Image> mImage;
    private List<User> mUser;
    private Context context;
    private int windowWidth;
    private static final int VIEW_TYPE_MOVE = 1;
    private static final int VIEW_TYPE_LOAD = 2;

    @Override
    public int getItemViewType(int position){
        int viewType = VIEW_TYPE_MOVE;
        Log.w("spy", String.valueOf(mUser.size()-1));
        Log.w("spy", String.valueOf(position));
        if(position < mUser.size()){
            viewType = VIEW_TYPE_MOVE;
        }
        if (position == mUser.size() - 1){
            Log.w("bugaga", String.valueOf(position));
            viewType = VIEW_TYPE_LOAD;
        }
        return viewType;
    }

    public ImageAdapter(Context context, List<Image> images, List<User> users){
        this.mImage = images;
        this.mUser = users;
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        windowWidth = display.getWidth();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case VIEW_TYPE_MOVE:
                return new ViewHolder(inflater.inflate(R.layout.image,parent,false));

            case VIEW_TYPE_LOAD:
                return new LoadHolder(inflater.inflate(R.layout.progress_bar,parent,false));
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {

        Log.w("plaha",String.valueOf(h instanceof ViewHolder));
        Log.w("plaha",String.valueOf(h instanceof LoadHolder));


        if (h instanceof ViewHolder){
            ViewHolder holder = (ViewHolder) h;
            Log.w("BindViewHolder", String.valueOf(position));
            Image image = mImage.get(position);
            User user = mUser.get(position);
            FirebaseInfo firebaseInfo = FirebaseInfo.getInstance();

            final ImageView competitiveImageView =  holder.competitiveImageView;
            int currentHeight = (windowWidth * image.getHeight()) / image.getWidth();
            competitiveImageView.setMinimumHeight(currentHeight);

            firebaseInfo.getImagesSReference().child(image.getCompetitiveImageFileName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri)
                            .resize(competitiveImageView.getWidth(), competitiveImageView.getHeight()).into(competitiveImageView);
                }
            });
            final ImageButton profileImageButton = holder.profileImageButton;
            firebaseInfo.getProfileImagesSReference().child(user.getProfileImageFileName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri)
                            .resize(profileImageButton.getWidth(), profileImageButton.getHeight()).into(profileImageButton);
                }
            });

            TextView nicknameTextView = holder.nicknameTextView;
            nicknameTextView.setText(user.getNickname());

            TextView likeTextView = holder.likeTextView;
            likeTextView.setText(String.valueOf(image.getLikeCount()));
        }else if (h instanceof LoadHolder){

            Log.w("pisun",String.valueOf(position));
        }

    }

    @Override
    public int getItemCount() {
        return mImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageButton profileImageButton;
        public ImageButton optionImageButton;
        public ImageButton likeImageButton;
        public ImageView competitiveImageView;
        public TextView nicknameTextView;
        public TextView likeTextView;

        public ViewHolder(View itemView){
            super(itemView);

            profileImageButton = (ImageButton) itemView.findViewById(R.id.profile_image_button);
            optionImageButton = (ImageButton) itemView.findViewById(R.id.option_image_button);
            likeImageButton = (ImageButton) itemView.findViewById(R.id.like_image_button);
            competitiveImageView = (ImageView) itemView.findViewById(R.id.competitive_image_view);
            nicknameTextView = (TextView) itemView.findViewById(R.id.nickname_text_view);
            likeTextView = (TextView) itemView.findViewById(R.id.like_text_view);
        }
    }

    private class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }
}
