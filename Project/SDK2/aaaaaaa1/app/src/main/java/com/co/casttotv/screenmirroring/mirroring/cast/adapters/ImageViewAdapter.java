package com.co.casttotv.screenmirroring.mirroring.cast.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.co.casttotv.screenmirroring.mirroring.cast.BR;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ItemImageivewBinding;
import com.co.casttotv.screenmirroring.mirroring.cast.models.MediaModel;

import java.util.ArrayList;

public class ImageViewAdapter extends PagerAdapter {

    Context mContext;
    ArrayList<MediaModel> modelList;

    public ImageViewAdapter(Context mContext, ArrayList<MediaModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
        if (modelList != null) return modelList.size();
        return 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ItemImageivewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_imageivew, container, false);

        binding.setVariable(BR.images, modelList.get(position));
        binding.executePendingBindings();

        container.addView(binding.getRoot());
        return binding.getRoot();
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
