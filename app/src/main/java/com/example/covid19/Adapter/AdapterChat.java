package com.example.covid19.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19.Model.ModelChat;
import com.example.covid19.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {


    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<ModelChat> chatList;
    private String imageUrl;
    private FirebaseUser firebaseUser;


    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == MSG_TYPE_RIGHT) {
            View  layoutView = LayoutInflater.from(context).inflate(R.layout.row_chat_right,parent,false);
            return new MyHolder(layoutView);
        } else {
            View  layoutView = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent,false);
            return new MyHolder(layoutView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder,final int position) {

        String message=chatList.get(position).getMessage();
        String timeStamp=chatList.get(position).getTimestamp();

        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        try {
            calendar.setTimeInMillis(Long.parseLong(timeStamp));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        String dateTime= DateFormat.format("dd/MM/yyy hh:mm aa",calendar ).toString();

        holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);

        try{
            Picasso.get().load(imageUrl).into(holder.profileTv);
        }catch (Exception e){

        }

        //click a message to delete dialog
        holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog alert
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this message");
                //delete button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(position);
                    }
                });
                //cancel deleting
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss dialog
                        dialog.dismiss();
                    }
                });
                //create and show dialog
                builder.create().show();
            }
        });

        if (position==chatList.size()-1){
            if (chatList.get(position).isSeen()){
                holder.isSeenTv.setText("Seen");
            }
            else{
                holder.isSeenTv.setText("Delivered");
            }
        }
        else{
            holder.isSeenTv.setVisibility(View.GONE);
        }


    }

        private void deleteMessage(int position) {
        String mUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        //get timestamp of message
        //compare to timestamp clicked of message with all message
        // Where both values matches delete that message
        String msgTimeStamp=chatList.get(position).getTimestamp();
        DatabaseReference df= FirebaseDatabase.getInstance().getReference("Chats");

        Query query=df.orderByChild("timeStamp").equalTo(msgTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    if(ds.child("sender").getValue().equals(mUid)) {

                        //remove the message ,,set the value thi message deleted..
                      ds.getRef().removeValue();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("message", "This message was deleted");
                        ds.getRef().updateChildren(hashMap);
                        Toast.makeText(context,"Message deleted",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context,"You can only delete your messages",Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class  MyHolder extends RecyclerView.ViewHolder{

        ImageView profileTv;
        TextView messageTv,timeTv,isSeenTv;
        LinearLayout messageLayout;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profileTv=itemView.findViewById(R.id.profile_tv);
            messageTv=itemView.findViewById(R.id.message_tv);
            timeTv=itemView.findViewById(R.id.timeTV);
            isSeenTv=itemView.findViewById(R.id.isSeenTv);
            messageLayout=itemView.findViewById(R.id.messageLayout);
        }
    }

}
