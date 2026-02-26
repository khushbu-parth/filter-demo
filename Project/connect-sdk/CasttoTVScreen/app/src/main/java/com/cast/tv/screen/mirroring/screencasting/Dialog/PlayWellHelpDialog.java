package com.cast.tv.screen.mirroring.screencasting.Dialog;

import android.content.Intent;
import android.view.View;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseDialogFragment;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.UI.help.HelpActivity;
import com.cast.tv.screen.mirroring.screencasting.Utils.ScreenUtil;

public class PlayWellHelpDialog extends BaseDialogFragment {
    public static void safedk_PlayWellHelpDialog_startActivity_ff714e12d3fed759d8b8111df131e951(PlayWellHelpDialog p0, Intent p1) {
        if (p1 == null) {
            return;
        }
        p0.startActivity(p1);
    }

    public static PlayWellHelpDialog newInstance() {
        return new PlayWellHelpDialog();
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
        return R.layout.dialog_play_well_help;
    }

    @Override
    protected int setDialogWidth() {
        return ScreenUtil.getScreenWidth(this.mContext) - ScreenUtil.dip2px(this.mContext, 32.0f);
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.text_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                PlayWellHelpDialog.this.initViewPlayWellHelpDialog(view2);
            }
        });
        view.findViewById(R.id.text_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                PlayWellHelpDialog.this.initViewPlayWellHelpDialog1(view2);
            }
        });
        view.findViewById(R.id.text_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                PlayWellHelpDialog.this.initViewPlayWellHelpDialog2(view2);
            }
        });
    }

    public void initViewPlayWellHelpDialog(View view) {
//        ShareUtil.sendEmailFeedback(this.mContext, "");
        dismiss();
    }

    public void initViewPlayWellHelpDialog1(View view) {
        startActivity(new Intent(this.mContext, HelpActivity.class));
        dismiss();
    }

    public void initViewPlayWellHelpDialog2(View view) {
        dismiss();
    }
}
