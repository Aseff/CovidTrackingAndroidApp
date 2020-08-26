package com.example.covid19.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.covid19.Adapter.AdapterChat;
import com.example.covid19.Model.ModelChat;
import com.example.covid19.Model.ModelUser;
import com.example.covid19.R;
import com.example.covid19.notification.APIService;
import com.example.covid19.notification.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileTv;
    TextView nameTv, userStatusTv;
    EditText messageEt;
    ImageButton imageButton;

    String hisUid;
    String myUid;
    String hisImage;


    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ModelChat> chatList;
    AdapterChat adapterChat;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Query query;

    private APIService apiService;
    boolean notify=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Toolbar toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        recyclerView = findViewById(R.id.chat_recyclerView);
        profileTv = findViewById(R.id.profileTv);
        nameTv = findViewById(R.id.nameTv);
        userStatusTv = findViewById(R.id.userStatusTV);
        messageEt = findViewById(R.id.messageTv);
        imageButton = findViewById(R.id.sendBtn);
        firebaseAuth = FirebaseAuth.getInstance();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);



//        apiService= Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);



        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Patients");


        query = databaseReference.orderByChild("uid").equalTo(hisUid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = "" + ds.child("name").getValue();
                    hisImage = "" + ds.child("image").getValue();
                    String onlineStatus = "" + ds.child("onlineStatus").getValue();
                    String typingStatus= "" + ds.child("typingTo").getValue();


                    if(typingStatus.equals(myUid)){
                        userStatusTv.setText("typing...");
                    }
                    else{

                        if (onlineStatus.equals("online")) {
                            userStatusTv.setText(onlineStatus);
                        } else {

                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            try {
                                calendar.setTimeInMillis(Long.parseLong(onlineStatus));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            String dateTime = DateFormat.format("dd/MM/yyy hh:mm aa", calendar).toString();
                            userStatusTv.setText("Last seen at:" + dateTime);
                        }

                    }


                    nameTv.setText(name);


                    try {
                        Picasso.get().load(hisImage).placeholder(R.drawable.ic_person_black_24dp).into(profileTv);

                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_person_black_24dp).into(profileTv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notify=true;
                String message = messageEt.getText().toString().trim();

                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(ChatActivity.this, "Cannot send the empty message...", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(message);
                }

            }
        });

        messageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    checkTypingStatus("noOne");
                } else {
                    checkTypingStatus(hisUid);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        readMessages();
        seenMessages();


    }


    @Override
    protected void onStart() {
        checkUserStatus();
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        String timeStamp = String.valueOf(System.currentTimeMillis());

        checkOnlineStatus(timeStamp);
        checkTypingStatus("noOne");
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onResume() {
        checkOnlineStatus("online");
        super.onResume();
    }

    private void checkUserStatus() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {

            myUid = firebaseUser.getUid();

        } else {
            startActivity(new Intent(getApplicationContext(), HospitalHomeActivity.class));
            finish();
        }
    }

    private void sendMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String timeStamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timeStamp", timeStamp);
        hashMap.put("isSeen", false);

        databaseReference.child("Chats").push().setValue(hashMap);

        messageEt.setText("");
//
//        String msg=message;
//        DatabaseReference database=FirebaseDatabase.getInstance().getReference("paths").child(myUid);
//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ModelUser modelUser=dataSnapshot.getValue(ModelUser.class);
//
//                if(notify){
//                    sendNotifi
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        })

    }

    private void seenMessages() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (myUid.equals(chat.getReceiver()) && hisUid.equals(chat.getSender())) {
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("isSeen", true);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages() {

        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (myUid.equals(chat.getReceiver()) && hisUid.equals(chat.getSender()) ||
                            hisUid.equals(chat.getReceiver()) && myUid.equals(chat.getSender())) {
                        chatList.add(chat);
                    }
                    adapterChat = new AdapterChat(ChatActivity.this, chatList, hisImage);
                    adapterChat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChat);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkOnlineStatus(String status) {
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Patients").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status);

        dRef.updateChildren(hashMap);
    }

    private void checkTypingStatus(String typing) {
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Patients").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("typingTo", typing);

        dRef.updateChildren(hashMap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        menu.findItem(R.id.action_search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            checkUserStatus();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
