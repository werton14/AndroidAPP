package com.example.vital.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.vital.myapplication.activities.FullScreenPictureActivity;
import com.example.vital.myapplication.activities.Image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by werton on 03.06.17.
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Image> mImage;
    private List<User> mUser;
    private List<String> mImageId;
    private List<Uri> mImageUris;
    private List<Uri> mProfileUris;
    private Context context;
    private int windowWidth;
    public static final int VIEW_TYPE_MOVE = 1;
    private static final int VIEW_TYPE_LOAD = 2;

    @Override
    public int getItemViewType(int position){
        int viewType = VIEW_TYPE_MOVE;
        if(position < mUser.size()){
            viewType = VIEW_TYPE_MOVE;
        }
        if (position == mUser.size() - 1){
            viewType = VIEW_TYPE_LOAD;
        }
        return viewType;
    }

    public ImageAdapter(Context context, List<Image> images, List<User> users, List<String> imageId,
                        List<Uri> imageUris, List<Uri> profileUris){
        this.mImage = images;
        this.mUser = users;
        this.mImageId = imageId;
        this.mImageUris = imageUris;
        this.mProfileUris = profileUris;
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
        if (h instanceof ViewHolder){
            Image image = mImage.get(position);
            User user = mUser.get(position);
            String imageId = mImageId.get(position);
            //Uri imageUri = mImageUris.get(position);
            Uri profileUri = mProfileUris.get(position);

            ViewHolder holder = (ViewHolder) h;
            holder.bindView(image, user, imageId, null, profileUri);

        }else if (h instanceof LoadHolder){

        }

    }

    @Override
    public int getItemCount() {
        return mImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView profileImageButton;
        private ImageButton optionImageButton;
        private ImageButton likeImageButton;
        private ImageView competitiveImageView;
        private TextView nicknameTextView;
        private TextView likeTextView;
        private TextView viewsCountTextView;

        private Image mImage;
        private User mUser;
        private String mImageId;
        private Uri mImageUri;
        private Uri mProfileUri;
        private FirebaseInfo firebaseInfo = FirebaseInfo.getInstance();
        private boolean isLikedByCurrentUser;

        public ViewHolder(View itemView){
            super(itemView);

            profileImageButton = (CircleImageView) itemView.findViewById(R.id.profile_image_button);
            optionImageButton = (ImageButton) itemView.findViewById(R.id.option_image_button);
            likeImageButton = (ImageButton) itemView.findViewById(R.id.like_image_button);
            likeImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeLike();
                }
            });
            competitiveImageView = (ImageButton) itemView.findViewById(R.id.competitive_image_view);
            nicknameTextView = (TextView) itemView.findViewById(R.id.nickname_text_view);
            likeTextView = (TextView) itemView.findViewById(R.id.like_text_view);
            viewsCountTextView = (TextView) itemView.findViewById(R.id.views_count_text_view);

        }

        public void bindView(Image image, User user, final String imageId, Uri imageUri, Uri profileUri){
            mImage = image;
            mUser = user;
            mImageId = imageId;
            //mImageUri = imageUri;
            mProfileUri = profileUri;

            int currentHeight = (windowWidth * mImage.getHeight()) / mImage.getWidth();
            competitiveImageView.setMinimumHeight(currentHeight);

            Glide.with(context).load(mImage.getImageUri()).diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().dontAnimate().into(competitiveImageView);

            Glide.with(context).load(mProfileUri).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(profileImageButton);

            nicknameTextView.setText(mUser.getNickname());

            firebaseInfo.getWhoLikedImageDbReference().child(imageId).child(firebaseInfo.getCurrentUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        boolean like = dataSnapshot.getValue(boolean.class);
                        isLikedByCurrentUser = like;
                        if(like) likeImageButton.setImageResource(R.drawable.ic_favorite_red);
                    }else{
                        isLikedByCurrentUser = false;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            likeTextView.setText(String.valueOf(mImage.getLikeCount()));

            DatabaseReference viewsDbReference = firebaseInfo.getViewsDbReference();
            viewsDbReference.child(mImageId).child("view").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    viewsCountTextView.setText(String.valueOf(dataSnapshot.getValue(long.class)));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            competitiveImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullScreenPictureActivity.class);
                    int [] screenLocation = new int[2];
                    v.getLocationOnScreen(screenLocation);
                    intent.putExtra("left", screenLocation[0]);
                    intent.putExtra("top", screenLocation[1]);
                    int orientation = context.getResources().getConfiguration().orientation;
                    intent.putExtra("orientation", orientation);
                    intent.putExtra("width", competitiveImageView.getWidth());
                    intent.putExtra("height", competitiveImageView.getHeight());
                    intent.putExtra("imageUri", mImageUri.toString());
                    intent.putExtra("imageId", imageId);
                    context.startActivity(intent);
                }
            });
        }

        private void makeLike(){
            final FirebaseInfo info = FirebaseInfo.getInstance();
            final DatabaseReference imageDbReference = info.getWhoLikedImageDbReference().child(mImageId);
            if(isLikedByCurrentUser){
                updateLikeParam(imageDbReference, false);
                runLikeTransaction(-1l);

                likeImageButton.setImageResource(R.drawable.ic_favorite_grey);

                mImage.setLikeCount(mImage.getLikeCount() - 1l);
                likeTextView.setText(String.valueOf(mImage.getLikeCount()));
            }else {
                updateLikeParam(imageDbReference, true);
                runLikeTransaction(1l);

                likeImageButton.setImageResource(R.drawable.ic_favorite);

                mImage.setLikeCount(mImage.getLikeCount() + 1l);
                likeTextView.setText(String.valueOf(mImage.getLikeCount()));
            }
        }

        private void updateLikeParam(DatabaseReference imageDbReference, boolean value){
            isLikedByCurrentUser = value;
            FirebaseInfo info = FirebaseInfo.getInstance();
            imageDbReference.child(info.getCurrentUserId()).setValue(value);
        }

        private void runLikeTransaction(final long action){
            firebaseInfo.getImagesDbReference().child(mImageId).runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Image image = mutableData.getValue(Image.class);
                    if(image == null) {
                        Log.w("Like", "1");
                    }else{
                        image.setLikeCount(image.getLikeCount() + action);
                        mutableData.setValue(image);
                        Log.w("Like", "2");
                    }
                    return Transaction.success(mutableData);

                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    if(databaseError != null);
                }
            });
        }
    }

    private class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }
}
