package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.history.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.HistoryBrowser;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ContentHistoryAdapter.kt */
/* loaded from: classes4.dex */
public final class ContentHistoryAdapter extends RecyclerView.Adapter<ContentHistoryAdapter.MyViewHolder> {
    @NotNull
    private List<HistoryBrowser> historyList = new ArrayList();
    @Nullable
    private IItemClickHistory mListener;

    /* compiled from: ContentHistoryAdapter.kt */
    /* loaded from: classes4.dex */
    public interface IItemClickHistory {
        void clickDelete(@NotNull HistoryBrowser historyBrowser);

        void clickItem(@NotNull HistoryBrowser historyBrowser);
    }

    public final void setData(@NotNull List<HistoryBrowser> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        this.historyList.clear();
        this.historyList.addAll(list);
        notifyDataSetChanged();
    }

    public final void setListener(@NotNull IItemClickHistory iItemClickHistory) {
        Intrinsics.checkNotNullParameter(iItemClickHistory, "iItemClickHistory");
        this.mListener = iItemClickHistory;
    }

    /* compiled from: ContentHistoryAdapter.kt */
    /* loaded from: classes4.dex */
    public static final class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageButton imvRemove;
        private ImageView imvThumb;
        private TextView tvLink;
        private TextView tvTitle;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MyViewHolder(@NotNull View itemView) {
            super(itemView);
            Intrinsics.checkNotNullParameter(itemView, "itemView");
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            this.tvLink = (TextView) itemView.findViewById(R.id.tvLink);
            this.imvRemove = (ImageButton) itemView.findViewById(R.id.imvRemove);
            this.imvThumb = (ImageView) itemView.findViewById(R.id.imvThumb);
        }

        public final TextView getTvTitle() {
            return this.tvTitle;
        }

        public final TextView getTvLink() {
            return this.tvLink;
        }

        public final ImageButton getImvRemove() {
            return this.imvRemove;
        }

        public final ImageView getImvThumb() {
            return this.imvThumb;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NotNull
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int i) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content_history, parent, false);
        Intrinsics.checkNotNullExpressionValue(inflate, "from(parent.context).inf…      false\n            )");
        return new MyViewHolder(inflate);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NotNull MyViewHolder holder, final int i) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        holder.getTvTitle().setText(this.historyList.get(i).getTitle());
        holder.getTvLink().setText(this.historyList.get(i).getLinkUrl());
        holder.getImvRemove().setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.history.adapter.ContentHistoryAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ContentHistoryAdapter.m2253onBindViewHolder$lambda0(ContentHistoryAdapter.this, i, view);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.history.adapter.ContentHistoryAdapter$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ContentHistoryAdapter.m2254onBindViewHolder$lambda1(ContentHistoryAdapter.this, i, view);
            }
        });
        String lowerCase = this.historyList.get(i).getLinkUrl().toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "this as java.lang.String).toLowerCase(Locale.ROOT)");
        switch (Utils.getTypeBrowser(lowerCase)) {
            case 0:
                holder.getImvThumb().setImageResource(R.drawable.ic_facebook);
                return;
            case 1:
                holder.getImvThumb().setImageResource(R.drawable.ic_tiktok);
                return;
            case 2:
                holder.getImvThumb().setImageResource(R.drawable.ic_buzz_web);
                return;
            case 3:
                holder.getImvThumb().setImageResource(R.drawable.ic_google_web);
                return;
            case 4:
                holder.getImvThumb().setImageResource(R.drawable.ic_twitter_web);
                return;
            case 5:
                holder.getImvThumb().setImageResource(R.drawable.ic_instagram);
                return;
            case 6:
                holder.getImvThumb().setImageResource(R.drawable.ic_imdb_web);
                return;
            case 7:
                holder.getImvThumb().setImageResource(R.drawable.ic_twitch_web);
                return;
            case 8:
                holder.getImvThumb().setImageResource(R.drawable.ic_fox_web);
                return;
            case 9:
                holder.getImvThumb().setImageResource(R.drawable.ic_live_vimeo_web);
                return;
            case 10:
                holder.getImvThumb().setImageResource(R.drawable.ic_espn_web);
                return;
            case 11:
                holder.getImvThumb().setImageResource(R.drawable.ic_dailymotion);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onBindViewHolder$lambda-0  reason: not valid java name */
    public static final void m2253onBindViewHolder$lambda0(ContentHistoryAdapter this$0, int i, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        IItemClickHistory iItemClickHistory = this$0.mListener;
        if (iItemClickHistory != null) {
            iItemClickHistory.clickDelete(this$0.historyList.get(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onBindViewHolder$lambda-1  reason: not valid java name */
    public static final void m2254onBindViewHolder$lambda1(ContentHistoryAdapter this$0, int i, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        IItemClickHistory iItemClickHistory = this$0.mListener;
        if (iItemClickHistory != null) {
            iItemClickHistory.clickItem(this$0.historyList.get(i));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.historyList.size();
    }
}
