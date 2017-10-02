package com.example.vital.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vital.myapplication.activities.ContainerUserFragmentsActivity;


public class FragmentCamera extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragmentcamera, container, false);
        Button tempButton = (Button) rootView.findViewById(R.id.button2);
        

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ContainerUserFragmentsActivity.class);
                startActivity(intent);
            }
        });
        return  rootView;
    }


}

