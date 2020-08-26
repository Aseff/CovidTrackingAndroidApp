package com.example.covid19.Activities;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;

import com.example.covid19.R;

public class Transmitted_Activity extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmitted_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("How is it Transmitted?");


        collapsingToolbarLayout=findViewById(R.id.toolbar_layout);
        image=findViewById(R.id.distance_image);
        image.setImageResource(R.drawable.disranse);

    }
}
