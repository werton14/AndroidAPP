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

import com.google.firebase.storage.FirebaseStorage;

import java.util.zip.Inflater;

/**
 * Created by qwert on 4/9/2017.
 */

public class FragmentScrollView extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> holderAdapter;
//    private LinearLayout scrollLayout;
    private FragmentPrototypeScroll previous;
    LinearLayoutManager linearLayoutManager;
    private boolean hasNext;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentscrollview, container, false);
//        scrollLayout = (LinearLayout) view.findViewById(R.id.scrollLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                addNewPhoto(inflater);
            }
        });


        addNewAlonePhoto(inflater);
        addNewPhoto(inflater);
        addNewPhoto(inflater);
        addNewPhoto(inflater);


        return view;
    }


    private void addNewPhoto(final LayoutInflater inflater){
        if(hasNext) {
            final FragmentPrototypeScroll fps = new FragmentPrototypeScroll();
            fps.addViewIsReadyListener(new FragmentPrototypeScroll.ViewIsReadyListener() {
                @Override
                public void OnViewIsReady() {
                    hasNext = false;
                }
            });
            linearLayoutManager.addView(fps.onCreateView(inflater, null, null));
            previous.addViewIsReadyListener(new FragmentPrototypeScroll.ViewIsReadyListener() {
                @Override
                public void OnViewIsReady() {
                    FragmentPrototypeScroll fg = fps;
                    fg.findImage();
                }
            });
            previous = fps;
        }else{
            addNewAlonePhoto(inflater);
        }
    }

    private void addNewAlonePhoto(LayoutInflater inflater){
        previous = new FragmentPrototypeScroll();
        recyclerView.addView(previous.onCreateView(inflater, null, null));
        previous.findImage();
        hasNext = true;
    }
}
