package com.colorcallscreen.colorphone.callscreen.calltheme.custom;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telecom.Call;
import android.widget.RemoteViews;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.CallService;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.audiomanager.BoloAudioManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.AcceptDeclineCallManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.CallRingingManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler;


public class NotificationCallHandler {
    public static final String BOLO_ACTION_ACCEPT_CALL = "bolo.codeplay.action.ACCEPT_CALL";
    public static final String BOLO_ACTION_BLOCK_CALL = "bolo.codeplay.action.BLOCK_CALL";
    public static final String BOLO_ACTION_DECLINE_CALL = "bolo.codeplay.action.DECLINE_CALL";
    public static final String BOLO_ACTION_MSG_CALL = "bolo.codeplay.action.MSG_CALL";
    public static final String BOLO_ACTION_SILENT_CALL = "bolo.codeplay.action.SILENT_CALL";
    public static final String BOLO_ACTION_SPEAKER_CALL = "bolo.codeplay.action.SPEAKER_CALL";
    public static final String BOLO_ACTION_WHATSAPP_CALL = "bolo.codeplay.action.WHATSAPP_CALL";
    private BroadcastReceiver acceptCallReceiver;
    private SmallPopUpHandler.BlockConfirm blockConfirm;
    private NotificationCompat.Builder builder;
    CallModel callModel;
    private int callStatus = -5;
    private Context context;
    private BroadcastReceiver declineCallReciever;
    private RemoteViews remoteViews;
    private Handler timerHandler;


    public interface NotificationHandler {
        void onActivityOpened();
    }

    public NotificationCallHandler(Context context) {
        this.context = context;
        if (context == null) {
            this.context = BoloApplication.getApplication();
        }
    }

