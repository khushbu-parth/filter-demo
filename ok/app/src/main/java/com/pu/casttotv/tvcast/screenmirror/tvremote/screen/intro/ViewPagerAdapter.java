package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.intro;

import android.annotation.SuppressLint;
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
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.IntroModel;
import java.util.List;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    LayoutInflater mLayoutInflater;
    List<IntroModel> modelList;

    @SuppressLint("WrongConstant")
    public ViewPagerAdapter(Context context2, List<IntroModel> list) {
        this.context = context2;
        this.modelList = list;
        this.mLayoutInflater = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.modelList.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == ((LinearLayout) obj);
    }

    @NonNull
    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        try {
            view = this.mLayoutInflater.inflate(R.layout.item_slide, viewGroup, false);
            ((ImageView) view.findViewById(R.id.imageSlide)).setImageResource(this.modelList.get(i).getImage());
            ((TextView) view.findViewById(R.id.tvTitle)).setText(this.modelList.get(i).getName());
            ((TextView) view.findViewById(R.id.tvIntro)).setText(this.modelList.get(i).getIntro());
            Objects.requireNonNull(viewGroup);
            viewGroup.addView(view);
            return view;
        } catch (Exception e2) {
            e2.printStackTrace();
            return view;
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, @NonNull Object obj) {
        viewGroup.removeView((LinearLayout) obj);
    }
}
