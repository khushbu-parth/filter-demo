package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MediaModel;
import java.io.File;
import java.util.List;

/* loaded from: classes4.dex */
public class PageImageAdapter extends PagerAdapter {
    List<MediaModel> listImage;

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        List<MediaModel> list = this.listImage;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public PageImageAdapter(List<MediaModel> list) {
        this.listImage = list;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_slide_image, viewGroup, false);
        Glide.with(viewGroup.getContext()).load(Uri.fromFile(new File(this.listImage.get(i).getPhotoUri()))).into((ImageView) inflate.findViewById(R.id.imageView));
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

    public void addItems(List<MediaModel> list) {
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
