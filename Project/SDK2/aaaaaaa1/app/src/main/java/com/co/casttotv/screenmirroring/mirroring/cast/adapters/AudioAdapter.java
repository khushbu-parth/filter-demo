package com.co.casttotv.screenmirroring.mirroring.cast.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.co.casttotv.screenmirroring.mirroring.cast.BR;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.activities.PlayMediaActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.activities.ScanningActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.Config;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.DeviceManager;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.MediaData;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ItemAudioBinding;
import com.co.casttotv.screenmirroring.mirroring.cast.models.MediaModel;

import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {

    Context mContext;
    ArrayList<MediaModel> modelList;

    public AudioAdapter(Context mContext, ArrayList<MediaModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAudioBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_audio, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(modelList.get(position));
        holder.itemView.setOnClickListener(view -> {
            Config.selectedImageFolderList = modelList;
            Config.selectedPosition = position;

            if (DeviceManager.getInstance().isConnected()) {
                mContext.startActivity(new Intent(mContext, PlayMediaActivity.class)
                        .putExtra(MediaData.TYPE_MEDIA_KEY, MediaData.TYPE_AUDIO)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            } else {
                mContext.startActivity(new Intent(mContext, ScanningActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (modelList != null) return modelList.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemAudioBinding binding;

        public ViewHolder(@NonNull ItemAudioBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(Object data) {
            binding.setVariable(BR.audio, data);
            binding.executePendingBindings();
        }
    }

}
