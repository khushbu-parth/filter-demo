package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.whatsappdata;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;


public class pictureRecyclerAdapter extends RecyclerView.Adapter<pictureRecyclerAdapter.MyViewHolder> {
    public Activity activity;
    private ArrayList<File> filesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView download;
        RelativeLayout main_rel;
        ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.pictures_thumbnail);
            this.download = (TextView) view.findViewById(R.id.save_picture);
            this.main_rel = (RelativeLayout) view.findViewById(R.id.main_rel);
        }
    }

    public pictureRecyclerAdapter(ArrayList<File> arrayList, Activity activity2) {
        this.filesList = arrayList;
        this.activity = activity2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pictures_item_view, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final File file = this.filesList.get(i);
        Glide.with(this.activity).load(file.getAbsolutePath()).into(myViewHolder.thumbnail);
        myViewHolder.main_rel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(pictureRecyclerAdapter.this.activity, WAPreviewpix.class);
                intent.putExtra("video_url", file.getAbsolutePath().toString());
                intent.putExtra("CurrentFile", file.toString());
                intent.putExtra("FileName", file.getName().toString());
                pictureRecyclerAdapter.this.activity.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.filesList.size();
    }

    public void copyFile(File file, File file2, Activity activity2) throws IOException {
        if (!file2.getParentFile().exists()) {
            file2.getParentFile().mkdirs();
        }
        if (!file2.exists()) {
            file2.createNewFile();
        }
        FileChannel channel = new FileInputStream(file).getChannel();
        try {
            FileChannel channel2 = new FileOutputStream(file2).getChannel();
            try {
                channel2.transferFrom(channel, 0, file.length());
                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                intent.setData(Uri.fromFile(file2));
                activity2.sendBroadcast(intent);
                if (channel != null) {
                    channel.close();
                }
                if (channel2 != null) {
                    channel2.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
