package com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.telephony.TelephonyManager;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.CallService;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.audiomanager.BoloAudioManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.AcceptDeclineCallManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallOpreationHandler;

import java.util.ArrayList;
import java.util.List;


public class BoloCallHandler {
    private static final BoloCallHandler ourInstance = new BoloCallHandler();
    private CallOpreationHandler callOpreationHandler;
    private Context context;
    private BoloPhoneStateListener listener;
    private Notification.Action[] notificationActions;
    private TelephonyManager telephonyManager;
    private boolean isScreenShown = false;
    public List<String> pendingTaskAfterCallConnected = new ArrayList();

    public static BoloCallHandler getInstance() {
        return ourInstance;
    }

    private boolean isCallActive(Context context) {
        return context != null && ((AudioManager) context.getSystemService("audio")).getMode() == 2;
    }

    public void addCallHookTasks(String str) {
        this.pendingTaskAfterCallConnected.add(str);
    }

    public void callHasBeenPutToSpeaker() {
        this.pendingTaskAfterCallConnected.remove(Constants.PUT_CALL_ON_SPEAKER_TASK);
    }

    public Notification.Action[] getNotificationActions() {
        return this.notificationActions;
    }

    public void onIncomingCallRecived(Context context, Intent intent) {
        this.context = context;
        startPhoneStateService(context);
    }

    /* JADX WARN: Type inference failed for: r2v11, types: [com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.BoloCallHandler$2] */
    /* JADX WARN: Type inference failed for: r2v6, types: [com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.BoloCallHandler$1] */
    public void onOffHookCall(String str) {
        CallService callService = CallService.callService;
        if ((callService == null || !callService.isOnCallAddedCalled) && this.pendingTaskAfterCallConnected.contains(Constants.PUT_CALL_ON_SPEAKER_TASK)) {
            this.pendingTaskAfterCallConnected.remove(Constants.PUT_CALL_ON_SPEAKER_TASK);
            try {
                new Thread() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.BoloCallHandler.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        try {
                            BoloAudioManager.putOnSpeaker(BoloApplication.getApplication());
                        } catch (Exception unused) {
                        }
                    }
                }.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.pendingTaskAfterCallConnected.contains(Constants.PUT_CALL_ON_UMUTE_TASK)) {
            this.pendingTaskAfterCallConnected.remove(Constants.PUT_CALL_ON_UMUTE_TASK);
            try {
                new Thread() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.BoloCallHandler.2
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        try {
                            Thread.sleep(600L);
                            BoloAudioManager.removeSpeakerMode(BoloApplication.getApplication());
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }.start();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void onPhoneStartedRinging(final String str) {
        Context context;
        if (!PreferenceUtils.getInstance().getBoolean(Constants.ENABLE_BOLO) || isCallActive(this.context)) {
            return;
        }
        new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.BoloCallHandler.3
            @Override 
            public void run() {
                CallService callService = CallService.callService;
                if ((callService == null || !callService.isOnCallAddedCalled) && BoloCallHandler.this.callOpreationHandler == null) {
                    BoloCallHandler.this.callOpreationHandler = new CallOpreationHandler();
                    CallModel callModel = new CallModel();
                    callModel.setPhnNumber(str);
                    callModel.setName(Utility.getNameFromCall(callModel, BoloApplication.getApplication()));
                    BoloCallHandler.this.callOpreationHandler.init(BoloApplication.getApplication(), callModel);
                    BoloCallHandler.this.callOpreationHandler.startSpeakUpAndListing(callModel);
                }
            }
        }, 500L);
        if ((Build.VERSION.SDK_INT < 23 || PreferenceUtils.getInstance().getBoolean(Utility.IS_DEFAULT_PACKAGE_NOT_FOUND)) && (context = this.context) != null && BlockHelper.isPhoneNumberInBlockedList(str, context)) {
            AcceptDeclineCallManager.declineCall(this.context, null, null);
        }
    }

    public void onPhoneStopRinging(String str) {
        CallOpreationHandler callOpreationHandler = this.callOpreationHandler;
        if (callOpreationHandler != null) {
            callOpreationHandler.cleanUpEverything();
            this.callOpreationHandler = null;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            PreferenceUtils.getInstance().getBoolean(Utility.IS_DEFAULT_PACKAGE_NOT_FOUND);
        }
    }

    public void setNotificationActions(Notification.Action[] actionArr) {
        this.notificationActions = actionArr;
    }

    public void startPhoneStateService(Context context) {
        try {
            if (this.context == null) {
                this.context = context;
            }
            if (this.context == null) {
                this.context = BoloApplication.getApplication();
            }
            if (this.telephonyManager == null) {
                this.listener = new BoloPhoneStateListener();
                TelephonyManager telephonyManager = (TelephonyManager) this.context.getSystemService("phone");
                this.telephonyManager = telephonyManager;
                telephonyManager.listen(this.listener, 32);
            }
        } catch (Exception unused) {
        }
    }
}
