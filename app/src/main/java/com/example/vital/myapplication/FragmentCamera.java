package com.example.vital.myapplication;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class FragmentCamera extends Fragment{

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragmentcamera, container, false);
        Button tempButton = (Button) rootView.findViewById(R.id.button2);


        return  rootView;
    }

}

