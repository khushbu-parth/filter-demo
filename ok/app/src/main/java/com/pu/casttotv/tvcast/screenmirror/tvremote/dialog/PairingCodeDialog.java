package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.NumericKeyBoardTransformationMethod;

public class PairingCodeDialog extends BaseDialog {
    private Activity activity;
    private DialogListener dialogListener;
    private EditText edt_kbFireTVInput;
    private TextView tv_pinCodeCancel;
    private TextView tv_pinCodeOk;

    public interface DialogListener {
        void onCancel();

        void onOk(String str);
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_pin_code;
    }

    public PairingCodeDialog(Activity activity2, DialogListener dialogListener2) {
        super(activity2);
        this.activity = activity2;
        this.dialogListener = dialogListener2;
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
            /* class com.magicapps.casttotv.tv.dialog.PairingCodeDialog.AnonymousClass1 */

            public void onClick(View view) {
                PairingCodeDialog.this.dialogListener.onOk(PairingCodeDialog.this.edt_kbFireTVInput.getText().toString().trim());
                PairingCodeDialog.this.dismiss();
            }
        });
        this.tv_pinCodeCancel.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.PairingCodeDialog.AnonymousClass2 */

            public void onClick(View view) {
                PairingCodeDialog.this.dismiss();
                PairingCodeDialog.this.dialogListener.onCancel();
            }
        });
        this.edt_kbFireTVInput.setInputType(18);
        this.edt_kbFireTVInput.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        this.edt_kbFireTVInput.addTextChangedListener(new TextWatcher() {
            /* class com.magicapps.casttotv.tv.dialog.PairingCodeDialog.AnonymousClass3 */

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (PairingCodeDialog.this.edt_kbFireTVInput.getText().toString().isEmpty()) {
                    PairingCodeDialog.this.tv_pinCodeOk.setTextColor(PairingCodeDialog.this.context.getResources().getColor(R.color.color_gray9));
                } else {
                    PairingCodeDialog.this.tv_pinCodeOk.setTextColor(PairingCodeDialog.this.context.getResources().getColor(R.color.color_main));
                }
            }
        });
        showKeyboard(this.edt_kbFireTVInput);
    }

    public static void showKeyboard(final EditText editText) {
        editText.post(new Runnable() {
            /* class com.magicapps.casttotv.tv.dialog.PairingCodeDialog.AnonymousClass4 */

            public void run() {
                editText.requestFocus();
                ((InputMethodManager) editText.getContext().getSystemService("input_method")).showSoftInput(editText, 1);
            }
        });
    }
}
