package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.DialogShowImage;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.ManagerDataPlay;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast.PlayCastActivity;
import java.util.ArrayList;

/* loaded from: classes4.dex */
public class ResultHolderAdapter extends RecyclerView.Adapter<ResultHolderAdapter.MyViewHolder> {
    file_type _type;
    private Activity activity;
    private ArrayList<downloadable_resource_model> listData;
    private Context mContext;

    public ResultHolderAdapter(Context context, file_type file_typeVar, Activity activity, ArrayList<downloadable_resource_model> arrayList) {
        this.mContext = context;
        this.activity = activity;
        this._type = file_typeVar;
        this.listData = arrayList;
    }

    public void setData(ArrayList<downloadable_resource_model> arrayList) {
        this.listData.clear();
        this.listData.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.download_result_list, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        try {
            final downloadable_resource_model downloadable_resource_modelVar = this.listData.get(i);
            if (downloadable_resource_modelVar != null && downloadable_resource_modelVar.getFile_size() != null && !downloadable_resource_modelVar.getFile_size().equals("")) {
                myViewHolder.txtVidSize.setText(downloadable_resource_modelVar.getFile_size());
            }
            if (downloadable_resource_modelVar == null) {
                return;
            }
            TextView textView = myViewHolder.tv_film_name;
            textView.setText(downloadable_resource_modelVar.getTitle() + "");
            if (this._type == file_type.IMAGE) {
                Glide.with(this.mContext).load(downloadable_resource_modelVar.getURL()).placeholder(R.drawable.ic_image_default).error(R.drawable.ic_image_default).into(myViewHolder.iv_poster);
            } else {
                Glide.with(this.mContext).load(downloadable_resource_modelVar.getURL()).placeholder(R.drawable.ic_video_default).error(R.drawable.ic_video_default).into(myViewHolder.iv_poster);
            }
            myViewHolder.iv_poster.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.ResultHolderAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (downloadable_resource_modelVar.getFile_type() == file_type.IMAGE) {
                        DialogShowImage dialogShowImage = new DialogShowImage(ResultHolderAdapter.this.mContext);
                        dialogShowImage.setLinkUrl(downloadable_resource_modelVar.getURL());
                        dialogShowImage.show();
                    }
                }
            });
            myViewHolder.btnDownload.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.ResultHolderAdapter.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (!TVConnectUtils.getInstance().isConnected()) {
                        ResultHolderAdapter.this.mContext.startActivity(new Intent(ResultHolderAdapter.this.mContext, ConnectActivity.class));
                        com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils.nextScreen(ResultHolderAdapter.this.activity);
                        return;
                    }
                    Intent intent = new Intent(ResultHolderAdapter.this.mContext, PlayCastActivity.class);
                    if (downloadable_resource_modelVar.getFile_type() == file_type.VIDEO) {
                        ManagerDataPlay.getInstance().setPosSelected(0);
                        ManagerDataPlay.getInstance().setTypePlay(8);
                    } else if (downloadable_resource_modelVar.getFile_type() == file_type.IMAGE) {
                        ManagerDataPlay.getInstance().setTypePlay(9);
                        ManagerDataPlay.getInstance().setPosSelected(i);
                    } else if (downloadable_resource_modelVar.getFile_type() == file_type.AUDIO) {
                        ManagerDataPlay.getInstance().setTypePlay(10);
                        ManagerDataPlay.getInstance().setPosSelected(i);
                    }
                    ManagerDataPlay.getInstance().titleCast = downloadable_resource_modelVar.getTitle();
                    ManagerDataPlay.getInstance().pathCast = downloadable_resource_modelVar.getURL();
                    ManagerDataPlay.getInstance().thumbCast = downloadable_resource_modelVar.getURL();
                    ManagerDataPlay.getInstance().duration = 0L;
                    ResultHolderAdapter.this.mContext.startActivity(intent);
                }
            });
            myViewHolder.btnPreview.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.ResultHolderAdapter.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    downloadable_resource_modelVar.getURL();
                    ResultHolderAdapter.this.getMedia(downloadable_resource_modelVar.getURL()).toString();
                    if (downloadable_resource_modelVar.getFile_type() == file_type.VIDEO || downloadable_resource_modelVar.getFile_type() == file_type.AUDIO) {
                        new video_player(downloadable_resource_modelVar).show(((FragmentActivity) ResultHolderAdapter.this.mContext).getSupportFragmentManager(), "TAG");
                    } else if (downloadable_resource_modelVar.getFile_type() != file_type.IMAGE) {
                    } else {
                        DialogShowImage dialogShowImage = new DialogShowImage(ResultHolderAdapter.this.mContext);
                        dialogShowImage.setLinkUrl(downloadable_resource_modelVar.getURL());
                        dialogShowImage.show();
                    }
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Uri getMedia(String str) {
        if (URLUtil.isValidUrl(str)) {
            return Uri.parse(str);
        }
        return Uri.parse("android.resource://" + this.mContext.getPackageName() + "/raw/" + str);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.listData.size();
    }

    /* loaded from: classes4.dex */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button btnDownload;
        public Button btnPreview;
        public RoundedImageView iv_poster;
        public TextView tv_film_name;
        public TextView txtVidSize;

        public MyViewHolder(ResultHolderAdapter resultHolderAdapter, View view) {
            super(view);
            this.iv_poster = (RoundedImageView) view.findViewById(R.id.iv_poster);
            this.tv_film_name = (TextView) view.findViewById(R.id.tv_film_name);
            this.btnDownload = (Button) view.findViewById(R.id.btnDownload);
            this.txtVidSize = (TextView) view.findViewById(R.id.txtVidSize);
            this.btnPreview = (Button) view.findViewById(R.id.btnPreview);
        }
    }
}
