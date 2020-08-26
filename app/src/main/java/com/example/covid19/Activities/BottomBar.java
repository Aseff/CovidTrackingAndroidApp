package com.example.covid19.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.covid19.R;

public class BottomBar extends AppCompatActivity  implements View.OnClickListener {
    GridLayout mainGrid;
    private CardView hospital_card, news_card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
//        hospital_card=findViewById(R.id.card_hospital);
//        news_card=findViewById(R.id.card_news);
//
//        hospital_card.setOnClickListener(this);
//        news_card.setOnClickListener(this);
        //       hospital_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(BottomBar.this,"you clicked health",Toast.LENGTH_SHORT ).show();
//                Intent intent=new Intent(getApplicationContext(),OfficialDetail.class);
//                startActivity(intent);
//            }
//        });


    }


    @Override
    public void onClick(View v) {
//        Intent i;
//
//        switch (v.getId()){
//            case R.id.card_hospital:i=new Intent(this,OfficialDetail.class);
//            startActivity(i);
//            break;
//            case R.id.card_news:i=new Intent(this,Local_News.class);
//                startActivity(i);
//
//                break;
//            default:break;
//        }
//
//    }
//}
    }
}