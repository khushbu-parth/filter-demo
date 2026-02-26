package com.cast.tv.screen.mirroring.screencasting.Dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Utils.ScreenUtil;

public class NormalTipDialog extends DialogFragment {
    private final String cancelBtn;
    private final String contentText;
    private final String continueBtn;
    private final View.OnClickListener mCancelListener;
    private final View.OnClickListener mContinueListener;
    private Context mContext;

    private NormalTipDialog(View.OnClickListener onClickListener, View.OnClickListener onClickListener2, String... strArr) {
        this.mCancelListener = onClickListener;
        this.mContinueListener = onClickListener2;
        this.contentText = strArr[0];
        this.cancelBtn = strArr[1];
        this.continueBtn = strArr[2];
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.dialog_normal_tip, viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((TextView) view.findViewById(R.id.text_content)).setText(this.contentText);
        TextView textView = (TextView) view.findViewById(R.id.text_cancel);
        textView.setText(this.cancelBtn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                NormalTipDialog.this.onViewCreatedNormalTipDialog(view2);
            }
        });
        TextView textView2 = (TextView) view.findViewById(R.id.text_confirm);
        textView2.setText(this.continueBtn);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                NormalTipDialog.this.$onViewCreated$1$NormalTipDialog(view2);
            }
        });
    }

    public void onViewCreatedNormalTipDialog(View view) {
        View.OnClickListener onClickListener = this.mCancelListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
        dismiss();
    }

    public void $onViewCreated$1$NormalTipDialog(View view) {
        View.OnClickListener onClickListener = this.mContinueListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
        dismiss();
    }

    @Override
    public void onStart() {
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window == null) {
                return;
            }
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = ScreenUtil.getScreenWidth(this.mContext) - ScreenUtil.dip2px(this.mContext, 32.0f);
            attributes.gravity = Gravity.CENTER;
            window.setAttributes(attributes);
        }
        super.onStart();
    }

    public static class Builder {
        private String cancelBtn;
        private String contentText;
        private String continueBtn;
        private View.OnClickListener lListener;
        private View.OnClickListener rListener;

        public Builder setContent(String str) {
            if (str == null || str.isEmpty()) {
                str = "Do you sure you want to continue?";
            }
            this.contentText = str;
            return this;
        }

        public Builder setCancel(String str) {
            setCancel(str, null);
            return this;
        }

        public Builder setCancel(String str, View.OnClickListener onClickListener) {
            if (str == null || str.isEmpty()) {
                str = "Cancel";
            }
            this.cancelBtn = str;
            this.lListener = onClickListener;
            return this;
        }

        public Builder setContinue(String str) {
            setContinue(str, null);
            return this;
        }

        public Builder setContinue(String str, View.OnClickListener onClickListener) {
            if (str == null || str.isEmpty()) {
                str = "Continue";
            }
            this.continueBtn = str;
            this.rListener = onClickListener;
            return this;
        }

        public NormalTipDialog build() {
            return new NormalTipDialog(this.lListener, this.rListener, new String[]{this.contentText, this.cancelBtn, this.continueBtn});
        }
    }
}
