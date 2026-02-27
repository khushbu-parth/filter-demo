package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

public class PairingAlertDialog extends Dialog {
    private Button btn_cancel;
    private Button btn_ok;
    private PairingListener pairingListener;

    public interface PairingListener {
        void onClick();
    }

    public PairingAlertDialog(Context context, PairingListener pairingListener2) {
        super(context);
        this.pairingListener = pairingListener2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_pairing_alert);
        this.btn_cancel = (Button) findViewById(R.id.btn_cancel);
        this.btn_ok = (Button) findViewById(R.id.btn_ok);
        this.btn_cancel.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.PairingAlertDialog.AnonymousClass1 */

            public void onClick(View view) {
                PairingAlertDialog.this.dismiss();
            }
        });
        this.btn_ok.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.PairingAlertDialog.AnonymousClass2 */

            public void onClick(View view) {
                if (PairingAlertDialog.this.pairingListener != null) {
                    PairingAlertDialog.this.pairingListener.onClick();
                }
                PairingAlertDialog.this.dismiss();
            }
        });
    }
}
