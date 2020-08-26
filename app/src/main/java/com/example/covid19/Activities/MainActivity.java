package com.example.covid19.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid19.R;

public class MainActivity extends AppCompatActivity {

    TextView txthealth,txtjoin;
    Button btngetstarted;
    Animation atg,btgone, btgtwo;
    ImageView firstpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load animationa
        atg= AnimationUtils.loadAnimation(this,R.anim.atg);
        btgone= AnimationUtils.loadAnimation(this,R.anim.btgone);
        btgtwo= AnimationUtils.loadAnimation(this,R.anim.btgtwo);
        //pass animation
        firstpic.startAnimation(atg);
        txthealth.startAnimation(btgone);
        txtjoin.startAnimation(btgone);
        btngetstarted.startAnimation(btgtwo);


        Typeface MLight= Typeface.createFromAsset(getAssets(),"fonts/MLight.ttf");
        Typeface MMedium= Typeface.createFromAsset(getAssets(),"fonts/MMedium.ttf");
        Typeface MRegular= Typeface.createFromAsset(getAssets(),"fonts/MRegular.ttf");

        txthealth.setTypeface(MRegular);
        txtjoin.setTypeface(MLight);
        btngetstarted.setTypeface(MMedium);


    }
}
