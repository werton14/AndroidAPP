package com.example.vital.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.vital.myapplication.activities.Image;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FragmentLeaders extends Fragment {

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private ImageAdapterLeaders imageAdapterLeaders;
    private List<Uri> mImageUriList;
    private FirebaseInfo firebaseInfo;
    private int unDownloadedImage = 0;
    private int currentItemCount;
    private int lastItem = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_leaders, container, false);
        firebaseInfo = FirebaseInfo.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewLeaders);
        recyclerView.setItemViewCacheSize(1000);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mImageUriList = new ArrayList<>();
        imageAdapterLeaders = new ImageAdapterLeaders(view.getContext(), mImageUriList);
        recyclerView.setAdapter(imageAdapterLeaders);
        layoutManager = new GridLayoutManager(view.getContext(), 4);
        layoutManager.generateDefaultLayoutParams();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
//        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                currentItemCount = imageAdapterLeaders.getItemCount() + 1;
//                downLoadNewImage();
//            }
//        });
        currentItemCount = imageAdapterLeaders.getItemCount() + 1;
        downLoadNewImage();



        return view;
    }

    private void downLoadNewImage(){

        Log.w("downloadImage", "All ok!");
        unDownloadedImage++;

        class DownloadImage{
            int unCompleteDownloads = 0;
            public void getImage(DatabaseReference imageDbReference){
                unCompleteDownloads++;
                imageDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Image image = dataSnapshot.getValue(Image.class);
                        Log.w("IMAGE IS HAVE", "IMAGE IS HAVE");
                        getImageUri(image);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            private void getImageUri(final Image image){
                firebaseInfo.getImagesSReference().child(image.getCompetitiveImageFileName()).getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mImageUriList.add(uri);
                                unCompleteDownloads--;
                                if(unCompleteDownloads == 0){
                                    unDownloadedImage--;
                                    if(unDownloadedImage == 0) {
                                        Log.w("wtf", "All right!");
                                        imageAdapterLeaders.notifyDataSetChanged();
                                        //notifyAdapter();
                                    }
                                }
                            }
                        });
            }
        }

        final DatabaseReference imagesDbReference = firebaseInfo.getImagesDbReference();
        Query image= imagesDbReference.limitToLast(600);
        lastItem++;

        image.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DownloadImage downloadImage = new DownloadImage();
                DatabaseReference imageDbReference = null;
                for(DataSnapshot dS : dataSnapshot.getChildren()) {
                    imageDbReference = dS.getRef();

                    downloadImage.getImage(imageDbReference);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @UiThread
    private void notifyAdapter(){
        imageAdapterLeaders.notifyItemRangeInserted(currentItemCount, 1);
    }
}
