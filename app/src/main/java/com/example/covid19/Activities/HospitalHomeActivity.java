package com.example.covid19.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.covid19.Fragments.ChatListFragment;
import com.example.covid19.Fragments.HomeFragment;
import com.example.covid19.Fragments.HospitalFragment;
import com.example.covid19.Fragments.SettingsFragment;
import com.example.covid19.Fragments.UserFragment;
import com.example.covid19.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

public class HospitalHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private AppBarConfiguration mAppBarConfiguration;
    private BottomNavigationView myBottomView;
    private androidx.appcompat.app.ActionBar actionBar;


    private String mUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_home);

        myBottomView = findViewById(R.id.nav_view_bar);

         actionBar=getSupportActionBar();
        
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // TODO: Remove the redundant calls to getSupportActionBar()
//        //       and use variable actionBar instead
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBar.setTitle("Hospital");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();




        DrawerLayout drawer = findViewById(R.id.drawer_layout_home);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_hospital);
        navigationView.setNavigationItemSelectedListener(this);
        updateNavHeader();

        myBottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                if (id == R.id.navigation_home_hospital) {

                  //  getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new HomeFragment()).commit();

                } else if (id == R.id.navigation_users) {
                    actionBar.setTitle("Users");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_hospital_fragment, new UserFragment()).commit();
//                    Intent intent=new Intent(getApplicationContext(),NewsActivity.class);
//                    startActivity(intent);

                } else if (id == R.id.navigation_notif) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new NotifFragment()).commit();
//
//                    Intent intent=new Intent(getApplicationContext(),InformationActivity.class);
//                    startActivity(intent);
                }
                else if (id == R.id.navigation_chat) {
                    actionBar.setTitle("Chats");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_hospital_fragment, new ChatListFragment()).commit();
//                    Intent intent=new Intent(getApplicationContext(),NewsActivity.class);
//                    startActivity(intent);



                }

                return false;
            }
        });


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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //calling the method displayselectedscreen and passing the id of selected menu
        int id = item.getItemId();
        if (id == R.id.nav_icon_home) {

            getSupportFragmentManager().beginTransaction().replace(R.id.nav_hospital_fragment, new HomeFragment()).commit();


        } else if (id == R.id.nav_profile) {

            Intent intent=new Intent(getApplicationContext(),UserProfile.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {

            getSupportFragmentManager().beginTransaction().replace(R.id.nav_hospital_fragment, new SettingsFragment()).commit();

        } else if (id == R.id.nav_signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout_home);
        drawer.closeDrawer(GravityCompat.START);
        //make this method blank
        return true;
    }


    @Override
    public void onClick(View v) {

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_home);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart(){
        checkUserStatus();
        super.onStart();
    }
    public void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view_hospital);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);
        // ImageView navUserPhoto=headerView.findViewById(R.id.nav_photo);

        navUsername.setText(firebaseUser.getDisplayName());
        navUserMail.setText(firebaseUser.getEmail());


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_hospital_fragment);
        mAppBarConfiguration =new AppBarConfiguration.Builder(navController.getGraph()).build();
        //configure nav controller

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }






}
