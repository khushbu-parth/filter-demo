package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.history;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.HistoryBrowser;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.databinding.ActivityHistoryBrowserBinding;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.history.adapter.ListHistoryAdapter;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.history.viewModel.HistoryViewModel;
import com.ironsource.mediationsdk.utils.IronSourceConstants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@SuppressLint("WrongConstant")
public final class HistoryBrowserActivity extends BaseActivity {
    private ActivityHistoryBrowserBinding binding;
    private HistoryViewModel viewModel;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_history_browser);
        ActivityHistoryBrowserBinding inflate = ActivityHistoryBrowserBinding.inflate(getLayoutInflater());
        Intrinsics.checkNotNullExpressionValue(inflate, "inflate(layoutInflater)");
        this.binding = inflate;
        ActivityHistoryBrowserBinding activityHistoryBrowserBinding = null;
        if (inflate == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            inflate = null;
        }
        View root = inflate.getRoot();
        Intrinsics.checkNotNullExpressionValue(root, "binding.root");
        setContentView(root);
        final ListHistoryAdapter listHistoryAdapter = new ListHistoryAdapter();
        listHistoryAdapter.ListHistoryAdapter(this);
        listHistoryAdapter.setListener(new ListHistoryAdapter.IItemClick() {
            @Override
            public void clickItem(@NotNull HistoryBrowser historyBrowser) {
                Intrinsics.checkNotNullParameter(historyBrowser, "historyBrowser");
                Intent intent = new Intent();
                intent.putExtra(IronSourceConstants.EVENTS_RESULT, historyBrowser.getLinkUrl());
                HistoryBrowserActivity.this.setResult(-1, intent);
                HistoryBrowserActivity.this.onFinish();
            }

            @Override
            public void clickRemove(@NotNull HistoryBrowser historyBrowser) {
                HistoryViewModel historyViewModel;
                Intrinsics.checkNotNullParameter(historyBrowser, "historyBrowser");
                historyViewModel = HistoryBrowserActivity.this.viewModel;
                if (historyViewModel == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("viewModel");
                    historyViewModel = null;
                }
                historyViewModel.deleteHistory(historyBrowser);
            }
        });
        View findViewById = findViewById(R.id.main_ads_native);
        ActivityHistoryBrowserBinding activityHistoryBrowserBinding2 = this.binding;
        if (activityHistoryBrowserBinding2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityHistoryBrowserBinding2 = null;
        }
        RecyclerView recyclerView = activityHistoryBrowserBinding2.rcvHistory;
        Intrinsics.checkNotNullExpressionValue(recyclerView, "binding.rcvHistory");
        recyclerView.setAdapter(listHistoryAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(1);
        recyclerView.setLayoutManager(linearLayoutManager);
        HistoryViewModel historyViewModel = (HistoryViewModel) new ViewModelProvider(this).get(HistoryViewModel.class);
        this.viewModel = historyViewModel;
        if (historyViewModel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("viewModel");
            historyViewModel = null;
        }
        historyViewModel.getReadAllData().observe(this, new Observer() { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.history.HistoryBrowserActivity$$ExternalSyntheticLambda2
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                HistoryBrowserActivity.m2248onCreate$lambda0(HistoryBrowserActivity.this, listHistoryAdapter, (List) obj);
            }
        });
        ActivityHistoryBrowserBinding activityHistoryBrowserBinding3 = this.binding;
        if (activityHistoryBrowserBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityHistoryBrowserBinding3 = null;
        }
        activityHistoryBrowserBinding3.llBack.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.history.HistoryBrowserActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HistoryBrowserActivity.m2249onCreate$lambda1(HistoryBrowserActivity.this, view);
            }
        });
        ActivityHistoryBrowserBinding activityHistoryBrowserBinding4 = this.binding;
        if (activityHistoryBrowserBinding4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
        } else {
            activityHistoryBrowserBinding = activityHistoryBrowserBinding4;
        }
        activityHistoryBrowserBinding.llDeleteAll.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.history.HistoryBrowserActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HistoryBrowserActivity.m2250onCreate$lambda2(HistoryBrowserActivity.this, view);
            }
        });
    }

    public static final void m2248onCreate$lambda0(HistoryBrowserActivity this$0, ListHistoryAdapter adapter, List list) {
        List<HistoryBrowser> reversed;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(adapter, "$adapter");
        ActivityHistoryBrowserBinding activityHistoryBrowserBinding = null;
        if (list != null) {
            if (!list.isEmpty()) {
                ActivityHistoryBrowserBinding activityHistoryBrowserBinding2 = this$0.binding;
                if (activityHistoryBrowserBinding2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("binding");
                } else {
                    activityHistoryBrowserBinding = activityHistoryBrowserBinding2;
                }
                activityHistoryBrowserBinding.imvNoFile.setVisibility(8);
            } else {
                ActivityHistoryBrowserBinding activityHistoryBrowserBinding3 = this$0.binding;
                if (activityHistoryBrowserBinding3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("binding");
                } else {
                    activityHistoryBrowserBinding = activityHistoryBrowserBinding3;
                }
                activityHistoryBrowserBinding.imvNoFile.setVisibility(0);
            }
            reversed = CollectionsKt.reversed(list);
            adapter.setData(reversed);
            return;
        }
        ActivityHistoryBrowserBinding activityHistoryBrowserBinding4 = this$0.binding;
        if (activityHistoryBrowserBinding4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
        } else {
            activityHistoryBrowserBinding = activityHistoryBrowserBinding4;
        }
        activityHistoryBrowserBinding.imvNoFile.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onCreate$lambda-1  reason: not valid java name */
    public static final void m2249onCreate$lambda1(HistoryBrowserActivity this$0, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.onFinish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onCreate$lambda-2  reason: not valid java name */
    public static final void m2250onCreate$lambda2(HistoryBrowserActivity this$0, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        HistoryViewModel historyViewModel = this$0.viewModel;
        if (historyViewModel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("viewModel");
            historyViewModel = null;
        }
        historyViewModel.deleteAllHistory();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
