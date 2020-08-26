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

public class ScrollingSymptom extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_symptom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Virus Symptoms");


        collapsingToolbarLayout=findViewById(R.id.toolbar_layout);
        image=findViewById(R.id.symptom_image);
        image.setImageResource(R.drawable.corona);





    }
}
