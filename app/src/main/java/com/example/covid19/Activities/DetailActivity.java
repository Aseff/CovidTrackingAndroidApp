package com.example.covid19.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid19.R;

public class DetailActivity extends AppCompatActivity {

    private int positionCountry;
    TextView tvCountry,tvCases,tvRecovered,tvCritical,tvActive,tvTodayCases,tvTotalDeaths,tvTodayDeaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent=getIntent();
        positionCountry=intent.getIntExtra("position",0);
//
//        getSupportActionBar().setTitle("Details of "+TrackCountry.country_list.get(positionCountry).getCountry());
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tvCountry = findViewById(R.id.tvCountry);
        tvCases = findViewById(R.id.tvCases);
        tvRecovered = findViewById(R.id.tvRecovered);
        tvCritical = findViewById(R.id.tvCritical);
        tvActive = findViewById(R.id.tvActive);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTotalDeaths = findViewById(R.id.tvDeaths);
        tvTodayDeaths = findViewById(R.id.tvTodayDeaths);

        tvCountry.setText(TrackCountry.country_list.get(positionCountry).getCountry());
        tvCases.setText(TrackCountry.country_list.get(positionCountry).getCases());
        tvRecovered.setText(TrackCountry.country_list.get(positionCountry).getRecovered());
        tvCritical.setText(TrackCountry.country_list.get(positionCountry).getCritical());
        tvActive.setText(TrackCountry.country_list.get(positionCountry).getActive());
        tvTodayCases.setText(TrackCountry.country_list.get(positionCountry).getTodayCases());
        tvTotalDeaths.setText(TrackCountry.country_list.get(positionCountry).getDeaths());
        tvTodayDeaths.setText(TrackCountry.country_list.get(positionCountry).getTodayDeaths());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
