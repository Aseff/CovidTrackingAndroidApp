package com.example.covid19.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.covid19.Adapter.AdapterUsers;
import com.example.covid19.Model.ModelUser;
import com.example.covid19.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUser> modelUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        recyclerView = findViewById(R.id.users_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        modelUserList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        getAllUsers();

    }
    private void getAllUsers() {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelUserList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    if (!fUser.getUid().equals(modelUser.getUid())) {
                        modelUserList.add(modelUser);
                    }

                    adapterUsers = new AdapterUsers(getApplicationContext(), modelUserList);
                    recyclerView.setAdapter(adapterUsers);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void searchUsers(String query) {

        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelUserList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    if (!fUser.getUid().equals(modelUser.getUid())) {
                        if(query.toLowerCase().contains(modelUser.getName().toLowerCase())
                        ){

                            modelUserList.add(modelUser);
                        }

                    }

                    adapterUsers = new AdapterUsers(getApplicationContext(), modelUserList);
                    adapterUsers.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterUsers);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void checkUserStatus() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {

        } else {
            startActivity(new Intent(getApplicationContext(), HospitalHomeActivity.class));
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            checkUserStatus();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
