package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;

public class DialogAllowDebug extends BaseDialog {
    private CheckBox imv_check;
    private Button tv_ok;

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_allow_debug;
    }

    public DialogAllowDebug(Context context) {
        super(context);
        this.context = context;
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setLayout(-1, -2);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        getWindow().setAttributes(layoutParams);
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void initView() {
        this.imv_check = (CheckBox) findViewById(R.id.imv_check);
        Button button = (Button) findViewById(R.id.tv_ok);
        this.tv_ok = button;
        button.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.DialogAllowDebug.AnonymousClass1 */

            public void onClick(View view) {
                if (DialogAllowDebug.this.imv_check.isChecked()) {
                    SharedPrefsUtil.getInstance().put("KEY_SHOW_DIALOG", Boolean.TRUE);
                }
                DialogAllowDebug.this.dismiss();
            }
        });
    }
}
