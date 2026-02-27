package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.DialogAllowDebug;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;

/* loaded from: classes4.dex */
public class GuideFireTVActivity extends BaseActivity {
    private ImageView imv_close;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // com.magicapps.casttotv.tv.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_guide_fire_tv);
        initView();
    }

    private void initView() {
        Window window = getWindow();
        window.clearFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_main));
        if (!((Boolean) SharedPrefsUtil.getInstance().get("KEY_SHOW_DIALOG", Boolean.class)).booleanValue()) {
            DialogAllowDebug dialogAllowDebug = new DialogAllowDebug(this);
            if (!dialogAllowDebug.isShowing()) {
                dialogAllowDebug.show();
            }
        }
        ImageView imageView = (ImageView) findViewById(R.id.imv_close);
        this.imv_close = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.GuideFireTVActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GuideFireTVActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
