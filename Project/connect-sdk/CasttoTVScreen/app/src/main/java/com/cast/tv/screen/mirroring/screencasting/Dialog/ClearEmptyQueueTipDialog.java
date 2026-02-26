package com.cast.tv.screen.mirroring.screencasting.Dialog;

import android.view.View;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseDialogFragment;
import com.cast.tv.screen.mirroring.screencasting.Callback.EmptyQueueCallback;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Utils.ScreenUtil;

public class ClearEmptyQueueTipDialog extends BaseDialogFragment {
    private EmptyQueueCallback mCallback;

    public static ClearEmptyQueueTipDialog newInstance() {
        return new ClearEmptyQueueTipDialog();
    }

    @Override
    protected int setDialogGravity() {
        return 17;
    }

    @Override
    protected int setDialogHeight() {
        return 0;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_clear_empty_queue_tip;
    }

    @Override
    protected int setDialogWidth() {
        return ScreenUtil.getScreenWidth(this.mContext) - ScreenUtil.dip2px(this.mContext, 150.0f);
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                ClearEmptyQueueTipDialog.this.initViewClearEmptyTipDialog(view2);
            }
        });
        view.findViewById(R.id.text_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                ClearEmptyQueueTipDialog.this.initViewClearEmptyQueueTipDialog(view2);
            }
        });
    }

    public void initViewClearEmptyTipDialog(View view) {
        dismiss();
    }

    public void initViewClearEmptyQueueTipDialog(View view) {
        EmptyQueueCallback emptyQueueCallback = this.mCallback;
        if (emptyQueueCallback != null) {
            emptyQueueCallback.clearQueue();
        }
        dismiss();
    }

    public void setCallback(EmptyQueueCallback emptyQueueCallback) {
        this.mCallback = emptyQueueCallback;
    }
}
