package com.colorcallscreen.colorphone.callscreen.calltheme.windowview;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;


public class DialerView extends WindowViewer implements View.OnClickListener {
    private ImageView backspace;
    private View bottomView;
    private FrameLayout callBtn;
    private TextView editText;
    private LinearLayout eight;
    private LinearLayout five;
    private LinearLayout four;
    public boolean fromCall;
    private LinearLayout hash;
    private ImageView hideBtn;
    private boolean isDialerVisible;
    private OnKeypadClickListener keypadClickListener;
    String n;
    private LinearLayout nine;
    private LinearLayout one;
    private LinearLayout seven;
    private LinearLayout six;
    private LinearLayout star;
    private LinearLayout three;
    private LinearLayout two;
    private LinearLayout zero;


    public interface OnKeypadClickListener {
        void onKeypadClick(Character ch);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    protected int animationStyle() {
        return R.style.SlideAnimation;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    protected int getWindowGravity() {
        return 80;
    }

    public DialerView(Context context, boolean z) {
        super(context);
        this.n = "";
        this.isDialerVisible = false;
        this.fromCall = z;
    }

    public void backspace() {
        if (this.n.length() == 0) {
            return;
        }
        String str = this.n;
        String substring = str.substring(0, str.length() - 1);
        this.n = substring;
        this.editText.setText(substring);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    public void findViews(View view) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.one);
        this.one = linearLayout;
        linearLayout.setOnClickListener(this);
        LinearLayout linearLayout2 = (LinearLayout) view.findViewById(R.id.two);
        this.two = linearLayout2;
        linearLayout2.setOnClickListener(this);
        LinearLayout linearLayout3 = (LinearLayout) view.findViewById(R.id.three);
        this.three = linearLayout3;
        linearLayout3.setOnClickListener(this);
        LinearLayout linearLayout4 = (LinearLayout) view.findViewById(R.id.four);
        this.four = linearLayout4;
        linearLayout4.setOnClickListener(this);
        LinearLayout linearLayout5 = (LinearLayout) view.findViewById(R.id.five);
        this.five = linearLayout5;
        linearLayout5.setOnClickListener(this);
        LinearLayout linearLayout6 = (LinearLayout) view.findViewById(R.id.six);
        this.six = linearLayout6;
        linearLayout6.setOnClickListener(this);
        LinearLayout linearLayout7 = (LinearLayout) view.findViewById(R.id.seven);
        this.seven = linearLayout7;
        linearLayout7.setOnClickListener(this);
        LinearLayout linearLayout8 = (LinearLayout) view.findViewById(R.id.eight);
        this.eight = linearLayout8;
        linearLayout8.setOnClickListener(this);
        LinearLayout linearLayout9 = (LinearLayout) view.findViewById(R.id.nine);
        this.nine = linearLayout9;
        linearLayout9.setOnClickListener(this);
        LinearLayout linearLayout10 = (LinearLayout) view.findViewById(R.id.zero);
        this.zero = linearLayout10;
        linearLayout10.setOnClickListener(this);
        LinearLayout linearLayout11 = (LinearLayout) view.findViewById(R.id.star);
        this.star = linearLayout11;
        linearLayout11.setOnClickListener(this);
        LinearLayout linearLayout12 = (LinearLayout) view.findViewById(R.id.hash);
        this.hash = linearLayout12;
        linearLayout12.setOnClickListener(this);
        this.bottomView = view.findViewById(R.id.bottomPaddingView);
        this.hideBtn = (ImageView) view.findViewById(R.id.hide_keypad);
        TextView textView = (TextView) view.findViewById(R.id.number_box);
        this.editText = textView;
        textView.setMovementMethod(new ScrollingMovementMethod());
        this.backspace = (ImageView) view.findViewById(R.id.backspace);
        this.callBtn = (FrameLayout) view.findViewById(R.id.callbtn);
        if (!ViewConfiguration.get(BoloApplication.getApplication()).hasPermanentMenuKey()) {
            this.bottomView.setVisibility(0);
        } else {
            this.bottomView.setVisibility(8);
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    public void finishWindow() {
        super.finishWindow();
        this.isDialerVisible = false;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    public View initView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.keypad_dial2, (ViewGroup) null, false);
    }

    public boolean isDialerVisible() {
        return this.isDialerVisible;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override 
    public void onClick(View view) {
        char c;
        int id = view.getId();
        if (id == R.id.eight) { /* 2131362081 */
            String str = this.editText.getText().toString() + "8";
            this.n = str;
            this.editText.setText(str);
            c = '8';
        } else if (id == R.id.five) { /* 2131362114 */
            String str2 = this.editText.getText().toString() + "5";
            this.n = str2;
            this.editText.setText(str2);
            c = '5';
        } else if (id == R.id.four) { /* 2131362124 */
            String str3 = this.editText.getText().toString() + "4";
            this.n = str3;
            this.editText.setText(str3);
            c = '4';
        } else if (id == R.id.hash) { /* 2131362150 */
            String str4 = this.editText.getText().toString() + "#";
            this.n = str4;
            this.editText.setText(str4);
            c = '#';
        } else if (id == R.id.nine) { /* 2131362342 */
            String str5 = this.editText.getText().toString() + "9";
            this.n = str5;
            this.editText.setText(str5);
            c = '9';
        } else if (id == R.id.one) { /* 2131362363 */
            String str6 = this.editText.getText().toString() + "1";
            this.n = str6;
            this.editText.setText(str6);
            c = '1';
        } else if (id == R.id.seven) { /* 2131362492 */
            String str7 = this.editText.getText().toString() + "7";
            this.n = str7;
            this.editText.setText(str7);
            c = '7';
        } else if (id == R.id.six) { /* 2131362508 */
            String str8 = this.editText.getText().toString() + "6";
            this.n = str8;
            this.editText.setText(str8);
            c = '6';
        } else if (id == R.id.star) { /* 2131362539 */
            String str9 = this.editText.getText().toString() + "*";
            this.n = str9;
            this.editText.setText(str9);
            c = '*';
        } else if (id == R.id.three) { /* 2131362596 */
            String str10 = this.editText.getText().toString() + "3";
            this.n = str10;
            this.editText.setText(str10);
            c = '3';
        } else if (id == R.id.two) { /* 2131362631 */
            String str11 = this.editText.getText().toString() + "2";
            this.n = str11;
            this.editText.setText(str11);
            c = '2';
        } else if (id == R.id.zero) { /* 2131362695 */
            String str12 = this.editText.getText().toString() + "0";
            this.n = str12;
            this.editText.setText(str12);
            c = '0';
        } else {
            c = '1';
        }
        Utility.resizeText(this.editText, 30, 16);
        OnKeypadClickListener onKeypadClickListener = this.keypadClickListener;
        if (onKeypadClickListener != null) {
            onKeypadClickListener.onKeypadClick(Character.valueOf(c));
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    public void onWindowCreated() {
        this.isDialerVisible = true;
        if (this.fromCall) {
            this.callBtn.setVisibility(8);
        } else {
            this.callBtn.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.DialerView.1
                @Override 
                public void onClick(View view) {
                    DialerView.this.n.isEmpty();
                }
            });
        }
        this.backspace.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.DialerView.2
            @Override 
            public void onClick(View view) {
                DialerView.this.backspace();
            }
        });
        this.backspace.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.DialerView.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                DialerView.this.editText.setText("");
                return true;
            }
        });
        this.hideBtn.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.DialerView.4
            @Override 
            public void onClick(View view) {
                DialerView.this.finishWindow();
                DialerView.this.isDialerVisible = false;
            }
        });
    }

    public void setKeypadClickListener(OnKeypadClickListener onKeypadClickListener) {
        this.keypadClickListener = onKeypadClickListener;
    }
}
