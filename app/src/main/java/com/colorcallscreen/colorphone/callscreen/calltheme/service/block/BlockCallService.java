package com.colorcallscreen.colorphone.callscreen.calltheme.service.block;

import android.content.Intent;
import android.os.IBinder;
import android.telecom.Call;
import android.telecom.CallScreeningService;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.CallService;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.audiomanager.BoloAudioManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;


public class BlockCallService extends CallScreeningService {
    public static int defaultAlarmIndex = -1;
    public static int defaultNotificationIndex = -1;
    public static int defaultRingingIndex = -1;
    public static int defaultRingingMode = -1;
    public static int defaultSystemSound = -1;

    public static void cleanRingtone() {
        defaultRingingMode = -1;
        defaultRingingIndex = -1;
        defaultSystemSound = -1;
        defaultAlarmIndex = -1;
        defaultNotificationIndex = -1;
    }

    @Override // android.telecom.CallScreeningService, android.app.Service
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override // android.telecom.CallScreeningService
    public void onScreenCall(Call.Details details) {
        String numberFromCallDetails = Utility.numberFromCallDetails(details);
        Helper.isCallScreeiningDone = true;
        CallService callService = CallService.callService;
        if (callService != null) {
            callService.isOnCallAddedCalled = false;
        }
        if (numberFromCallDetails != null && BlockHelper.isPhoneNumberInBlockedList(numberFromCallDetails, this)) {
            CallResponse.Builder builder = new CallResponse.Builder();
            builder.setDisallowCall(true);
            builder.setRejectCall(true);
            builder.setSkipCallLog(true);
            builder.setSkipNotification(true);
            respondToCall(details, builder.build());
            return;
        }
        CallResponse.Builder builder2 = new CallResponse.Builder();
        builder2.setDisallowCall(false);
        builder2.setRejectCall(false);
        builder2.setSkipCallLog(false);
        builder2.setSkipNotification(false);
        updateRingToneIndex();
        respondToCall(details, builder2.build());
    }

    public void updateRingToneIndex() {
        defaultRingingMode = BoloAudioManager.getAudioStatus(BoloApplication.getApplication());
        defaultRingingIndex = BoloAudioManager.getAudioStreamVolume(BoloApplication.getApplication());
        defaultSystemSound = BoloAudioManager.getAudioStreamVolumeForStream(3, BoloApplication.getApplication());
        defaultAlarmIndex = BoloAudioManager.getAudioStreamVolumeForStream(4, BoloApplication.getApplication());
        defaultNotificationIndex = BoloAudioManager.getAudioStreamVolumeForStream(5, BoloApplication.getApplication());
    }
}
