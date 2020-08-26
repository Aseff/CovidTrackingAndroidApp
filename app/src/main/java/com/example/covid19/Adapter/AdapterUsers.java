package com.example.covid19.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19.Activities.ChatActivity;
import com.example.covid19.Model.ModelUser;
import com.example.covid19.R;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {
    Context context;
    List<ModelUser> modelUserList;

    public AdapterUsers(Context context, List<ModelUser> modelUserList) {
        this.context = context;
        this.modelUserList = modelUserList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_user,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String hisUid=modelUserList.get(position).getUid();
        String username=modelUserList.get(position).getName();
        String email=modelUserList.get(position).getEmail();

        holder.mNameTv.setText(username);
        holder.mEmailTv.setText(email);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid",hisUid);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return modelUserList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{
            ImageView mavatarIv;
            TextView mNameTv,mEmailTv;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                mavatarIv=itemView.findViewById(R.id.avatarIv);
                mNameTv=itemView.findViewById(R.id.nameTv);
                mEmailTv=itemView.findViewById(R.id.emailTv);


            }


        }

}
