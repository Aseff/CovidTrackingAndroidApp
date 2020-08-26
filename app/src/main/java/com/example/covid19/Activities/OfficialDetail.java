package com.example.covid19.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.covid19.R;

public class OfficialDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_detail);
//
//        TextView txtInfo = findViewById(R.id.txtInfo);
//        if(getIntent() != null)
//        {
//            String info = getIntent().getStringExtra("info");
//            txtInfo.setText(info);
//        }
    }
}
