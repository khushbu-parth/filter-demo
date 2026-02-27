package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.jsibbold.zoomage.ZoomageView;

public class DialogShowImage extends BaseDialog {
    private Context context;
    private ImageView imvExit;
    private ZoomageView imvImage;
    private String linkUrl = "";

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_show_image;
    }

    public DialogShowImage(Context context2) {
        super(context2);
        this.context = context2;
    }

    public void setLinkUrl(String str) {
        this.linkUrl = str;
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setLayout(-1, -1);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        getWindow().setAttributes(layoutParams);
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void initView() {
        this.imvImage = (ZoomageView) findViewById(R.id.myZoomageView);
        this.imvExit = (ImageView) findViewById(R.id.imvExit);
        if (this.linkUrl != null) {
            ((RequestBuilder) Glide.with(this.context).load(this.linkUrl).placeholder(R.drawable.ic_image_default)).into(this.imvImage);
        }
        this.imvExit.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.DialogShowImage.AnonymousClass1 */

            public void onClick(View view) {
                DialogShowImage.this.dismiss();
            }
        });
    }
}
