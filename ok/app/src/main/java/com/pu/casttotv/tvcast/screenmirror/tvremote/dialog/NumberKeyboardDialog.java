package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

public class NumberKeyboardDialog extends BaseDialog {
    private Context context;
    private NumberKeyboardListener numberKeyboardListener;
    private RelativeLayout rlt_eight;
    private RelativeLayout rlt_five;
    private RelativeLayout rlt_four;
    private RelativeLayout rlt_nine;
    private RelativeLayout rlt_one;
    private RelativeLayout rlt_seven;
    private RelativeLayout rlt_six;
    private RelativeLayout rlt_three;
    private RelativeLayout rlt_two;
    private RelativeLayout rlt_zero;

    public interface NumberKeyboardListener {
        void onNumberClick(int i);
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.dialog_number_keyboard;
    }

    public NumberKeyboardDialog(Context context2, NumberKeyboardListener numberKeyboardListener2) {
        super(context2);
        this.context = context2;
        this.numberKeyboardListener = numberKeyboardListener2;
    }

    /* access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void initView() {
        this.rlt_zero = (RelativeLayout) findViewById(R.id.rlt_zero);
        this.rlt_one = (RelativeLayout) findViewById(R.id.rlt_one);
        this.rlt_two = (RelativeLayout) findViewById(R.id.rlt_two);
        this.rlt_three = (RelativeLayout) findViewById(R.id.rlt_three);
        this.rlt_four = (RelativeLayout) findViewById(R.id.rlt_four);
        this.rlt_five = (RelativeLayout) findViewById(R.id.rlt_five);
        this.rlt_six = (RelativeLayout) findViewById(R.id.rlt_six);
        this.rlt_seven = (RelativeLayout) findViewById(R.id.rlt_seven);
        this.rlt_eight = (RelativeLayout) findViewById(R.id.rlt_eight);
        this.rlt_nine = (RelativeLayout) findViewById(R.id.rlt_nine);
        this.rlt_zero.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.NumberKeyboardDialog.AnonymousClass1 */

            public void onClick(View view) {
                if (NumberKeyboardDialog.this.numberKeyboardListener != null) {
                    NumberKeyboardDialog.this.numberKeyboardListener.onNumberClick(0);
                }
            }
        });
        this.rlt_one.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.NumberKeyboardDialog.AnonymousClass2 */

            public void onClick(View view) {
                if (NumberKeyboardDialog.this.numberKeyboardListener != null) {
                    NumberKeyboardDialog.this.numberKeyboardListener.onNumberClick(1);
                }
            }
        });
        this.rlt_two.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.NumberKeyboardDialog.AnonymousClass3 */

            public void onClick(View view) {
                if (NumberKeyboardDialog.this.numberKeyboardListener != null) {
                    NumberKeyboardDialog.this.numberKeyboardListener.onNumberClick(2);
                }
            }
        });
        this.rlt_three.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.NumberKeyboardDialog.AnonymousClass4 */

            public void onClick(View view) {
                if (NumberKeyboardDialog.this.numberKeyboardListener != null) {
                    NumberKeyboardDialog.this.numberKeyboardListener.onNumberClick(3);
                }
            }
        });
        this.rlt_four.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.NumberKeyboardDialog.AnonymousClass5 */

            public void onClick(View view) {
                if (NumberKeyboardDialog.this.numberKeyboardListener != null) {
                    NumberKeyboardDialog.this.numberKeyboardListener.onNumberClick(4);
                }
            }
        });
        this.rlt_five.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.NumberKeyboardDialog.AnonymousClass6 */

            public void onClick(View view) {
                if (NumberKeyboardDialog.this.numberKeyboardListener != null) {
                    NumberKeyboardDialog.this.numberKeyboardListener.onNumberClick(5);
                }
            }
        });
        this.rlt_six.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.NumberKeyboardDialog.AnonymousClass7 */

            public void onClick(View view) {
                if (NumberKeyboardDialog.this.numberKeyboardListener != null) {
                    NumberKeyboardDialog.this.numberKeyboardListener.onNumberClick(6);
                }
            }
        });
        this.rlt_seven.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.NumberKeyboardDialog.AnonymousClass8 */

            public void onClick(View view) {
                if (NumberKeyboardDialog.this.numberKeyboardListener != null) {
                    NumberKeyboardDialog.this.numberKeyboardListener.onNumberClick(7);
                }
            }
        });
        this.rlt_eight.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.NumberKeyboardDialog.AnonymousClass9 */

            public void onClick(View view) {
                if (NumberKeyboardDialog.this.numberKeyboardListener != null) {
                    NumberKeyboardDialog.this.numberKeyboardListener.onNumberClick(8);
                }
            }
        });
        this.rlt_nine.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.NumberKeyboardDialog.AnonymousClass10 */

            public void onClick(View view) {
                if (NumberKeyboardDialog.this.numberKeyboardListener != null) {
                    NumberKeyboardDialog.this.numberKeyboardListener.onNumberClick(9);
                }
            }
        });
    }
}
