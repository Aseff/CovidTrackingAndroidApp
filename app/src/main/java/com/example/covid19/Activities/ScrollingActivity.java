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

public class ScrollingActivity extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.scroll_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Virus Categories");


        collapsingToolbarLayout=findViewById(R.id.toolbar_layout);
        image=findViewById(R.id.scroll_image);
        image.setImageResource(R.drawable.covidd);





//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
}
