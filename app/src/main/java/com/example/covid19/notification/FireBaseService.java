package com.example.covid19.notification;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class FireBaseService  extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String tokenRefresh= FirebaseInstanceId.getInstance().getToken();
        if(firebaseUser!=null){
            updateToken(tokenRefresh);

        }
    }

    private void updateToken(String tokenRefresh) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token=new Token(tokenRefresh);
        reference.child(user.getUid()).setValue(token);



    }
}
