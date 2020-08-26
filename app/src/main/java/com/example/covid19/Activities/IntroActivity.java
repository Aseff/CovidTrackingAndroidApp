package com.example.covid19.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.covid19.IntroViewPageAdapter;
import com.example.covid19.R;
import com.example.covid19.ScreenItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class
IntroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    IntroViewPageAdapter introViewPageAdapter;
    TabLayout tabLayout;
    Button button_next;
    int position = 0;
    Button btngetStarted;
    Animation animation;
    TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // when this activity is about to be launch we need to check if its openened before or not

        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(), Register.class );
            startActivity(mainActivity);
            finish();


        }

        setContentView(R.layout.activity_intro);
        tabLayout = findViewById(R.id.tab_indicator);
        button_next = findViewById(R.id.btn_next);
        btngetStarted = findViewById(R.id.btn_get_started);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        tvSkip=findViewById(R.id.tv_skip);

        final List<ScreenItem> myList = new ArrayList<>();
        myList.add(new ScreenItem("Personal Information", "The application includes an e-health record to save and monitor the health data for the user", R.drawable.second));
        myList.add(new ScreenItem("Information and News", "The app  provides information and news in various categories, such as symptom guidelines, virus categories, and public video learning contents, preventing white paper, local news, and general information about health and self-preventing activities.", R.drawable.fifth));
        myList.add(new ScreenItem("Self-declaration and upload center", "registered users can send short audio, video, or comment to the health centers. This section is useful for monitoring people remotely", R.drawable.third));
        myList.add(new ScreenItem("Referral patient to the hospital", "Information is made by hospital staff, including the quarantined or healthy user. Health records for any patient can save to the system for the next referral if needed", R.drawable.fouth));
        myList.add(new ScreenItem("The Self-Quarantine", "To monitoring self-quarantine, the application saves some data and provides the guidelines and news for those users. It can join to other programs to provide services like food services, and other requirements for the user", R.drawable.fifth));
        myList.add(new ScreenItem("Coronavirus measure section (based on the current location, people)", "If the correct information is entered, general and detailed information about patients is collected through this system, and other health systems and the level of confidentiality is determined by managers to access different levels of access including, state, provincial, city, health care providers (physicians, nurses,...) ", R.drawable.sixth));


        viewPager = findViewById(R.id.screen_viewpager);
        introViewPageAdapter = new IntroViewPageAdapter(this, myList);
        viewPager.setAdapter(introViewPageAdapter);

        tabLayout.setupWithViewPager(viewPager);

        //next button action
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = viewPager.getCurrentItem();
                if (position < myList.size()) {
                    position++;
                    viewPager.setCurrentItem(position);
                }

                if (position == myList.size() - 1) {
                    loadLastScreen();
                }
            }
        });

        //TABLAYOUT add cahnge listener // eger animation ile edende getstarted buttonu gelsin

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition()==myList.size()-1) {
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        btngetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Register.class);
                startActivity(intent);

                savePreferIntro();
                finish();
            }
        });


        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(myList.size());
            }
        });

    }

    private boolean restorePrefData() {

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore=sharedPreferences.getBoolean("isIntroOpnend",false);
        return isIntroActivityOpenedBefore;
    }

    public void loadLastScreen() {
        button_next.setVisibility(View.INVISIBLE);
        btngetStarted.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        btngetStarted.setAnimation(animation);
    }

    public void savePreferIntro(){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();

    }
}
