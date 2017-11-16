package com.example.vital.myapplication.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.vital.myapplication.FirebaseInfo;
import com.example.vital.myapplication.OnSwipeTouchListener;
import com.example.vital.myapplication.R;

/**
 * Created by Artem on 12.11.2017.
 */

public class ForeignPersonalData extends AppCompatActivity {

    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_date);

        relativeLayout = (RelativeLayout) findViewById(R.id.activity_personal_date);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutDataa);
        editText = (EditText) findViewById(R.id.description);

        editText.setEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            linearLayout.setVisibility(View.VISIBLE);
        } else {

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(params);
            linearLayout.setVisibility(View.VISIBLE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
}
