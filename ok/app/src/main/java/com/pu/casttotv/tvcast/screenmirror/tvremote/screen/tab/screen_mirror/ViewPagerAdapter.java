package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ViewPagerAdapter extends PagerAdapter {
    int[] arrImage;
    Context context;
    LayoutInflater mLayoutInflater;

    @SuppressLint("WrongConstant")
    public ViewPagerAdapter(Context context, int[] iArr) {
        this.context = context;
        this.arrImage = iArr;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.arrImage.length;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((ConstraintLayout) obj);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        View inflate = this.mLayoutInflater.inflate(R.layout.item_viewpager, viewGroup, false);
        ((ImageView) inflate.findViewById(R.id.imageSlide)).setImageResource(this.arrImage[i]);
        Objects.requireNonNull(viewGroup);
        viewGroup.addView(inflate);
        return inflate;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((ConstraintLayout) obj);
    }
}
