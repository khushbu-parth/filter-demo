package com.co.casttotv.screenmirroring.mirroring.cast.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.co.casttotv.screenmirroring.mirroring.cast.BR;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.activities.ImageViewActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.activities.PlayMediaActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.activities.ScanningActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.Config;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.DeviceManager;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.MediaData;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ItemImagesBinding;
import com.co.casttotv.screenmirroring.mirroring.cast.models.MediaModel;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    Context mContext;
    ArrayList<MediaModel> modelList;
    Boolean isVideo = false;

    public ImageAdapter(Context mContext, ArrayList<MediaModel> modelList, Boolean isVideo) {
        this.mContext = mContext;
        this.modelList = modelList;
        this.isVideo = isVideo;
    }

    public void Update(ArrayList<MediaModel> list) {
        modelList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImagesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_images, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(modelList.get(position), position);
        holder.binding.imagePlay.setVisibility(isVideo ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        if (modelList != null) return modelList.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemImagesBinding binding;

        public ViewHolder(@NonNull ItemImagesBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(Object data, int i) {
            binding.setVariable(BR.images, data);
            itemView.setOnClickListener(view -> {
                Config.selectedImageFolderList = modelList;
                Config.selectedPosition = i;

                if (DeviceManager.getInstance().isConnected()) {
                    mContext.startActivity(new Intent(mContext, isVideo ? PlayMediaActivity.class : ImageViewActivity.class)
                            .putExtra(MediaData.TYPE_MEDIA_KEY, MediaData.TYPE_VIDEO)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    mContext.startActivity(new Intent(mContext, ScanningActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });
            binding.executePendingBindings();
        }
    }

}
