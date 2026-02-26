package com.cast.tv.screen.mirroring.screencasting.Base;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import com.cast.tv.screen.mirroring.screencasting.R;


public abstract class BaseDialogFragment extends DialogFragment implements View.OnClickListener {
    protected Context mContext;
    private IDismissCallback mCallback;
    private View mRootView;

    protected void initListener(View view) {
    }

    protected abstract void initView(View view);

    protected void onClick(int i) {
    }

    protected abstract int setDialogGravity();

    protected abstract int setDialogHeight();

    protected abstract int setDialogWidth();

    protected abstract int setLayoutId();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(setLayoutId(), viewGroup, false);
        this.mRootView = inflate;
        inflate.setOnClickListener(null);
        return this.mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView(this.mRootView);
        initListener(this.mRootView);
    }

    @Override
    public void onStart() {
        Window window;
        if (getDialog() != null && (window = getDialog().getWindow()) != null) {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = setDialogWidth();
            if (setDialogHeight() != 0) {
                attributes.height = setDialogHeight();
            }
            attributes.gravity = setDialogGravity();
            window.setAttributes(attributes);
        }
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        onClick(view.getId());
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        IDismissCallback iDismissCallback = this.mCallback;
        if (iDismissCallback != null) {
            iDismissCallback.onDismiss();
        }
    }

    public void setDismissCallback(IDismissCallback iDismissCallback) {
        this.mCallback = iDismissCallback;
    }

    public interface IDismissCallback {
        void onDismiss();
    }
}
