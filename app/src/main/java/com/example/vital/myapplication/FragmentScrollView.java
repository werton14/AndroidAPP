package com.example.vital.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vital.myapplication.activities.Image;
import com.google.firebase.storage.FirebaseStorage;

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
        final List<Image> images = new ArrayList<Image>();
        final List<User> users = new ArrayList<User>();
        final ImageAdapter imageAdapter = new ImageAdapter(images, users);
        recyclerView.setAdapter(imageAdapter);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        imageDownloader = new ImageDownloader();
        imageDownloader.findImage();
        imageDownloader.findImage();
        imageDownloader.findImage();
        imageDownloader.findImage();
        imageDownloader.findImage();
        imageDownloader.setOnDataDownloadedListener(new ImageDownloader.OnDataDownloadedListener() {
            @Override
            public void onDataDownloaded(Image image, User user) {
                images.add(image);
                users.add(user);
                int prevSize = imageAdapter.getItemCount();
                imageAdapter.notifyItemRangeInserted(prevSize, images.size() - 1);
            }
        });
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                imageDownloader.findImage();
            }
        });




        return view;
    }


}
