package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.downloadable_resource_model;
import java.util.List;

/* loaded from: classes4.dex */
public class PageImageBrowserAdapter extends PagerAdapter {
    List<downloadable_resource_model> listImage;

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        List<downloadable_resource_model> list = this.listImage;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public PageImageBrowserAdapter(List<downloadable_resource_model> list) {
        this.listImage = list;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_slide_image, viewGroup, false);
        Glide.with(viewGroup.getContext()).load(this.listImage.get(i).getURL()).placeholder(R.drawable.ic_image_default).into((ImageView) inflate.findViewById(R.id.imageView));
        viewGroup.addView(inflate);
        return inflate;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return view.equals(obj);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    public void clearItems() {
        this.listImage.clear();
    }

    public void addItems(List<downloadable_resource_model> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        this.listImage.clear();
        this.listImage.addAll(list);
        if (list.size() > 0) {
            list.get(0).isSelected = true;
        }
        notifyDataSetChanged();
    }
}
