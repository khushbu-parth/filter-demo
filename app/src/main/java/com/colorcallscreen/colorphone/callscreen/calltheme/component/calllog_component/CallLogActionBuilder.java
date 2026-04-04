package com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallLogUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;


public class CallLogActionBuilder implements CallLogAction {
    private Context context;

    public CallLogActionBuilder(Context context) {
        this.context = context;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogAction
    public void onBlock(final String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context, R.style.MyAlertDialogTheme2);
        builder.setTitle(this.context.getString(R.string.block_this_caller));
        builder.setMessage(this.context.getString(R.string.block_info));
        builder.setPositiveButton(this.context.getString(R.string.block), new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogActionBuilder.1
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                BlockHelper.addToBlockList(str, CallLogActionBuilder.this.context);
            }
        });
        builder.setNegativeButton(this.context.getString(R.string.cancel), (DialogInterface.OnClickListener) null);
        builder.show();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogAction
    public void onUnblock(final String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context, R.style.MyAlertDialogTheme2);
        builder.setMessage(R.string.this_number_will_be_removed);
        builder.setPositiveButton(R.string.unblock, new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogActionBuilder.2
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                BlockHelper.removeFromBlockList(str, CallLogActionBuilder.this.context);
            }
        });
        builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
        builder.show();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogAction
    public void onCall(String str) {
        CallLogUtils.makeCall(BoloApplication.getApplication(), str);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogAction
    public void onMessage(String str) {
        Helper.sendSms(BoloApplication.getApplication(), str);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogAction
    public void sendWhatsAppMsg(String str, String str2) {
        Helper.sendWhatsAppMsg(this.context, str2, str);
    }
}
