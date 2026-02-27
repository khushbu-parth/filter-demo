package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote.RemoteFragment;

public class KeyboardFireTVDialog extends BaseDialog {
    String andperson = "\"&\"";
    private EditText edt_kbFireTVInput;
    String hashtag = "\"#\"";
    private int lastLength;
    int oldLength = 0;
    String parentheseLeft = "\"(\"";
    String parentheseRight = "\")\"";
    String questionmark = "\"?\"";
    String star = "\"*\"";
    private TextView tv_kbFireTVCancel;

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_keyboard_fire_tv;
    }

    public KeyboardFireTVDialog(Context context) {
        super(context);
        this.context = context;
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void initView() {
        this.tv_kbFireTVCancel = (TextView) findViewById(R.id.tv_pinCodeOk);
        this.edt_kbFireTVInput = (EditText) findViewById(R.id.edt_pinCodeInput);
        this.tv_kbFireTVCancel.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.KeyboardFireTVDialog.AnonymousClass1 */

            public void onClick(View view) {
                KeyboardFireTVDialog.this.dismiss();
            }
        });
        showKeyboard(this.edt_kbFireTVInput);
        initSearchFireTV();
        this.edt_kbFireTVInput.setOnKeyListener(new View.OnKeyListener() {
            /* class com.magicapps.casttotv.tv.dialog.KeyboardFireTVDialog.AnonymousClass2 */

            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i != 67 || !KeyboardFireTVDialog.this.edt_kbFireTVInput.getText().toString().isEmpty()) {
                    return true;
                }
                RemoteFragment.FireTVButtonPressDialog("input keyevent 67", KeyboardFireTVDialog.this.context);
                return true;
            }
        });
    }

    private void initSearchFireTV() {
        if (Build.VERSION.SDK_INT < 11) {
            this.edt_kbFireTVInput.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                /* class com.magicapps.casttotv.tv.dialog.KeyboardFireTVDialog.AnonymousClass3 */

                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    contextMenu.clear();
                }
            });
        } else {
            this.edt_kbFireTVInput.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                /* class com.magicapps.casttotv.tv.dialog.KeyboardFireTVDialog.AnonymousClass4 */

                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    return false;
                }

                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                public void onDestroyActionMode(ActionMode actionMode) {
                }

                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }
            });
        }
        this.edt_kbFireTVInput.addTextChangedListener(new TextWatcher() {
            /* class com.magicapps.casttotv.tv.dialog.KeyboardFireTVDialog.AnonymousClass5 */

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                KeyboardFireTVDialog.this.lastLength = charSequence.length();
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                KeyboardFireTVDialog.this.edt_kbFireTVInput.getSelectionStart();
                KeyboardFireTVDialog.this.edt_kbFireTVInput.getSelectionEnd();
                String obj = KeyboardFireTVDialog.this.edt_kbFireTVInput.getText().toString();
                int length = obj.length();
                int i4 = KeyboardFireTVDialog.this.oldLength;
                if (length > i4) {
                    int i5 = length - 1;
                    char charAt = obj.charAt(i5);
                    if (charAt == ' ') {
                        RemoteFragment.FireTVButtonPressDialog("input keyevent 62", KeyboardFireTVDialog.this.context);
                    } else if (charAt == '#') {
                        RemoteFragment.FireTVButtonPressDialog("input text " + KeyboardFireTVDialog.this.hashtag, KeyboardFireTVDialog.this.context);
                    } else if (charAt == '&') {
                        RemoteFragment.FireTVButtonPressDialog("input text " + KeyboardFireTVDialog.this.andperson, KeyboardFireTVDialog.this.context);
                    } else if (charAt != '?') {
                        switch (charAt) {
                            case '(':
                                RemoteFragment.FireTVButtonPressDialog("input text " + KeyboardFireTVDialog.this.parentheseLeft, KeyboardFireTVDialog.this.context);
                                break;
                            case ')':
                                RemoteFragment.FireTVButtonPressDialog("input text " + KeyboardFireTVDialog.this.parentheseRight, KeyboardFireTVDialog.this.context);
                                break;
                            case '*':
                                RemoteFragment.FireTVButtonPressDialog("input text " + KeyboardFireTVDialog.this.star, KeyboardFireTVDialog.this.context);
                                break;
                            default:
                                RemoteFragment.FireTVButtonPressDialog("input text " + Character.valueOf(obj.charAt(i5)), KeyboardFireTVDialog.this.context);
                                break;
                        }
                    } else {
                        RemoteFragment.FireTVButtonPressDialog("input text " + KeyboardFireTVDialog.this.questionmark, KeyboardFireTVDialog.this.context);
                    }
                    KeyboardFireTVDialog.this.oldLength = length;
                } else if (length < i4) {
                    int i6 = i4 - length;
                    for (int i7 = 0; i7 < i6; i7++) {
                        RemoteFragment.FireTVButtonPressDialog("input keyevent 67", KeyboardFireTVDialog.this.context);
                    }
                    KeyboardFireTVDialog.this.oldLength = length;
                }
            }
        });
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
