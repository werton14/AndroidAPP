package com.example.vital.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.storage.FirebaseStorage;

import java.util.zip.Inflater;

/**
 * Created by qwert on 4/9/2017.
 */

public class FragmentScrollView extends Fragment {

    private InteractiveScrollView scrollView;
    private LinearLayout scrollLayout;
    private FragmentPrototypeScroll previous;
    private boolean hasNext;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentscrollview, container, false);
        scrollLayout = (LinearLayout) view.findViewById(R.id.scrollLayout);
        scrollView = (InteractiveScrollView) view.findViewById(R.id.scrollview);


        addNewAlonePhoto(inflater);
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

    private void addNewPhoto(final LayoutInflater inflater){
        if(hasNext) {
            final FragmentPrototypeScroll fps = new FragmentPrototypeScroll();
            fps.addViewIsReadyListener(new FragmentPrototypeScroll.ViewIsReadyListener() {
                @Override
                public void OnViewIsReady() {
                    hasNext = false;
                }
            });
            scrollLayout.addView(fps.onCreateView(inflater, null, null));
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
        scrollLayout.addView(previous.onCreateView(inflater, null, null));
        previous.findImage();
        hasNext = true;
    }
}
