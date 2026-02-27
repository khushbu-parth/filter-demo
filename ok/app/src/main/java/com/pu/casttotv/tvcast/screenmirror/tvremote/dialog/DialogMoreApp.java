package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

public class DialogMoreApp extends BaseDialog {
    private Button btn_continue;
    private Button btn_later;
    private Activity context;
    private ImageView imv_cover;
    private IIExitMoreApp mListener;

    public interface IIExitMoreApp {
        void clickExitApp();

        void clickSubmit();
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_more_app;
    }

    public void setListener(IIExitMoreApp iIExitMoreApp) {
        this.mListener = iIExitMoreApp;
    }

    public DialogMoreApp(Activity activity) {
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
        this.btn_later = (Button) findViewById(R.id.btn_later);
        this.btn_continue = (Button) findViewById(R.id.btn_continue);
        this.imv_cover = (ImageView) findViewById(R.id.imv_cover);
        Glide.with(this.context).load("https://lh3.googleusercontent.com/deQQqyUOaJ2gW5C3xLnRMTrRYyPE1X4DAxi_QYXgVO1nlK4yJYqitgxaTTR3HeR63g=h310").into(this.imv_cover);
        this.btn_later.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.DialogMoreApp.AnonymousClass1 */

            public void onClick(View view) {
                DialogMoreApp.this.dismiss();
                if (DialogMoreApp.this.mListener != null) {
                    DialogMoreApp.this.mListener.clickExitApp();
                }
            }
        });
        this.btn_continue.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.DialogMoreApp.AnonymousClass2 */

            public void onClick(View view) {
                DialogMoreApp.this.dismiss();
                if (DialogMoreApp.this.mListener != null) {
                    DialogMoreApp.this.mListener.clickSubmit();
                }
            }
        });
    }
}
