package com.cast.tv.screen.mirroring.screencasting.UI.how2use;

import android.view.View;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseActivity;
import com.cast.tv.screen.mirroring.screencasting.R;

public class How2UseActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_how_to_use;
    }

    @Override
    protected void init() {
        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                How2UseActivity.this.initHow2UseActivity(view);
            }
        });
    }

    public void initHow2UseActivity(View view) {
        finish();
    }
}
