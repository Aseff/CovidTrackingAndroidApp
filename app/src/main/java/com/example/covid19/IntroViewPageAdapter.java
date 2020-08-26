package com.example.covid19;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class IntroViewPageAdapter extends PagerAdapter {


    Context context;
    List<ScreenItem> ListscreenItems;

    public IntroViewPageAdapter(Context context, List<ScreenItem> listscreenItems) {
        this.context = context;
        ListscreenItems = listscreenItems;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen= layoutInflater.inflate(R.layout.layout_screen,null);


        ImageView imageView= layoutScreen.findViewById(R.id.imagesample);
        TextView title=layoutScreen.findViewById(R.id.introtxt);
        TextView description=layoutScreen.findViewById(R.id.introdesc);
        description.setMovementMethod(new ScrollingMovementMethod());

        title.setText(ListscreenItems.get(position).getTitle());
        description.setText(ListscreenItems.get(position).getDescription());
        imageView.setImageResource(ListscreenItems.get(position).getScreenImg());



        container.addView(layoutScreen);

        return layoutScreen;


    }

    @Override
    public int getCount() {
        return ListscreenItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
