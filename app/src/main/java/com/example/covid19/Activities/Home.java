package com.example.covid19.Activities;

import  android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.covid19.Fragments.HomeFragment;
import com.example.covid19.Fragments.HospitalFragment;
import com.example.covid19.Fragments.MapFragment;
import com.example.covid19.Fragments.ProfileFragment;
import com.example.covid19.Fragments.SettingsFragment;
import com.example.covid19.Fragments.UserFragment;
import com.example.covid19.R;
import com.example.covid19.notification.Token;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class  Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private BottomNavigationView myBottomView;
    private String mUID;
    private CardView hospital_card,news_card,categories_virus,symptom,transmitted,prevent,mask,treatment;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myBottomView = findViewById(R.id.nav_bar_view);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        hospital_card=findViewById(R.id.card_hospital);
        news_card=findViewById(R.id.card_news);
        categories_virus=findViewById(R.id.virus_categories);
        symptom=findViewById(R.id.symptom);
        transmitted=findViewById(R.id.transmitted);
        prevent=findViewById(R.id.prevent);
        mask=findViewById(R.id.mask);
        treatment=findViewById(R.id.treatment);



        hospital_card.setOnClickListener(this);
        news_card.setOnClickListener(this);
        categories_virus.setOnClickListener(this);
        symptom.setOnClickListener(this);
        transmitted.setOnClickListener(this);
        prevent.setOnClickListener(this);
        mask.setOnClickListener(this);
        treatment.setOnClickListener(this);




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//        R.id.navigation_home,R.id.navigation_news, R.id.navigation_maps,R.id.navigation_notifications, R.id.navigation_hospital)
//        .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(myBottomView, navController);
//


        updateNavHeader();

        myBottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                if (id == R.id.navigation_home) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new HomeFragment()).commit();

                } else if (id == R.id.navigation_news) {
                    Intent intent=new Intent(getApplicationContext(),NewsActivity.class);
                    startActivity(intent);


                } else if (id == R.id.navigation_maps) {
                    Intent intent=new Intent(getApplicationContext(),NearbyClinicLocation.class);
                    startActivity(intent);
//                   getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new MapFragment()).commit();
                } else if (id == R.id.navigation_notifications) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new NotifFragment()).commit();

                    Intent intent=new Intent(getApplicationContext(),InformationActivity.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_chat) {
                   // getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new UserFragment()).commit();

                    Intent intent=new Intent(getApplicationContext(),MessagingActivity.class);
                    startActivity(intent);
                }

                return false;
            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    protected void onResume(){
        checkUserStatus();
        super.onResume();
    }

    private void checkUserStatus(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            mUID= firebaseUser.getUid();
            //SAVE UID OF CURRENTLY SIGNED USER IN SHARED PREFERENCES
            SharedPreferences preferences=getSharedPreferences("SP_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("Current_USERID",mUID);
            editor.apply();

        }
        else{
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }
    }

    @Override
    protected void onStart(){
        checkUserStatus();
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public  void updateToken(String token){
        DatabaseReference reff= FirebaseDatabase.getInstance().getReference("Tokens");
        Token mtoken=new Token(token);
    //    reff.child(mUID).setValue(mtoken);

    }

    public void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);
        // ImageView navUserPhoto=headerView.findViewById(R.id.nav_photo);

        navUsername.setText(firebaseUser.getDisplayName());
        navUserMail.setText(firebaseUser.getEmail());


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //calling the method displayselectedscreen and passing the id of selected menu
        int id = item.getItemId();
        if (id == R.id.nav_icon_home) {

            getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new HomeFragment()).commit();


        } else if (id == R.id.nav_profile) {

            Intent intent=new Intent(getApplicationContext(),UserProfile.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {

            getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new SettingsFragment()).commit();

        } else if (id == R.id.nav_signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        //make this method blank
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.card_hospital:i=new Intent(this,OfficialDetail.class);
                startActivity(i);
                break;
            case R.id.card_news:i=new Intent(this,Local_News.class);
                startActivity(i);
                break;
            case R.id.virus_categories:i=new Intent(this,ScrollingActivity.class);
                startActivity(i);
                break;
            case R.id.symptom:i=new Intent(this,ScrollingSymptom.class);
                startActivity(i);
                break;
            case R.id.transmitted:i=new Intent(this,Transmitted_Activity.class);
                startActivity(i);
                break;
            case R.id.prevent:i=new Intent(this,Prevent_Activity.class);
                startActivity(i);
                break;
            case R.id.mask:i=new Intent(this,MaskWeb.class);
                startActivity(i);
                break;
            case R.id.treatment:i=new Intent(this,TreatmentWeb.class);
                startActivity(i);
                break;



            default:break;
        }

    }
    }

