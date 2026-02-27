package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

public class SubSaleDialog extends BaseDialog {
    private Context context;
    private ConstraintLayout ctPremiumV2ActContinue;
    private ImageView img_close;
    private SubSaleListener mListener;

    public interface SubSaleListener {
        void closeDialog();

        void onContinue();
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_sub_sale;
    }

    public SubSaleDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    public void setClickSale(SubSaleListener subSaleListener) {
        this.mListener = subSaleListener;
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void initView() {
        this.ctPremiumV2ActContinue = (ConstraintLayout) findViewById(R.id.ctPremiumV2ActContinue);
        ImageView imageView = (ImageView) findViewById(R.id.img_close);
        this.img_close = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.SubSaleDialog.AnonymousClass1 */

            public void onClick(View view) {
                SubSaleDialog.this.dismiss();
                if (SubSaleDialog.this.mListener != null) {
                    SubSaleDialog.this.mListener.closeDialog();
                }
            }
        });
        this.ctPremiumV2ActContinue.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.SubSaleDialog.AnonymousClass2 */

            public void onClick(View view) {
                if (SubSaleDialog.this.mListener != null) {
                    SubSaleDialog.this.mListener.onContinue();
                }
            }
        });
    }
}
