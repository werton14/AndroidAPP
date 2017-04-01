package com.example.vital.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ActivityPersonalDate extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_date);

        initToolbar();
    }

    private void initToolbar() {
        toolbar  = (Toolbar) findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.menu_choose_screen);

    }
}
