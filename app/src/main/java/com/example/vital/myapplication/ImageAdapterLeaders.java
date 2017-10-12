package com.example.vital.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by werton on 18.09.17.
 */

public class ImageAdapterLeaders extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Uri> mImageUriList;
    private Context context;

    public static final int VIEW_TYPE_MOVE = 1;
    private static final int VIEW_TYPE_LOAD = 2;

    @Override
    public int getItemViewType(int position) {
        Log.w("wtf", "onViewType");

        int viewType = VIEW_TYPE_MOVE;
        if(position < mImageUriList.size()){
            viewType = VIEW_TYPE_MOVE;
        }
        if (position == mImageUriList.size() - 1){
            viewType = VIEW_TYPE_LOAD;
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.w("wtf", "onCreate");
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case VIEW_TYPE_MOVE:
                return new ViewHolderLeaders(inflater.inflate(R.layout.leadersimagelayout,parent,false));

            case VIEW_TYPE_LOAD:
                return new LoadHolderLeader(inflater.inflate(R.layout.progress_bar,parent,false));
        }
        return null;
    }

    public ImageAdapterLeaders(Context context, List<Uri> mImageUriList){
        this.mImageUriList = mImageUriList;
        this.context = context;
        Log.w("wtf", "Constructor");

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.w("wtf", "onBind");
        if(holder instanceof ViewHolderLeaders){
            Uri imagesUri = mImageUriList.get(position);
            ViewHolderLeaders viewHolderLeaders = (ViewHolderLeaders) holder;
            viewHolderLeaders.bindView(imagesUri);
        }else if(holder instanceof LoadHolderLeader){

        }
    }

    @Override
    public int getItemCount() {
        return mImageUriList.size();
    }

    public class ViewHolderLeaders extends RecyclerView.ViewHolder{
        SquareImageView imageButton;


        public ViewHolderLeaders(View view){
            super(view);
            imageButton = (SquareImageView) view.findViewById(R.id.imageButton1);
        }

        public void bindView(Uri imageUri){
            Glide.with(context).load(imageUri).fitCenter().into(imageButton);
        }

    }

    private class LoadHolderLeader extends RecyclerView.ViewHolder {
        public LoadHolderLeader(View itemView) {
            super(itemView);
        }
    }

}
