package com.example.covid19.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.covid19.R;

public class TreatmentWeb extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_web);
        webView=findViewById(R.id.web_treat);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.mayoclinic.org/diseases-conditions/coronavirus/diagnosis-treatment/drc-20479976");
    }
}
