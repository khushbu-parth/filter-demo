package com.cast.tv.screen.mirroring.screencasting.TvAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.R;

import java.util.List;

public class PhotoViewPagerAdapter extends PagerAdapter {
    private final List<FileModel> mList;

    public PhotoViewPagerAdapter(List<FileModel> list) {
        this.mList = list;
    }

    @Override
    public int getItemPosition(Object obj) {
        return -2;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public int getCount() {
        List<FileModel> list = this.mList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        ImageView imageView = new ImageView(viewGroup.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        Glide.with(viewGroup.getContext()).load(this.mList.get(i).getTempPath()).error((int) R.color.color_BFBFBF).into(imageView);
        viewGroup.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }
}
