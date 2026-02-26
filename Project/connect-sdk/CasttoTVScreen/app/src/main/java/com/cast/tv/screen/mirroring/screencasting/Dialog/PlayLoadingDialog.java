package com.cast.tv.screen.mirroring.screencasting.Dialog;

import android.view.View;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseDialogFragment;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Utils.ScreenUtil;

public class PlayLoadingDialog extends BaseDialogFragment {
    public static PlayLoadingDialog newInstance() {
        return new PlayLoadingDialog();
    }

    @Override
    protected void initView(View view) {
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
        return R.layout.dialog_play_loading;
    }

    @Override
    protected int setDialogWidth() {
        return ScreenUtil.getScreenWidth(this.mContext) - ScreenUtil.dip2px(this.mContext, 64.0f);
    }
}
