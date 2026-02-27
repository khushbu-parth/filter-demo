package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.connectsdk.core.ExternalInputInfo;
import com.connectsdk.service.capability.ExternalInputControl;
import com.connectsdk.service.capability.TextInputControl;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;

public class KeyboardDialog extends BaseDialog {
    private Button btn_close;
    private Button btn_send;
    private Context context;
    private EditText edt_text_input;
    private String mOldText = "";
    private String newText = "";

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendBackspace() {
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_keyboard;
    }

    public KeyboardDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void initView() {
        this.edt_text_input = (EditText) findViewById(R.id.edt_text_input);
        this.btn_close = (Button) findViewById(R.id.btn_close);
        this.btn_send = (Button) findViewById(R.id.btn_send);
        this.btn_close.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.KeyboardDialog.AnonymousClass1 */

            public void onClick(View view) {
                KeyboardDialog.this.dismiss();
            }
        });
        this.btn_send.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.KeyboardDialog.AnonymousClass2 */

            public void onClick(View view) {
                try {
                    KeyboardDialog keyboardDialog = KeyboardDialog.this;
                    keyboardDialog.newText = keyboardDialog.edt_text_input.getText().toString();
                    if (!KeyboardDialog.this.newText.equalsIgnoreCase("")) {
                        KeyboardDialog keyboardDialog2 = KeyboardDialog.this;
                        keyboardDialog2.sendStringLiteral(keyboardDialog2.newText);
                        return;
                    }
                    Toast.makeText(KeyboardDialog.this.context, (int) R.string.empty_text, 0).show();
                } catch (Exception e2) {
                    try {
                        e2.printStackTrace();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
            }
        });
        this.edt_text_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /* class com.magicapps.casttotv.tv.dialog.KeyboardDialog.AnonymousClass3 */

            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 6 || keyEvent.getKeyCode() != 67 || keyEvent.getAction() != 0) {
                    return false;
                }
                KeyboardDialog.this.sendBackspace();
                return true;
            }
        });
        this.edt_text_input.setOnKeyListener(new View.OnKeyListener() {
            /* class com.magicapps.casttotv.tv.dialog.KeyboardDialog.AnonymousClass4 */

            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i != 67 || keyEvent.getAction() != 0) {
                    return false;
                }
                KeyboardDialog.this.sendBackspace();
                return true;
            }
        });
        this.edt_text_input.addTextChangedListener(new TextWatcher() {
            /* class com.magicapps.casttotv.tv.dialog.KeyboardDialog.AnonymousClass5 */

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String charSequence2 = charSequence.toString();
                int length = charSequence2.length() - KeyboardDialog.this.mOldText.length();
                if (charSequence2.equals("")) {
                    int length2 = KeyboardDialog.this.mOldText.length() - charSequence2.length();
                    for (int i4 = 0; i4 < length2; i4++) {
                        KeyboardDialog.this.sendBackspace();
                    }
                    KeyboardDialog.this.mOldText = charSequence2;
                } else if (length > 1) {
                    charSequence2.replace(KeyboardDialog.this.mOldText, "");
                    KeyboardDialog.this.mOldText = charSequence2;
                    KeyboardDialog.this.sendStringLiteral(charSequence2);
                } else {
                    String str = null;
                    if (charSequence2.length() > 0) {
                        str = charSequence2.substring(charSequence2.length() - 1);
                    }
                    if (KeyboardDialog.this.mOldText.length() > charSequence2.length()) {
                        str = "BACKSPACE";
                    }
                    KeyboardDialog.this.mOldText = charSequence2;
                    if (str != null) {
                        try {
                            if (str.equals("BACKSPACE")) {
                                KeyboardDialog.this.sendBackspace();
                                return;
                            }
                            if (str.equals(" ")) {
                                str = "%20";
                            }
                            KeyboardDialog.this.sendStringLiteral(str);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendStringLiteral(String str) {
        try {
            ((TextInputControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(TextInputControl.class)).sendText(str);
            ExternalInputInfo externalInputInfo = new ExternalInputInfo();
            externalInputInfo.setId(str);
            ((ExternalInputControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(ExternalInputControl.class)).setExternalInput(externalInputInfo, null);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
