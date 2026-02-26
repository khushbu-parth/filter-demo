package com.cast.tv.screen.mirroring.screencasting.UI.help;

import android.view.View;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseActivity;
import com.cast.tv.screen.mirroring.screencasting.R;

public class ConnectHelpActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_connect_help;
    }

    @Override
    protected void init() {
        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                ConnectHelpActivity.this.initConnectHelpActivity(view);
            }
        });
    }

    public void initConnectHelpActivity(View view) {
        finish();
    }
}
