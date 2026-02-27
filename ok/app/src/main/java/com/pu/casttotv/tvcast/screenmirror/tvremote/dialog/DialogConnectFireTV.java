package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

public class DialogConnectFireTV extends BaseDialog {
    private Context context;
    private String ipAddress;
    private TextView tv_cancel;
    private TextView tv_connectFireTV;

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_connect_fire_tv;
    }

    public DialogConnectFireTV(Context context2, String str) {
        super(context2);
        this.context = context2;
        this.ipAddress = str;
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void initView() {
        this.tv_connectFireTV = (TextView) findViewById(R.id.tv_connectFireTV);
        this.tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        TextView textView = this.tv_connectFireTV;
        textView.setText(this.context.getString(R.string.connecting_to) + " " + this.ipAddress);
        this.tv_cancel.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.DialogConnectFireTV.AnonymousClass1 */

            public void onClick(View view) {
                DialogConnectFireTV.this.dismiss();
            }
        });
    }
}
