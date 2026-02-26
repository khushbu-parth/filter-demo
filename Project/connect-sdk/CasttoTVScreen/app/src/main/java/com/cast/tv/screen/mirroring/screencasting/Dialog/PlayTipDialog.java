package com.cast.tv.screen.mirroring.screencasting.Dialog;

import android.content.DialogInterface;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseDialogFragment;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Utils.ScreenUtil;

public class PlayTipDialog extends BaseDialogFragment {
    private boolean isShowing = false;

    public static PlayTipDialog newInstance() {
        return new PlayTipDialog();
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
        return R.layout.dialog_play_tip;
    }

    @Override
    protected int setDialogWidth() {
        return ScreenUtil.getScreenWidth(this.mContext) - ScreenUtil.dip2px(this.mContext, 90.0f);
    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.text_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                PlayTipDialog.this.initListenerPlayTipDialog(view2);
            }
        });
        view.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                PlayTipDialog.this.initListenerPlayTipDialog1(view2);
            }
        });
    }

    public void initListenerPlayTipDialog(View view) {
        dismiss();
    }

    public void initListenerPlayTipDialog1(View view) {
        PlayWellHelpDialog newInstance = PlayWellHelpDialog.newInstance();
        newInstance.show(getChildFragmentManager(), "");
        newInstance.setDismissCallback(new IDismissCallback() {
            @Override
            public final void onDismiss() {
                PlayTipDialog.this.dismiss();
            }
        });
    }

    @Override
    public int show(FragmentTransaction fragmentTransaction, String str) {
        this.isShowing = true;
        return super.show(fragmentTransaction, str);
    }

    @Override
    public void show(FragmentManager fragmentManager, String str) {
        this.isShowing = true;
        super.show(fragmentManager, str);
    }

    public boolean isShowing() {
        return this.isShowing;
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        this.isShowing = false;
    }
}
