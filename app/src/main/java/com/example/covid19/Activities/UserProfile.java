package com.example.covid19.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.covid19.Fragments.ProfileFragment;
import com.example.covid19.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UserProfile";
    private static final int TAKE_IMAGE_CODE = 10001;
    private ImageView userphoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedphotouri;
    private static int LOAD_IMAGE_RESULTS = 1;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private TextView nameTv, emailTv;
    private FloatingActionButton fab;
    private ProgressDialog progressDialog;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userphoto = findViewById(R.id.profile_photo);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Patients");
        userphoto.setOnClickListener(this);
        fab = findViewById(R.id.fab);
        progressDialog = new ProgressDialog(UserProfile.this);
        storageReference= getInstance().getReference();



        loadUserData();
        fabClick();


        Query query = databaseReference.orderByChild("mail").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("mail").getValue();

                    nameTv.setText(name);
                    emailTv.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });
    }

    private void showEditProfileDialog() {
        String option[] = {"Edit Name"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setTitle("Choose Action");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    progressDialog.setMessage("Update Name");
                    showNameUpdateDialog("name");
                }
            }
        });
        builder.create().show();
    }

    private void showNameUpdateDialog(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setTitle("Update " + name);

        LinearLayout linearLayout = new LinearLayout(UserProfile.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);

        EditText editText = new EditText(UserProfile.this);
        editText.setHint("Enter " + name);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();

                if (!TextUtils.isEmpty(value)) {
                    progressDialog.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(name, value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Updated..." , Toast.LENGTH_SHORT).show();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Please enter " + e.getMessage(), Toast.LENGTH_SHORT).show();


                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter " + name, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                //the user has successfully picked a photo
                //we need to save its reference to a Uri variable
//                             pickedphotouri = data.getData();
//                userphoto.setImageURI(pickedphotouri);

                pickedphotouri = result.getUri();
                userphoto.setImageURI(pickedphotouri);
                updateUserInfo(pickedphotouri, mAuth.getCurrentUser());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = result.getError();
                Log.d("error", e.toString());
            }
        }

    }
//    private void checkAndRequestForPermission() {
//        if (ContextCompat.checkSelfPermission(UserProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(UserProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                Toast.makeText(UserProfile.this, "Please accept for required permission", Toast.LENGTH_SHORT ).show();
//            } else {
//
//                ActivityCompat.requestPermissions(UserProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
//
//
//            }
//
//
//        } else {
//            openGallery();
//        }
//
//
//    }


//    private void updateUI() {
//        Intent afterRegister=new Intent(getApplicationContext(), UserProfile.class);
//        startActivity(afterRegister);
//        finish();
//    }
//
//    private void showMessage(String message) {
//
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, TAKE_IMAGE_CODE);
//        }

        if (v == userphoto) {
            CropImage.activity().start(UserProfile.this);


//            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//            // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
//            startActivityForResult(i,LOAD_IMAGE_RESULTS); //LOAD_IMAGE_RESULTS
        }
    }


    private void updateUserInfo(Uri pickedphotouri, final FirebaseUser currentUser) {
        StorageReference storageReference = getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = storageReference.child(pickedphotouri.getLastPathSegment());
        imageFilePath.putFile(pickedphotouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //IMAGE UPLOAD SUCCESFULLY
                //now we can get our image path
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //URI CONTAINS user image uri
                        UserProfileChangeRequest profileupdate = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Profile image uploaded", Toast.LENGTH_LONG).show();

                                }
                            }

                        });
                    }
                });

            }
        });
    }

    private void loadUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(TAG, "onCreate" + user.getDisplayName());

            if (user.getPhotoUrl() != null) {

                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(userphoto);
            }
//        FirebaseUser user = mAuth.getCurrentUser();
//
//        //If no profile picture found
//        if(user.getPhotoUrl() != null)
//        {
//            String photoUrl = user.getPhotoUrl().toString();
//
//            //Set profile picture
//            Glide.with(this)
//                    .load(user.getPhotoUrl().toString())
//                    .into(userphoto);
//        }
////
//        //If no username found
//        if(user.getDisplayName() != null)
//        {
//            String username = user.getDisplayName();
//
//            //Insert display name
//            inputUsername.setText(user.getDisplayName());
//        }


        }

    }
}


