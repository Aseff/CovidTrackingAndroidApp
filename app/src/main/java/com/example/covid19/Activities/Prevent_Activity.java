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

public class Prevent_Activity extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevent_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Preventing a virus");


        collapsingToolbarLayout=findViewById(R.id.toolbar_layout);
        image=findViewById(R.id.prevent_image);
        image.setImageResource(R.drawable.prevent);


    }
}
