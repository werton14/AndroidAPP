package com.example.vital.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.vital.myapplication.activities.Image;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Artem on 16.06.2017.
 */

public class LeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<List<Image>> mImage;
    private Context context;
    private static final int VIEW_TYPE_MOVE = 1;
    private static final int VIEW_TYPE_LOAD = 2;

    public LeaderAdapter(Context context, List<List<Image>> mImage){
        this.context = context;
        this.mImage = mImage;
    }

    @Override
    public int getItemViewType(int position){
        int viewType = VIEW_TYPE_MOVE;
        if(position < mImage.size()){
            viewType = VIEW_TYPE_MOVE;
        }
        if (position == mImage.size() - 1){
            viewType = VIEW_TYPE_LOAD;
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType){
            case VIEW_TYPE_MOVE:
                return new ViewHolder(inflater.inflate(R.layout.image,parent,false));

            case VIEW_TYPE_LOAD:
                return new LoadHolder(inflater.inflate(R.layout.progress_bar,parent,false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        ViewHolder holder = (ViewHolder) h;
        List<Image> image = mImage.get(position);
        FirebaseInfo firebaseInfo = FirebaseInfo.getInstance();

        final Iterator<ImageButton> itertor = holder.imageButtons.iterator();

        for (Image img:image) {

            firebaseInfo.getImagesSReference().child(img.getCompetitiveImageFileName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri).centerCrop().into(itertor.next());
                }
            });
        }
    }

    @Override
    public int getItemCount() { return mImage.size();}

    private class ViewHolder extends RecyclerView.ViewHolder {

        public List<ImageButton> imageButtons;

        public ViewHolder(View itemView) {
            super(itemView);
            imageButtons = new ArrayList<ImageButton> ();
            imageButtons.add((ImageButton) itemView.findViewById(R.id.imageButton1));
        }
    }

    private class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }
}
