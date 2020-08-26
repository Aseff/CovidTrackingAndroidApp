package com.example.covid19.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.covid19.Entity.Article;
import com.example.covid19.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;

import com.bumptech.glide.request.target.Target;
import com.example.covid19.Util.NewsUtil;

import java.util.List;

public class AdapterForNews  extends RecyclerView.Adapter<AdapterForNews.MyViewHolder> {

    private List<Article> articleList;
    private OnItemClickListener onItemClickListener;
    private Context context;



    public AdapterForNews(List<Article> articles, Context context) {
        this.articleList = articles;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {
        final MyViewHolder myholder=holder;
        Article model=articleList.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(NewsUtil.getRandomDrawbleColor());
        requestOptions.error(NewsUtil.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        myholder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        myholder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        holder.title.setText(model.getTitle());
      //  holder.description.setText(model.getDescription());
      //  holder.source.setText(model.getSource().getName());
      //  holder.time.setText(" \u2022 " + NewsUtil.DateToTimeFormat(model.getPublishedAt()));
        holder.published_ad.setText(NewsUtil.DateFormat(model.getPublished()));

       // holder.author.setText(model.getAuthor());



    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnItemClickListener onItemClickListener;
        TextView title,description,author,published_ad,source,time;
        ImageView imageView;
        ProgressBar progressBar;


        public MyViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            
            title = itemView.findViewById(R.id.title);
          //  description = itemView.findViewById(R.id.desc);
          //  author = itemView.findViewById(R.id.author);
            published_ad = itemView.findViewById(R.id.publishedAt);
//            source = itemView.findViewById(R.id.source);
          //  time = itemView.findViewById(R.id.time);
            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.progress_load_photo);

            this.onItemClickListener =  onItemClickListener;

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}

