package com.example.vital.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FragmentLeaders extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ImageAdapterLeaders imageAdapterLeaders;
    private List<List<Uri>> mImageUriList;
    private FirebaseInfo firebaseInfo;
    private int unDownloadedImage = 0;
    private int currentItemCount;

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
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                currentItemCount = imageAdapterLeaders.getItemCount() + 1;
                downLoadNewImage();
                downLoadNewImage();
                downLoadNewImage();


            }
        });
        currentItemCount = imageAdapterLeaders.getItemCount() + 1;
        downLoadNewImage();
        downLoadNewImage();
        downLoadNewImage();



        return view;
    }

    private void downLoadNewImage(){
        unDownloadedImage++;

        class DownloadImage{
            int unCompleteDownloads = 0;
            public void getImage(DatabaseReference imageDbReference, final List<Uri> imageUri){
                unCompleteDownloads++;
                imageDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Image image = dataSnapshot.getValue(Image.class);
                        Log.w("IMAGE IS HAVE", "IMAGE IS HAVE");
                        getImageUri(image, imageUri);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            private void getImageUri(final Image image, final List<Uri> imageUri){
                firebaseInfo.getImagesSReference().child(image.getCompetitiveImageFileName()).getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUri.add(uri);
                                unCompleteDownloads--;
                                if(unCompleteDownloads == 0){
                                    mImageUriList.add(imageUri);
                                    unDownloadedImage--;
                                    if(unDownloadedImage == 0) {
                                        Log.w("wtf", "All right!");
                                        imageAdapterLeaders.notifyItemRangeInserted(currentItemCount, 1);
                                    }
                                }
                            }
                        });
            }

        }


        final DatabaseReference imagesDbReference = firebaseInfo.getImagesDbReference();
        imagesDbReference.orderByChild("likeCount").limitToFirst(4).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Uri> imageUri = new ArrayList<>();
                DownloadImage downloadImage = new DownloadImage();
                for(DataSnapshot dS : dataSnapshot.getChildren()) {
                    DatabaseReference imageDbReference = dS.getRef();
                    downloadImage.getImage(imageDbReference, imageUri);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
