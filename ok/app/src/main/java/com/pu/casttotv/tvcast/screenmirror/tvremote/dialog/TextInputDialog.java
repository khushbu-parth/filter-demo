package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.jaku.core.JakuRequest;
import com.jaku.core.KeypressKeyValues;
import com.jaku.request.KeypressRequest;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks.RequestCallback;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks.RequestTask;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.CommandHelper;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.RokuRequestTypes;
import java.util.ArrayDeque;

public class TextInputDialog extends BaseDialog {
    private String mOldText = "";
    private EditText mTextBox;
    private TextView tvClose;

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_fragment_text_input;
    }

    public TextInputDialog(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setLayout(-1, -2);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        getWindow().setAttributes(layoutParams);
        setCancelable(true);
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void initView() {
        this.mTextBox = (EditText) findViewById(R.id.text_box);
        this.tvClose = (TextView) findViewById(R.id.tvClose);
        setupTextBox();
        this.tvClose.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.TextInputDialog.AnonymousClass1 */

            public void onClick(View view) {
                TextInputDialog.this.dismiss();
            }
        });
    }

    public void onStart() {
        super.onStart();
        showSoftKeyboard(this.mTextBox);
    }

    private void showSoftKeyboard(final View view) {
        if (view.requestFocus()) {
            @SuppressLint("WrongConstant") final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
            view.postDelayed(new Runnable() {
                public void run() {
                    view.requestFocus();
                    inputMethodManager.showSoftInput(view, 1);
                }
            }, 100);
        }
    }

    private void setupTextBox() {
        this.mTextBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /* class com.magicapps.casttotv.tv.dialog.TextInputDialog.AnonymousClass3 */

            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 6 || keyEvent.getKeyCode() != 67 || keyEvent.getAction() != 0) {
                    return false;
                }
                TextInputDialog.this.sendBackspace();
                return true;
            }
        });
        this.mTextBox.setOnKeyListener(new View.OnKeyListener() {
            /* class com.magicapps.casttotv.tv.dialog.TextInputDialog.AnonymousClass4 */

            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i != 67 || keyEvent.getAction() != 0) {
                    return false;
                }
                TextInputDialog.this.sendBackspace();
                return true;
            }
        });
        this.mTextBox.addTextChangedListener(new TextWatcher() {
            /* class com.magicapps.casttotv.tv.dialog.TextInputDialog.AnonymousClass5 */

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String charSequence2 = charSequence.toString();
                int length = charSequence2.length() - TextInputDialog.this.mOldText.length();
                if (charSequence2.equals("")) {
                    int length2 = TextInputDialog.this.mOldText.length() - charSequence2.length();
                    for (int i4 = 0; i4 < length2; i4++) {
                        TextInputDialog.this.sendBackspace();
                    }
                    TextInputDialog.this.mOldText = charSequence2;
                } else if (length > 1) {
                    charSequence2.replace(TextInputDialog.this.mOldText, "");
                    TextInputDialog.this.mOldText = charSequence2;
                    TextInputDialog.this.sendStringLiteral(charSequence2);
                } else {
                    String str = null;
                    if (charSequence2.length() > 0) {
                        str = charSequence2.substring(charSequence2.length() - 1);
                    }
                    if (TextInputDialog.this.mOldText.length() > charSequence2.length()) {
                        str = "BACKSPACE";
                    }
                    TextInputDialog.this.mOldText = charSequence2;
                    if (str == null) {
                        return;
                    }
                    if (str.equals("BACKSPACE")) {
                        TextInputDialog.this.sendBackspace();
                        return;
                    }
                    if (str.equals(" ")) {
                        str = "%20";
                    }
                    TextInputDialog.this.sendStringLiteral(str);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendBackspace() {
        new RequestTask(new JakuRequest(new KeypressRequest(CommandHelper.getDeviceURL(this.context), KeypressKeyValues.BACKSPACE.getValue()), null), new RequestCallback() {
            /* class com.magicapps.casttotv.tv.dialog.TextInputDialog.AnonymousClass6 */

            @Override // com.magicapps.casttotv.tv.utils.remote.roku.tasks.RequestCallback
            public void onErrorResponse(RequestTask.Result result) {
            }

            @Override // com.magicapps.casttotv.tv.utils.remote.roku.tasks.RequestCallback
            public void requestResult(RokuRequestTypes rokuRequestTypes, RequestTask.Result result) {
            }
        }).execute(RokuRequestTypes.keypress);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendStringLiteral(String str) {
        String deviceURL = CommandHelper.getDeviceURL(this.context);
        StringBuilder sb = new StringBuilder();
        KeypressKeyValues keypressKeyValues = KeypressKeyValues.LIT_;
        sb.append(keypressKeyValues.getValue());
        sb.append(str);
        JakuRequest jakuRequest = new JakuRequest(new KeypressRequest(deviceURL, sb.toString()), null);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("OK1: ");
        sb2.append(keypressKeyValues.getValue());
        sb2.append(str);
        new RequestTask(jakuRequest, new RequestCallback() {
            /* class com.magicapps.casttotv.tv.dialog.TextInputDialog.AnonymousClass7 */

            @Override // com.magicapps.casttotv.tv.utils.remote.roku.tasks.RequestCallback
            public void onErrorResponse(RequestTask.Result result) {
            }

            @Override // com.magicapps.casttotv.tv.utils.remote.roku.tasks.RequestCallback
            public void requestResult(RokuRequestTypes rokuRequestTypes, RequestTask.Result result) {
            }
        }).execute(RokuRequestTypes.keypress);
        new ArrayDeque();
    }
}
