package com.example.covid19.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.covid19.Entity.Patients;
import com.example.covid19.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Register extends AppCompatActivity {

    private EditText username, email, password, password2;
    private ImageView userphoto;
    private ProgressBar loadprogress,loadprogress_hospital;
    private Button regbutton, reg_btn_hospital;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private TextView account_text;
    private DatabaseReference reff;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedphotouri;
    long maxId=0;
    private static final String Patients="Patients";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        userphoto = findViewById(R.id.reg_userphoto);
        username = findViewById(R.id.regName);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_password);
        password2 = findViewById(R.id.reg_password2);
        loadprogress = findViewById(R.id.regProgressBar);
        loadprogress_hospital = findViewById(R.id.reg_hospital_ProgressBar);
        regbutton = findViewById(R.id.reg_btn);
        reg_btn_hospital=findViewById(R.id.reg_btn_hospital);
        loadprogress.setVisibility(View.INVISIBLE);
        loadprogress_hospital.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        account_text=findViewById(R.id.txtaccount);

        reff=FirebaseDatabase.getInstance().getReference().child("Patients");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxId=(dataSnapshot.getChildrenCount());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(Patients);


        SpannableString ss=new SpannableString("Have an account? Log in");
        ClickableSpan cs=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent log_intent=new Intent(getApplicationContext(),Login.class);
                startActivity(log_intent);

            }
        };

        ss.setSpan(cs,17,23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        account_text.setText(ss);
        account_text.setMovementMethod(LinkMovementMethod.getInstance());


        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regbutton.setVisibility(View.INVISIBLE);
                loadprogress.setVisibility(View.VISIBLE);
                final String submit_name = username.getText().toString();
                final String submit_email = email.getText().toString();
                final String submit_password = password.getText().toString();
                final String submit_password2 = password2.getText().toString();
                final String type="patient";
                final String onlineStatus="online";
                final String typingTo="noOne";




                if (submit_name.isEmpty() || submit_email.isEmpty() || submit_password.isEmpty() || submit_password2.isEmpty()) {
                    if(!submit_password.equals(submit_password2)){
                        showMessage("Password are not same");
                        regbutton.setVisibility(View.VISIBLE);
                        loadprogress.setVisibility(View.INVISIBLE);

                    }
                    showMessage("Please fill all fields");
                    regbutton.setVisibility(View.VISIBLE);
                    loadprogress.setVisibility(View.INVISIBLE);



                } else {
                    mAuth.createUserWithEmailAndPassword(submit_email, submit_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser fuser= mAuth.getCurrentUser();
                                final String uid=fuser.getUid();

                                Patients user = new Patients(submit_name, submit_email,type,uid,onlineStatus,typingTo);
                                reff.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        loadprogress_hospital.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this,"Successfully created" , Toast.LENGTH_LONG).show();
                                            reg_btn_hospital.setVisibility(View.VISIBLE);
                                            loadprogress_hospital.setVisibility(View.INVISIBLE);
                                            Intent intent=new Intent(getApplicationContext(),Login.class);
                                            startActivity(intent);
                                        } else {
                                            showMessage("Account creation failed" + task.getException().getMessage());
                                            reg_btn_hospital.setVisibility(View.VISIBLE);
                                            loadprogress_hospital.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });

                            } else {
                                //acount does not created
                                showMessage("Account creation failed" + task.getException().getMessage());
                                regbutton.setVisibility(View.VISIBLE);
                                loadprogress.setVisibility(View.INVISIBLE);
                            }
                        }
                    });


                }

            }
        });


        reg_btn_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(getApplicationContext(),RegisterLikeHospital.class);
              startActivity(intent);

            }
        });


    }

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }




}
