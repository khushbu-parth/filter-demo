package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

public class DialogNoResourcesFound extends Dialog {
    private ImageView imv_close;
    private RefreshListener refreshListener;
    private RelativeLayout rl_refresh;

    public interface RefreshListener {
        void onClick();
    }

    public DialogNoResourcesFound(Context context, RefreshListener refreshListener2) {
        super(context);
        this.refreshListener = refreshListener2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_no_resource_found);
        setCancelable(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
        getWindow().setLayout(-1, -2);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        getWindow().setAttributes(layoutParams);
        this.rl_refresh = (RelativeLayout) findViewById(R.id.rl_refresh);
        ImageView imageView = (ImageView) findViewById(R.id.imv_close);
        this.imv_close = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.DialogNoResourcesFound.AnonymousClass1 */

            public void onClick(View view) {
                DialogNoResourcesFound.this.dismiss();
            }
        });
        this.rl_refresh.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.DialogNoResourcesFound.AnonymousClass2 */

            public void onClick(View view) {
                DialogNoResourcesFound.this.refreshListener.onClick();
                DialogNoResourcesFound.this.dismiss();
            }
        });
    }
}
