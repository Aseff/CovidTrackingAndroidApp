package com.example.covid19.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid19.Entity.Hospital;
import com.example.covid19.R;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterLikeHospital extends AppCompatActivity {
    private EditText username, emaill, password1, password2, url;
    private ProgressBar loadprogress_hospital;
    private Button reg_btn_hospital;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private TextView account_text;
    private DatabaseReference reff;

    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedphotouri;
    long maxId=0;

    private static final String Hospital="Hospital";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_like_hospital);


        username = findViewById(R.id.regName_hos);
        emaill = findViewById(R.id.reg_email_hos);
        password1 = findViewById(R.id.reg_pass);
        password2 = findViewById(R.id.reg_pass2);
        loadprogress_hospital = findViewById(R.id.reg_prog);
        reg_btn_hospital = findViewById(R.id.reg_hos);
        url = findViewById(R.id.reg_hook);
        loadprogress_hospital.setVisibility(View.INVISIBLE);
        account_text = findViewById(R.id.txtaccount_hos);
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        reff=FirebaseDatabase.getInstance().getReference().child("Hospital");

//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    maxId=(dataSnapshot.getChildrenCount());
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        SpannableString ss = new SpannableString("Have an account? Log in");
        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent log_intent = new Intent(getApplicationContext(), Login.class);
                startActivity(log_intent);

            }
        };

        ss.setSpan(cs, 17, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        account_text.setText(ss);
        account_text.setMovementMethod(LinkMovementMethod.getInstance());


                reg_btn_hospital.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reg_btn_hospital.setVisibility(View.INVISIBLE);
                        loadprogress_hospital.setVisibility(View.VISIBLE);

                        final  String name = username.getText().toString();
                        final  String submit_email = emaill.getText().toString();
                        final  String submit_password = password1.getText().toString();
                        final  String password_second = password2.getText().toString();
                        final  String hook_url = url.getText().toString();
                        final String type="hospital";


                        if (name.isEmpty() || submit_email.isEmpty() || submit_password.isEmpty() || password_second.isEmpty() || hook_url.isEmpty()) {
                            if (!submit_password.equals(password_second)) {
                                showMessage("Password are not same");
                                reg_btn_hospital.setVisibility(View.VISIBLE);
                                loadprogress_hospital.setVisibility(View.INVISIBLE);
                            }
                            showMessage("Please fill all fields");
                            reg_btn_hospital.setVisibility(View.VISIBLE);
                            loadprogress_hospital.setVisibility(View.INVISIBLE);

                        } else {
                            mAuth.createUserWithEmailAndPassword(submit_email, submit_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Hospital user = new Hospital(name, submit_email, hook_url,type);

                                        reff.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                               loadprogress_hospital.setVisibility(View.GONE);
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterLikeHospital.this,"Successfully created" , Toast.LENGTH_LONG).show();
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
                                        reg_btn_hospital.setVisibility(View.VISIBLE);
                                        loadprogress_hospital.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });


                        }

                    }
                });


            }




            public void showMessage(String message){

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }


        }





