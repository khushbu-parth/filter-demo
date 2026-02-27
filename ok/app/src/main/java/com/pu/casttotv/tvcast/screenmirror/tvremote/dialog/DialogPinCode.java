package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.NumericKeyBoardTransformationMethod;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.sony.RemoteSonyManager;

public class DialogPinCode extends BaseDialog {
    private Activity activity;
    private EditText edt_kbFireTVInput;
    private TextView tv_pinCodeCancel;
    private TextView tv_pinCodeOk;

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_pin_code;
    }

    public DialogPinCode(Activity activity2) {
        super(activity2);
        this.activity = activity2;
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void initView() {
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
        getWindow().setLayout(-1, -2);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        getWindow().setAttributes(layoutParams);
        this.tv_pinCodeOk = (TextView) findViewById(R.id.tv_pinCodeOk);
        this.edt_kbFireTVInput = (EditText) findViewById(R.id.edt_pinCodeInput);
        this.tv_pinCodeCancel = (TextView) findViewById(R.id.tv_pinCodeCancel);
        this.tv_pinCodeOk.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.DialogPinCode.AnonymousClass1 */

            public void onClick(View view) {
                RemoteSonyManager.getInstance().accessControlWithPIN(DialogPinCode.this.activity, DialogPinCode.this.edt_kbFireTVInput.getText().toString(), RemoteSonyManager.TEST_IP, TVConnectUtils.getInstance().getConnectableDevice().getIpAddress(), new RemoteSonyManager.NetworkListener() {
                    /* class com.magicapps.casttotv.tv.dialog.DialogPinCode.AnonymousClass1.AnonymousClass1 */

                    @Override // com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.NetworkListener
                    public void onDevicePincodeGenerated(boolean z) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onDevicePincodeGenerated --- isAuthorized: ");
                        sb.append(z);
                    }

                    @Override // com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.NetworkListener
                    public void onDeviceRegistrationCompleted(boolean z) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onDeviceRegistrationCompleted --- isRegistered: ");
                        sb.append(z);
                        if (z) {
                            DialogPinCode.this.dismiss();
                        }
                    }

                    @Override // com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.NetworkListener
                    public void onFailedToConnect() {
                        Toast.makeText(DialogPinCode.this.activity, DialogPinCode.this.activity.getString(R.string.fail_connect_sony), Toast.LENGTH_SHORT).show();
                        DialogPinCode.this.dismiss();
                    }
                });
            }
        });
        this.tv_pinCodeCancel.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.DialogPinCode.AnonymousClass2 */

            public void onClick(View view) {
                DialogPinCode.this.dismiss();
            }
        });
        this.edt_kbFireTVInput.setInputType(18);
        this.edt_kbFireTVInput.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        this.edt_kbFireTVInput.addTextChangedListener(new TextWatcher() {
            /* class com.magicapps.casttotv.tv.dialog.DialogPinCode.AnonymousClass3 */

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (DialogPinCode.this.edt_kbFireTVInput.getText().toString().isEmpty()) {
                    DialogPinCode.this.tv_pinCodeOk.setTextColor(DialogPinCode.this.context.getResources().getColor(R.color.color_gray_2_ads));
                } else {
                    DialogPinCode.this.tv_pinCodeOk.setTextColor(DialogPinCode.this.context.getResources().getColor(R.color.color_main));
                }
            }
        });
        showKeyboard(this.edt_kbFireTVInput);
    }

    public static void showKeyboard(final EditText editText) {
        editText.post(new Runnable() {
            @SuppressLint("WrongConstant")
            public void run() {
                editText.requestFocus();
                ((InputMethodManager) editText.getContext().getSystemService("input_method")).showSoftInput(editText, 1);
            }
        });
    }
}
