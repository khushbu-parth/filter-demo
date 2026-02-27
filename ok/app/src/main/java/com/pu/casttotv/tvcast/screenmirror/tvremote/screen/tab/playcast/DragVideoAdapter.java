package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MediaModel;
import com.wonshinhyo.dragrecyclerview.DragAdapter;
import com.wonshinhyo.dragrecyclerview.DragHolder;
import java.io.File;
import java.util.ArrayList;

/* loaded from: classes4.dex */
public class DragVideoAdapter extends DragAdapter {
    private ArrayList<MediaModel> arrVideo;
    private Context mContext;
    private IClickItem mListener;
    private String titleSelected;

    /* loaded from: classes4.dex */
    public interface IClickItem {
        void onClickItem(MediaModel mediaModel, int i);

        void onDeleteItem(MediaModel mediaModel, int i);
    }

    public DragVideoAdapter(Context context, ArrayList<MediaModel> arrayList, String str) {
        super(context, arrayList);
        this.arrVideo = new ArrayList<>();
        this.titleSelected = "";
        this.arrVideo = arrayList;
        this.mContext = context;
        this.titleSelected = str;
    }

    public void setListener(IClickItem iClickItem) {
        this.mListener = iClickItem;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.item_video_drag, viewGroup, false), i);
    }

    @Override // com.wonshinhyo.dragrecyclerview.DragAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
        ((Holder) viewHolder).binData(this.arrVideo.get(i), i);
    }

    /* loaded from: classes4.dex */
    private final class Holder extends DragHolder {
        ImageView img_video_offline;
        ImageView imv_close;
        RelativeLayout rl_item;
        TextView tv_name;

        Holder(View view, int i) {
            super(view);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
            this.img_video_offline = (ImageView) view.findViewById(R.id.img_video_offline);
            this.imv_close = (ImageView) view.findViewById(R.id.imv_close);
            this.rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);
        }

        public void binData(final MediaModel mediaModel, final int i) {
            Glide.with(DragVideoAdapter.this.mContext).load(Uri.fromFile(new File(mediaModel.getPhotoUri()))).into(this.img_video_offline);
            this.tv_name.setText(mediaModel.getTitle());
            if (DragVideoAdapter.this.titleSelected == null || DragVideoAdapter.this.titleSelected.isEmpty() || !DragVideoAdapter.this.titleSelected.equalsIgnoreCase(mediaModel.getTitle())) {
                this.rl_item.setBackgroundColor(DragVideoAdapter.this.mContext.getResources().getColor(R.color.color_transparent));
            } else {
                this.rl_item.setBackground(DragVideoAdapter.this.mContext.getDrawable(R.drawable.bg_item_selected));
            }
            this.imv_close.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.DragVideoAdapter.Holder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    DragVideoAdapter.this.mListener.onDeleteItem(mediaModel, i);
                }
            });
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.DragVideoAdapter.Holder.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    DragVideoAdapter.this.mListener.onClickItem(mediaModel, i);
                }
            });
        }
    }
}
