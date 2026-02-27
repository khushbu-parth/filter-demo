package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

public abstract class BaseDialog extends Dialog {
    public Context context;

    /* access modifiers changed from: protected */
    public abstract int getLayoutId();

    /* access modifiers changed from: protected */
    public abstract void initView();

    public BaseDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setLayout(-1, -2);
        requestWindowFeature(1);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setContentView(getLayoutId());
        initView();
    }
}
