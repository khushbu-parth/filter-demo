package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;

public class DialogNotSupport extends BaseDialog {
    private static DialogNotSupport dialogNotSupport;
    private boolean isShow;
    TextView title;
    TextView tv_ok;

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_not_support;
    }

    public DialogNotSupport(Context context) {
        super(context);
    }

    public static DialogNotSupport getInstance(Context context) {
        if (dialogNotSupport == null) {
            dialogNotSupport = new DialogNotSupport(context);
        }
        return dialogNotSupport;
    }

    public void setMessage(String str) {
        TextView textView = this.title;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void clear() {
        dialogNotSupport = null;
    }

    public void showInstance() {
        if (!this.isShow) {
            show();
        }
    }

    public void show() {
        this.isShow = true;
        super.show();
    }

    public void dismiss() {
        super.dismiss();
        this.isShow = false;
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void initView() {
        this.title = (TextView) findViewById(R.id.title);
        TextView textView = (TextView) findViewById(R.id.tv_ok);
        this.tv_ok = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.DialogNotSupport.AnonymousClass1 */

            public void onClick(View view) {
                TVConnectUtils.getInstance().disconnect();
                DialogNotSupport.this.dismiss();
            }
        });
    }
}
