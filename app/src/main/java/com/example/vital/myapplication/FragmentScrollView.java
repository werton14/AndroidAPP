package com.example.vital.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vital.myapplication.activities.Image;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by qwert on 4/9/2017.
 */

public class FragmentScrollView extends Fragment {

    private RecyclerView recyclerView;
    private ImageDownloader imageDownloader;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentscrollview, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setItemViewCacheSize(1000);
        recyclerView.setDrawingCacheEnabled(true);
        final List<Image> images = new ArrayList<Image>();
        final List<User> users = new ArrayList<User>();
        final ImageAdapter imageAdapter = new ImageAdapter(view.getContext(), images, users);
        recyclerView.setAdapter(imageAdapter);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        imageDownloader = new ImageDownloader();
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                ArrayDeque<List<Object>> dataQueue = imageDownloader.getDataQueue();
                if(!dataQueue.isEmpty()){
                    List<Object> data = dataQueue.poll();
                    images.add((Image) data.get(0));
                    users.add((User) data.get(1));
                    int prevSize = imageAdapter.getItemCount();
                    imageAdapter.notifyItemRangeInserted(prevSize, images.size() - 1);
                }else {
                    imageDownloader.setOnDataDownloadedListener(new ImageDownloader.OnDataDownloadedListener() {
                        @Override
                        public void onDataDownloaded() {
                            onLoadMore(page, totalItemsCount);
                            imageDownloader.setOnDataDownloadedListener(null);
                        }
                    });
                }

            }
        });

        return view;
    }


}
