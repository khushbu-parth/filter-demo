package com.cast.tv.screen.mirroring.screencasting.UI.supported;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseActivity;
import com.cast.tv.screen.mirroring.screencasting.Model.SupportedDeviceModel;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.SupportedDeviceAdapter;

public class SupportedDeviceActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_supported_device;
    }

    @Override
    protected void init() {
        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                SupportedDeviceActivity.this.init$0$SupportedDeviceActivity(view);
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        SupportedDeviceAdapter supportedDeviceAdapter = new SupportedDeviceAdapter(SupportedDeviceModel.obtain());
        recyclerView.setAdapter(supportedDeviceAdapter);
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.item_supported_foot, (ViewGroup) null);
        supportedDeviceAdapter.addFooterView(inflate);
        inflate.findViewById(R.id.view_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                SupportedDeviceActivity.this.init$1$SupportedDeviceActivity(view);
            }
        });
    }

    public void init$0$SupportedDeviceActivity(View view) {
        finish();
    }

    public void init$1$SupportedDeviceActivity(View view) {
//        ShareUtil.sendEmailFeedback(this.mContext, "");
    }
}
