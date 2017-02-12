package com.example.vital.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.first_fragment, container, false);
        Button tempButton = (Button) rootView.findViewById(R.id.button_2);

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainPage.class);
                startActivity(intent);
            }
        });
        return  rootView;
    }
}

