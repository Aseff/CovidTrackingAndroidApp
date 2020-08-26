package com.example.covid19.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid19.Entity.Hospital;
import com.example.covid19.Entity.Patients;
import com.example.covid19.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private EditText userMail,userPassword;
    private Button btnLogin;
    private ProgressBar loginProgress;
    private FirebaseAuth mAuth;
    private Intent HomeActivity,HospitalActivity;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;

    private Intent HospitalHomeActivity;
    private ImageView loginPhoto;
    private TextView signup_text,mRecover;
    private DatabaseReference reference;
    private   Patients patients;
    private  Hospital hospital;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        userMail = findViewById(R.id.login_mail);
        userPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.loginBtn);
        loginProgress = findViewById(R.id.login_progress);
        mRecover=findViewById(R.id.txtrecoverpassword);
        mAuth = FirebaseAuth.getInstance();
        HomeActivity = new Intent(this,com.example.covid19.Activities.Home.class);
        HospitalActivity=new Intent(this,com.example.covid19.Activities.HospitalHomeActivity.class);
        loginPhoto = findViewById(R.id.login_photo);
        signup_text=findViewById(R.id.txtsignup);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        String mail=userMail.getText().toString();
         String password=userPassword.getText().toString();
         loginProgress.setVisibility(View.INVISIBLE);
         btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mail=userMail.getText().toString();
                String password=userPassword.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    userMail.setError("Invalid email");
                    userMail.setFocusable(true);
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
               }
                else{
                    signIn(mail,password);
                }

            }
        });


        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        ///recover pass textview click
        mRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });
        progressDialog=new ProgressDialog(this);



    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);
        EditText emailtxt=new EditText(this);
        emailtxt.setHint("Email");
        emailtxt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailtxt.setMinEms(16);

        linearLayout.addView(emailtxt);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!emailtxt.equals("")) {
                    String email = emailtxt.getText().toString().trim();
                    beginRecovery(email);
                }
                else{
                    emailtxt.setError("Invalid email");
                    emailtxt.setFocusable(true);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });
        builder.create().show();


    }

    private void beginRecovery(String email) {
        progressDialog.setMessage("Sending email... Please check your email address");
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Email sent",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Failed...",Toast.LENGTH_SHORT).show();

                    }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                //get and show proper error
                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void signIn(String mail, String password) {
          mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Patients patients=new Patients(mail,password);
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                   // onAuthSuccess(task.getResult().getUser());
                  updateUI();
                }
                else {
                    showMessage(task.getException().getMessage());
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);

                }
            }

        });

    }

    private void onAuthSuccess(FirebaseUser user) {
        if (user != null) {
            DatabaseReference refer = FirebaseDatabase.getInstance().getReference("Hospital");
            DatabaseReference refer2 = FirebaseDatabase.getInstance().getReference("Patients");

            refer.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("type").exists()) {

                        if (dataSnapshot.child("type").getValue().toString().equals("hospital")) {
                            startActivity(HospitalActivity);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });








        }

    }

    private void updateUI() {
//        if(FirebaseDatabase.getInstance().getReference("Hospital")
//                .child("type").equals("hospital")){
//            startActivity(HospitalActivity);
//            finish();
//        }
//        else if(FirebaseDatabase.getInstance().getReference("Patients")
//                        .child("type").equals("patient")) {
//            startActivity(HomeActivity);
//            finish();
//        }
        startActivity(HomeActivity);
        finish();


//        Patients patients=new Patients();
//        Hospital hospital=new Hospital();
//        if(hospital.getType().equals("hospital")){
//            startActivity(HospitalActivity);
//        }
//        else if(patients.getType().equals("patient")){
//            startActivity(HomeActivity);
//        }

    }



    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

         database=  FirebaseDatabase.getInstance();
         databaseReference = database.getReference("Hospital");
////
//     Firebase  refer = FirebaseDatabase.getInstance().getReference("Hospital")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if(firebaseUser!=null){
            //so user is connected and we need to redirect to another page
           /// onAuthSuccess(firebaseUser);
           updateUI();
        }
    }
}
