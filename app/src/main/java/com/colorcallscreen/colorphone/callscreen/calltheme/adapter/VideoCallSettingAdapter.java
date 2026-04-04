package com.colorcallscreen.colorphone.callscreen.calltheme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.controller.VideoCallController;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.VideoCallSettingModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import java.util.List;


public class VideoCallSettingAdapter extends RecyclerView.Adapter<VideoCallSettingAdapter.VCHolder> {
    private AppSelectedListener appSelectedListener;
    private Context context;
    private List<VideoCallSettingModel> list;
    private VideoCallController.VideoCallApps selectedApp = VideoCallController.Settings.getApp();

    
    public interface AppSelectedListener {
        void onAppSelected(VideoCallController.VideoCallApps videoCallApps);
    }

    
    public class VCHolder extends RecyclerView.ViewHolder {
        TextView appName;
        ImageView icon;
        ImageView tick;

        public VCHolder(View view) {
            super(view);
            this.appName = (TextView) view.findViewById(R.id.app_name);
            this.icon = (ImageView) view.findViewById(R.id.icon);
            ImageView imageView = (ImageView) view.findViewById(R.id.tick);
            this.tick = imageView;
            imageView.setVisibility(8);
        }
    }

    public VideoCallSettingAdapter(Context context, List<VideoCallSettingModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override 
    public int getItemCount() {
        return this.list.size();
    }

    public void setAppSelectedListener(AppSelectedListener appSelectedListener) {
        this.appSelectedListener = appSelectedListener;
    }

    @Override 
    public void onBindViewHolder(VCHolder vCHolder, int i) {
        final VideoCallSettingModel videoCallSettingModel = this.list.get(i);
        vCHolder.appName.setText(videoCallSettingModel.getName());
        vCHolder.icon.setImageResource(Utility.getResourceByName(this.context, videoCallSettingModel.getIcon(), "drawable"));
        if (VideoCallController.VideoCallApps.valueOf(videoCallSettingModel.getId()).equals(this.selectedApp)) {
            vCHolder.tick.setVisibility(0);
        } else {
            vCHolder.tick.setVisibility(8);
        }
        vCHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.VideoCallSettingAdapter.1
            @Override 
            public void onClick(View view) {
                if (VideoCallSettingAdapter.this.appSelectedListener != null) {
                    VideoCallSettingAdapter.this.appSelectedListener.onAppSelected(VideoCallController.VideoCallApps.valueOf(videoCallSettingModel.getId()));
                    VideoCallSettingAdapter.this.selectedApp = VideoCallController.Settings.getApp();
                    VideoCallSettingAdapter.this.notifyDataSetChanged();
                }
            }
        });
    }

    @Override 
    public VCHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VCHolder(LayoutInflater.from(this.context).inflate(R.layout.video_call_row_app, viewGroup, false));
    }
}
