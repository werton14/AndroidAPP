package com.example.vital.myapplication;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vital.myapplication.activities.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwert on 4/9/2017.
 */

public class FragmentScrollView extends Fragment {

    private RecyclerView recyclerView;
    private ImageDownloader imageDownloader;
    private LinearLayoutManager linearLayoutManager;
    private ImageAdapter imageAdapter;
    private List<Image> mImages;
    private List<User> mUsers;
    private List<String> mImageIds;
    private List<Uri> mImageUris;
    private List<Uri> mProfileUris;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentscrollview, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setItemViewCacheSize(1000);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mImages = new ArrayList<Image>();
        mUsers = new ArrayList<User>();
        mImageIds = new ArrayList<String>();
        mImageUris = new ArrayList<Uri>();
        mProfileUris = new ArrayList<Uri>();
        imageAdapter = new ImageAdapter(view.getContext(), mImages, mUsers, mImageIds, mImageUris, mProfileUris);
        recyclerView.setAdapter(imageAdapter);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        mSwipeRefreshLayout = view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.RED, Color.YELLOW, Color.BLUE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mImages.clear();
                mUsers.clear();
                mImageIds.clear();
                mImageUris.clear();
                mProfileUris.clear();
                newImageDownloader();
                imageDownloader.findImage();
            }
        });


        newImageDownloader();
        imageDownloader.findImage();

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                imageDownloader.findImage();
            }
        });



        return view;
    }

    private void newImageDownloader(){
        imageDownloader = new ImageDownloader(getActivity().getApplicationContext());
        imageDownloader.setOnDataDownloadedListener(new ImageDownloader.OnDataDownloadedListener() {
            @Override
            public void onDataDownloaded(ImageData data) {
                int currentItemCount = imageAdapter.getItemCount();
                mImages.addAll(data.getImages());
                mUsers.addAll(data.getUsers());
                mImageIds.addAll(data.getImageIds());
                mImageUris.addAll(data.getImageUris());
                mProfileUris.addAll(data.getProfileUris());
                imageAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                //imageAdapter.notifyItemRangeInserted(currentItemCount, data.getImages().size()-1);
            }
        });
    }

    public static FragmentScrollView newInstance(){
        return new FragmentScrollView();
    }

    public void scrollToTop(){
        recyclerView.smoothScrollToPosition(0);
    }
}
