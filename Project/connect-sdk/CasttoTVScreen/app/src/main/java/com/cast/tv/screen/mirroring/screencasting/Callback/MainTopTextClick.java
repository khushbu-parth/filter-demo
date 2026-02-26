package com.cast.tv.screen.mirroring.screencasting.Callback;

import android.content.Context;
import android.content.Intent;
import android.text.style.URLSpan;
import android.view.View;

import com.cast.tv.screen.mirroring.screencasting.Report.ReportUtil;
import com.cast.tv.screen.mirroring.screencasting.UI.how2use.How2UseActivity;

import java.lang.ref.WeakReference;


public class MainTopTextClick extends URLSpan {
    private WeakReference<Context> mContext;

    public MainTopTextClick(Context context, String str) {
        super(str);
        if (this.mContext == null) {
            this.mContext = new WeakReference<>(context);
        }
    }

    public static void startActivity(Context previousIntent, Intent nextIntent) {
        if (nextIntent == null) {
            return;
        }
        previousIntent.startActivity(nextIntent);
    }

    @Override
    public void onClick(View view) {
        if (this.mContext.get() != null) {
            ReportUtil.clickHomeInstruction();
            startActivity(this.mContext.get(), new Intent(this.mContext.get(), How2UseActivity.class));
        }
    }
}
