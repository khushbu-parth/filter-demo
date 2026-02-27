package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.howto;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;

import java.util.List;

/* loaded from: classes4.dex */
public class HtyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HtyModel> listHty;
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    public HtyAdapter(List<HtyModel> list, Context context) {
        this.listHty = list;
        this.context = context;
    }

    public void setData(List<HtyModel> list) {
        this.listHty.clear();
        this.listHty.addAll(list);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (ITEM_VIEW_TYPE_HEADER == i) {
            return new AdViewHolder(LayoutInflater.from(context).inflate(R.layout.ad_native, viewGroup, false));
        } else {
            return new ViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_hty, viewGroup, false));
        }

    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (isHeader(i)) {
            AdsManager.CallNativeAdLoad((Activity) context, ((AdViewHolder) viewHolder).frameLayout, AdsManager.NATIVE_MIDEUM);
            ((AdViewHolder) viewHolder).setIsRecyclable(false);
        } else {
            ((ViewHolder) viewHolder).binData(this.listHty.get(i));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.listHty.size();
    }
    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    public boolean isHeader(int position) {
        return listHty.get(position) == null;
    }

    /* loaded from: classes4.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imv_hty;
        TextView tv_hty;

        public ViewHolder(HtyAdapter htyAdapter, View view) {
            super(view);
            this.imv_hty = (ImageView) view.findViewById(R.id.imv_hty);
            this.tv_hty = (TextView) view.findViewById(R.id.tv_hty);
        }

        public void binData(HtyModel htyModel) {
            this.imv_hty.setImageResource(htyModel.getImage());
            this.tv_hty.setText(htyModel.getMessage());
        }
    }
    public class AdViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayout;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.native_container);
        }
    }
}
