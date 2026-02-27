package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;


import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.models.SliderData;

import java.util.List;
import java.util.Objects;

public class Ad_PagerImage extends PagerAdapter {
    Context context;
    List<SliderData> list;
    LayoutInflater mLayoutInflater;

    public Ad_PagerImage(Context context, List<SliderData> list2) {
        this.context = context;
        this.list = list2;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((RelativeLayout) obj);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
        View inflate = this.mLayoutInflater.inflate(R.layout.ad_item_slider, viewGroup, false);
        TextView textView = (TextView) inflate.findViewById(R.id.tvTitle);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tvDes);
        ((ImageView) inflate.findViewById(R.id.imvSlider)).setImageResource(this.list.get(i).getImage());
        if (i == 0) {
            textView.setVisibility(0);
            textView.setText(this.list.get(i).getTitle());
        } else {
            textView.setVisibility(8);
        }
        textView2.setText(this.list.get(i).getDes());
        Objects.requireNonNull(viewGroup);
        viewGroup.addView(inflate);
        return inflate;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((ConstraintLayout) obj);
    }
}