    public void checkAfterSometimeForNotification() {
        Handler handler = new Handler(Looper.getMainLooper());
        this.timerHandler = handler;
        handler.postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.NotificationCallHandler.1
            @Override 
            public void run() {
                if (NotificationCallHandler.this.timerHandler != null) {
                    NotificationCallHandler.this.timerHandler = null;
                    if (!CallService.callService.getCalls().isEmpty() && CallService.callService.getCalls().get(0).getState() != 7 && CallService.callService.getCalls().get(0).getState() != 10) {
                        NotificationCallHandler.this.checkAfterSometimeForNotification();
                    } else {
                        NotificationCallHandler.this.removeNotification();
                    }
                }
            }
        }, 3000L);
    }

    private void createNotification(boolean z, int i, boolean z2, final NotificationHandler notificationHandler) {
        RemoteViews remoteViews;
        NotificationChannel notificationChannel;
        String string = this.context.getString(R.string.default_notification_channel_id);
        if (z) {
            string = "VaniChannelID";
        }
        PendingIntent pendingIntentParentCall = Helper.pendingIntentParentCall(false, true);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, string);
        this.builder = builder;
        builder.setContentIntent(pendingIntentParentCall);
        NotificationManager notificationManager = (NotificationManager) this.context.getSystemService("notification");
        this.builder.setSmallIcon(R.drawable.notification_call);
        this.builder.setAutoCancel(false);
        if (i == 0) {
            remoteViews = new RemoteViews(this.context.getPackageName(), (int) R.layout.notification_view);
            RemoteViews remoteViews2 = new RemoteViews(this.context.getPackageName(), (int) R.layout.notification_view);
            remoteViews2.setViewVisibility(R.id.pop_btn_pannel, 8);
            updateUI(remoteViews2, i);
            setListeners(remoteViews2, i);
            remoteViews2.setViewVisibility(R.id.pop_sim_name, 0);
            String sim = this.callModel.getSim();
            if (sim != null && !sim.isEmpty()) {
                remoteViews2.setTextViewText(R.id.pop_sim_name, sim);
            } else {
                remoteViews2.setTextViewText(R.id.pop_sim_name, "");
            }
            this.builder.setCustomContentView(remoteViews2);
        } else {
            remoteViews = new RemoteViews(this.context.getPackageName(), (int) R.layout.on_going_call_notification);
            this.builder.setCustomContentView(remoteViews);
        }
        setListeners(remoteViews, i);
        updateUI(remoteViews, i);
        this.builder.setCustomHeadsUpContentView(remoteViews);
        this.builder.setCustomBigContentView(remoteViews);
        this.builder.setOngoing(true);
        this.builder.setPriority(1);
        this.builder.setVisibility(1);
        this.builder.setCategory(NotificationCompat.CATEGORY_CALL);
        if (z) {
            this.builder.setFullScreenIntent(pendingIntentParentCall, true);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            if (z) {
                notificationChannel = new NotificationChannel(string, "Caller theme", 4);
            } else {
                notificationChannel = new NotificationChannel(string, "Caller Theme Channel", 3);
            }
            notificationChannel.setLockscreenVisibility(1);
            notificationChannel.setSound(null, null);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(9, this.builder.build());
        if (z2) {
            try {
                Handler handler = null;
                pendingIntentParentCall.send(0, new PendingIntent.OnFinished() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.NotificationCallHandler.2
                    @Override // android.app.PendingIntent.OnFinished
                    public void onSendFinished(PendingIntent pendingIntent, Intent intent, int i2, String str, Bundle bundle) {
                        NotificationHandler notificationHandler2 = notificationHandler;
                        if (notificationHandler2 != null) {
                            notificationHandler2.onActivityOpened();
                        }
                    }
                }, (Handler) null);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnectRunning() {
        CallModel callModel = this.callModel;
        if (callModel != null && callModel.getCall() != null) {
            this.callModel.getCall().disconnect();
            removeNotification();
        }
        try {
            this.context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        } catch (Exception unused) {
        }
    }

    private void setListeners(RemoteViews remoteViews, final int i) {
        String str;
        if (remoteViews == null) {
            return;
        }
        if (this.context == null) {
            this.context = BoloApplication.getApplication();
        }
        try {
            remoteViews.setOnClickPendingIntent(R.id.pop_decline, PendingIntent.getBroadcast(this.context, 0, new Intent(BOLO_ACTION_DECLINE_CALL), 134217728));
            this.declineCallReciever = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.NotificationCallHandler.3
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (NotificationCallHandler.this.callModel == null) {
                        return;
                    }
                    if (intent.getAction().equalsIgnoreCase(NotificationCallHandler.BOLO_ACTION_SILENT_CALL)) {
                        BoloAudioManager.muteCall(BoloApplication.getApplication());
                    } else if (intent.getAction().equalsIgnoreCase(NotificationCallHandler.BOLO_ACTION_MSG_CALL)) {
                        Helper.declineCall(CallRingingManager.CallResponseGesture.Hand, context, NotificationCallHandler.this.callModel.getCall(), SettingsActivity.getAutoReplyMsg(BoloApplication.getApplication()));
                    } else if (intent.getAction().equalsIgnoreCase(NotificationCallHandler.BOLO_ACTION_BLOCK_CALL)) {
                        if (NotificationCallHandler.this.blockConfirm == null || !NotificationCallHandler.this.blockConfirm.isAlertShowing) {
                            NotificationCallHandler.this.blockConfirm = new SmallPopUpHandler.BlockConfirm(BoloApplication.getApplication(), NotificationCallHandler.this.callModel);
                            NotificationCallHandler.this.blockConfirm.setAsWindow();
                        }
                    } else if (intent.getAction().equalsIgnoreCase(NotificationCallHandler.BOLO_ACTION_WHATSAPP_CALL)) {
                        Helper.declineCall(CallRingingManager.CallResponseGesture.Hand, context, NotificationCallHandler.this.callModel.getCall(), null);
                        if (Utility.sendAutoReplyWithSocialMedia(NotificationCallHandler.this.callModel.getCall(), SettingsActivity.getAutoReplyMsg(BoloApplication.getApplication()))) {
                            Toast.makeText(BoloApplication.getApplication(), "Whatsapp sent", 0);
                        } else {
                            Helper.sendWhatsAppMsg(BoloApplication.getApplication(), Utility.getPhoneNumberOfCall(NotificationCallHandler.this.callModel, BoloApplication.getApplication()), SettingsActivity.getAutoReplyMsg(BoloApplication.getApplication()));
                        }
                    } else {
                        int i2 = i;
                        if (i2 == 2) {
                            NotificationCallHandler.this.disconnectRunning();
                        } else if (i2 == 0) {
                            NotificationCallHandler.this.declineCall(false);
                        }
                    }
                }
            };
            if (i == 0) {
                Intent intent = new Intent(BOLO_ACTION_ACCEPT_CALL);
                Intent intent2 = new Intent(BOLO_ACTION_SPEAKER_CALL);
                Intent intent3 = new Intent(BOLO_ACTION_MSG_CALL);
                Intent intent4 = new Intent(BOLO_ACTION_WHATSAPP_CALL);
                Intent intent5 = new Intent(BOLO_ACTION_BLOCK_CALL);
                Intent intent6 = new Intent(BOLO_ACTION_SILENT_CALL);
                Context context = this.context;
                str = BOLO_ACTION_DECLINE_CALL;
                remoteViews.setOnClickPendingIntent(R.id.pop_accept, PendingIntent.getBroadcast(context, 0, intent, 134217728));
                remoteViews.setOnClickPendingIntent(R.id.pop_speakerBtn, PendingIntent.getBroadcast(this.context, 0, intent2, 134217728));
                remoteViews.setOnClickPendingIntent(R.id.pop_whatsappBtn, PendingIntent.getBroadcast(this.context, 0, intent4, 134217728));
                remoteViews.setOnClickPendingIntent(R.id.pop_smsBtn, PendingIntent.getBroadcast(this.context, 0, intent3, 134217728));
                remoteViews.setOnClickPendingIntent(R.id.pop_blockBtn, PendingIntent.getBroadcast(this.context, 0, intent5, 134217728));
                remoteViews.setOnClickPendingIntent(R.id.mute_area, PendingIntent.getBroadcast(this.context, 0, intent6, 134217728));
                BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.NotificationCallHandler.4
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context2, Intent intent7) {
                        if (NotificationCallHandler.this.callModel == null) {
                            return;
                        }
                        if (intent7.getAction().equalsIgnoreCase(NotificationCallHandler.BOLO_ACTION_SPEAKER_CALL)) {
                            NotificationCallHandler.this.answerCall(true);
                        } else {
                            NotificationCallHandler.this.answerCall(false);
                        }
                    }
                };
                this.acceptCallReceiver = broadcastReceiver;
                this.context.registerReceiver(broadcastReceiver, new IntentFilter(BOLO_ACTION_ACCEPT_CALL));
                this.context.registerReceiver(this.acceptCallReceiver, new IntentFilter(BOLO_ACTION_SPEAKER_CALL));
                this.context.registerReceiver(this.declineCallReciever, new IntentFilter(BOLO_ACTION_BLOCK_CALL));
                this.context.registerReceiver(this.declineCallReciever, new IntentFilter(BOLO_ACTION_MSG_CALL));
                this.context.registerReceiver(this.declineCallReciever, new IntentFilter(BOLO_ACTION_WHATSAPP_CALL));
                this.context.registerReceiver(this.declineCallReciever, new IntentFilter(BOLO_ACTION_SILENT_CALL));
            } else {
                str = BOLO_ACTION_DECLINE_CALL;
            }
            this.context.registerReceiver(this.declineCallReciever, new IntentFilter(str));
        } catch (Exception unused) {
        }
    }

    private void setUiDetails(CallModel callModel) {
        RemoteViews remoteViews = this.remoteViews;
        if (remoteViews == null || callModel == null) {
            return;
        }
        try {
            if (this.callStatus == 2) {
                remoteViews.setViewVisibility(R.id.pop_accept, 8);
                this.remoteViews.setViewVisibility(R.id.noti_ballFrame_2, 0);
            }
            String nameFromCall = Utility.getNameFromCall(callModel, this.context);
            this.remoteViews.setTextViewText(R.id.pop_name, nameFromCall);
            this.remoteViews.setTextViewText(R.id.pop_number, Utility.getPhoneNumberOfCall(callModel, this.context));
            Bitmap imageOfUserCall = Utility.getImageOfUserCall(callModel, this.context);
            if (imageOfUserCall != null) {
                this.remoteViews.setViewVisibility(R.id.noti_textView_2, 8);
                this.remoteViews.setImageViewBitmap(R.id.noti_user_img_2, imageOfUserCall);
                return;
            }
            this.remoteViews.setViewVisibility(R.id.noti_user_img_2, 8);
            this.remoteViews.setTextViewText(R.id.noti_textView_2, nameFromCall.substring(0, 1));
        } catch (Exception unused) {
        }
    }

    private void updateUI(RemoteViews remoteViews, int i) {
        CharSequence nameFromCall = Utility.getNameFromCall(this.callModel, this.context);
        String phoneNumberOfCall = Utility.getPhoneNumberOfCall(this.callModel, this.context);
        if (nameFromCall != null) {
            remoteViews.setTextViewText(R.id.pop_name, nameFromCall);
        }
        if (phoneNumberOfCall != null) {
            remoteViews.setTextViewText(R.id.pop_number, Utility.getPhoneNumberOfCall(this.callModel, this.context));
        }
        if (i == 0) {
            String sim = this.callModel.getSim();
            if (sim != null && !sim.isEmpty()) {
                remoteViews.setTextViewText(R.id.pop_sim_name, sim);
            } else {
                remoteViews.setViewVisibility(R.id.pop_sim_name, 8);
            }
            if (Utility.hasWhatsappInstalled(BoloApplication.getApplication())) {
                return;
            }
            remoteViews.setViewVisibility(R.id.pop_whatsappBtn, 8);
        }
    }

    protected void answerCall(boolean z) {
        CallModel callModel = this.callModel;
        if (callModel != null && callModel.getCall() != null) {
            Helper.acceptCall(CallRingingManager.CallResponseGesture.Hand, z, this.context, this.callModel.getCall());
        } else {
            AcceptDeclineCallManager.acceptCall(z, this.context, null);
        }
        try {
            this.context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        } catch (Exception unused) {
        }
    }

    public void cleanUponCallComplete() {
        if (this.context == null) {
            this.context = BoloApplication.getApplication();
        }
        try {
            BroadcastReceiver broadcastReceiver = this.declineCallReciever;
            if (broadcastReceiver != null) {
                this.context.unregisterReceiver(broadcastReceiver);
            }
        } catch (Exception unused) {
        }
        try {
            BroadcastReceiver broadcastReceiver2 = this.acceptCallReceiver;
            if (broadcastReceiver2 != null) {
                this.context.unregisterReceiver(broadcastReceiver2);
            }
        } catch (Exception unused2) {
        }
        this.blockConfirm = null;
        this.callModel = null;
        this.remoteViews = null;
        this.callStatus = -5;
    }

    public void createIncomingNotification(int i, CallModel callModel, boolean z, boolean z2, NotificationHandler notificationHandler) {
        if (this.callStatus == i && z) {
            return;
        }
        removeNotification();
        CallService callService = CallService.callService;
        if (callService != null && callService.getCalls().size() > 0) {
            for (Call call : CallService.callService.getCalls()) {
                if (call.getState() != 7 && call.getState() != 10) {
                    break;
                }
            }
        }
        if (callModel != null) {
            this.callStatus = i;
            this.callModel = callModel;
            createNotification(z, i, z2, notificationHandler);
        }
    }

    protected void declineCall(boolean z) {
        if (this.callModel == null) {
            return;
        }
        if (z) {
            Helper.declineCall(CallRingingManager.CallResponseGesture.Hand, this.context, this.callModel.getCall(), SettingsActivity.getAutoReplyMsg(BoloApplication.getApplication()));
        } else {
            Helper.declineCall(CallRingingManager.CallResponseGesture.Hand, this.context, this.callModel.getCall(), null);
        }
        try {
            this.context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        } catch (Exception unused) {
        }
    }

    public void removeNotification() {
        if (this.context == null) {
            this.context = BoloApplication.getApplication();
        }
        Context context = this.context;
        if (context == null) {
            return;
        }
        ((NotificationManager) context.getSystemService("notification")).cancelAll();
    }
}
