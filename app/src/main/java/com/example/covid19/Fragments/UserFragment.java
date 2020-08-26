package com.example.covid19.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19.Adapter.AdapterUsers;
import com.example.covid19.Activities.HospitalHomeActivity;
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

public class UserFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUser> modelUserList;

    public UserFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = view.findViewById(R.id.users_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        modelUserList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        getAllUsers();
        return view;
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

                    adapterUsers = new AdapterUsers(getActivity(), modelUserList);
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

                    adapterUsers = new AdapterUsers(getActivity(), modelUserList);
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
            startActivity(new Intent(getActivity(), HospitalHomeActivity.class));
            getActivity().finish();
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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu,menuInflater);

        //SearchView
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /// when user press search button
                if (!query.equals("")) {
                    searchUsers(query);
                } else {
                    getAllUsers();
                }
                return false;
            }
//!TextUtils.isEmpty(query.trim())

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!newText.equals("")) {
                    searchUsers(newText);
                } else {
                    getAllUsers();
                }
                return false;
            }
        });


    }


}
