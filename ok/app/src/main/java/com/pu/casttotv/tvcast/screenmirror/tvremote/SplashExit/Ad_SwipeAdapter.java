package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

import java.util.Objects;


public class Ad_SwipeAdapter extends PagerAdapter {

    Context context;
    int[] images;
    String[] name;
    String[] name1;
    LayoutInflater mLayoutInflater;

    public Ad_SwipeAdapter(Ad_SwipeScreenActivity screenMirrorGuide, int[] images, String[] name, String[] name1) {
        this.context = screenMirrorGuide;
        this.images = images;
        this.name = name;
        this.name1 = name1;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container,int position) {

        View itemView = mLayoutInflater.inflate(R.layout.ad_swipe_adpater, container, false);
        ImageView imageGuide = (ImageView) itemView.findViewById(R.id.imageGuide);
        imageGuide.setImageResource(images[position]);
        TextView textInfo = (TextView) itemView.findViewById(R.id.textInfo);
        textInfo.setText(name[position]);
        TextView textInfo1 = (TextView) itemView.findViewById(R.id.textInfo1);
        textInfo1.setText(name1[position]);
        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
