package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

public class DialogExitActivity extends BaseDialog {
    private Button btnCancel;
    private Button btnExit;
    private Activity context;
    private IIExitMoreApp mListener;
    private String title;
    private TextView tvTitle;

    public interface IIExitMoreApp {
        void clickExitApp();

        void clickSubmit();
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_exit_activity;
    }

    public void setListener(IIExitMoreApp iIExitMoreApp) {
        this.mListener = iIExitMoreApp;
    }

    public DialogExitActivity(Activity activity) {
        super(activity);
        this.context = activity;
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
        this.btnCancel = (Button) findViewById(R.id.btnCancel);
        this.btnExit = (Button) findViewById(R.id.btnExit);
        this.tvTitle = (TextView) findViewById(R.id.tvTitle);
        String str = this.title;
        if (str != null && !str.isEmpty()) {
            this.tvTitle.setText(this.title);
        }
        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.DialogExitActivity.AnonymousClass1 */

            public void onClick(View view) {
                DialogExitActivity.this.dismiss();
                if (DialogExitActivity.this.mListener != null) {
                    DialogExitActivity.this.mListener.clickExitApp();
                }
            }
        });
        this.btnExit.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.DialogExitActivity.AnonymousClass2 */

            public void onClick(View view) {
                DialogExitActivity.this.dismiss();
                if (DialogExitActivity.this.mListener != null) {
                    DialogExitActivity.this.mListener.clickSubmit();
                }
            }
        });
    }
}
