package com.example.vital.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by qwert on 4/9/2017.
 */

public class FragmentScrollView extends Fragment {

    private InteractiveScrollView scrollView;
    private LinearLayout scrollLayout;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentscrollview, container, false);
        scrollLayout = (LinearLayout) view.findViewById(R.id.scrollLayout);
        scrollView = (InteractiveScrollView) view.findViewById(R.id.scrollview);

        addNewPhoto(inflater);
        addNewPhoto(inflater);
        addNewPhoto(inflater);
        addNewPhoto(inflater);
        addNewPhoto(inflater);

        scrollView.setOnBottomReachedListener(new InteractiveScrollView.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                addNewPhoto(inflater);
            }
        });

        return view;
    }

    private  void addNewPhoto(LayoutInflater inflater){
        ViewGroup container = null;
        Bundle instance = null;
        scrollLayout.addView(new FragmentPrototypeScroll().onCreateView(inflater, container, instance));
    }
}
