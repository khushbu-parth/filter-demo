package com.colorcallscreen.colorphone.callscreen.calltheme.windowview;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.audiomanager.BoloAudioManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.components.BoloSpeechRecognizer;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.CallRingingManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDataSource;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDelegate;
import com.colorcallscreen.colorphone.callscreen.calltheme.singleton.BoloSingleTon;

import java.util.ArrayList;
import java.util.List;


public class CallOpreationHandler implements RingingCallManagerDataSource, RingingCallManagerDelegate {
    private CallModel callModel;
    private CallRingingManager callRingingManager;
    private Context context;
    private boolean isVoiceReconigerActive;
    private Handler ringtoneMaxHandler;
    private Runnable ringtoneMaxRunable;
    private Handler timerHandler;
    private TimerHandlerTask timerHandlerTask;
    private boolean userAskedToPutInMute = false;


    public enum TimerHandlerTask {
        TimerHandlerTaskNone,
        TimerHandlerTaskSpeakUpName,
        TimerHandlerTaskStartRingingTone,
        TimerHandlerTaskStopRingingTone
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDelegate
    public void onSpeechRecognizerError(int i) {
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDelegate
    public void onSpeechStarted() {
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDataSource
    public int ringingModeInState(RingingCallManagerDataSource.CallManagerState callManagerState) {
        return 0;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDataSource
    public boolean shouldSpeakUpCallerName() {
        return true;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDataSource
    public boolean shouldSpeechRecoginzerRestartAfterFinish() {
        return true;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDelegate
    public void unableToStartSpeakUp(RingingCallManagerDelegate.SpeakUpError speakUpError) {
    }

    private void changeToDefaultRingtoneSetting(Context context, boolean z) {
        CallRingingManager callRingingManager = this.callRingingManager;
        if (callRingingManager != null) {
            callRingingManager.changeToDeafultSetting(context, z, false);
        }
    }

    private void changeToVoiceRecnognizerSetting() {
        CallRingingManager callRingingManager = this.callRingingManager;
        if (callRingingManager != null) {
            callRingingManager.changeToVoiceRecnoginzerSetting(this.context);
        }
    }

    private boolean checkIfWordContainsInArray(List<String> list, List<String> list2, String str) {
        if (list == null) {
            return false;
        }
        for (String str2 : list) {
            if (!str2.isEmpty()) {
                String lowerCase = str2.toLowerCase();
                String[] split = lowerCase.split(" ");
                if (split.length > 0) {
                    for (String str3 : split) {
                        if (list2.contains(str3.trim())) {
                            TextUtils.join(",", list2);
                            return true;
                        }
                    }
                    continue;
                } else if (list2.contains(lowerCase.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void startLooperForTypeAndSeconds(TimerHandlerTask timerHandlerTask, int i) {
        this.timerHandlerTask = timerHandlerTask;
        startLooperForSpeakNameOrRingtone(i);
    }

    private void startMaxRingtoneLimit() {
        this.ringtoneMaxHandler = new Handler();
        Runnable runnable = new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallOpreationHandler.1
            @Override 
            public void run() {
                CallOpreationHandler.this.cleanUpEverything();
            }
        };
        this.ringtoneMaxRunable = runnable;
        this.ringtoneMaxHandler.postDelayed(runnable, 35000L);
    }

    public void startRingtone() {
        if (this.callRingingManager != null) {
            changeToDefaultRingtoneSetting(this.context, false);
            startLooperForTypeAndSeconds(TimerHandlerTask.TimerHandlerTaskStopRingingTone, PathInterpolatorCompat.MAX_NUM_POINTS);
        }
    }

    public void startSpeakUp(boolean z) {
        if (z) {
            startSpeakUp();
            return;
        }
        if (PreferenceUtils.getInstance().getBoolean(Constants.CALLER_NAME)) {
            if (this.callRingingManager != null && !this.userAskedToPutInMute) {
                changeToDefaultRingtoneSetting(this.context, true);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallOpreationHandler.2
                    @Override 
                    public void run() {
                        CallOpreationHandler.this.startSpeakUp();
                    }
                }, 2000L);
            }
        } else {
            startRingtone();
        }
        startVoiceRecognizer();
    }

    private void startVoiceRecognizer() {
        if (this.userAskedToPutInMute || !PreferenceUtils.getInstance().getBoolean(Constants.VOCIE_RECOGITION) || this.callRingingManager == null) {
            return;
        }
        if (!PreferenceUtils.getInstance().getBoolean(Constants.CALLER_NAME)) {
            this.callRingingManager.changeToMuteSetting(BoloApplication.getApplication());
        }
        this.callRingingManager.startVoiceRecognizer(this.context, true);
        this.isVoiceReconigerActive = true;
    }

    private void stopAndCleanUpRingingManager() {
        CallRingingManager callRingingManager = this.callRingingManager;
        if (callRingingManager != null) {
            callRingingManager.cleanUpEverything(BoloApplication.getApplication());
            this.callRingingManager = null;
        }
    }

    public void stopRingtone() {
        if (this.callRingingManager != null && shouldStartSpeechRecognizer() && !this.callRingingManager.canContinueRingtone()) {
            changeToVoiceRecnognizerSetting();
        }
        startLooperForTypeAndSeconds(TimerHandlerTask.TimerHandlerTaskStartRingingTone, 4000);
    }

    public void cleanUpEverything() {
        Runnable runnable;
        cleanUpTimer();
        if (this.callRingingManager != null) {
            stopAndCleanUpRingingManager();
        }
        this.callModel = null;
        Helper.cleanUpHelper();
        Handler handler = this.ringtoneMaxHandler;
        if (handler == null || (runnable = this.ringtoneMaxRunable) == null) {
            return;
        }
        handler.removeCallbacks(runnable);
        this.ringtoneMaxRunable = null;
        this.ringtoneMaxHandler = null;
    }

    public void cleanUpTimer() {
        Handler handler = this.timerHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.timerHandler = null;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public CallRingingManager getCallRingingManager() {
        return this.callRingingManager;
    }

    public void init(Context context, CallModel callModel) {
        this.context = context;
        this.callModel = callModel;
        if (this.callRingingManager == null) {
            this.callRingingManager = new CallRingingManager(this, this, context);
        }
        startMaxRingtoneLimit();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDelegate
    public void onSpeakUpStarted() {
        CallRingingManager callRingingManager = this.callRingingManager;
        if (callRingingManager != null) {
            callRingingManager.changeToSpeakUpSetting(this.context);
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDelegate
    public void onSpeakingEnded() {
        if (this.callRingingManager != null) {
            if (this.isVoiceReconigerActive) {
                changeToVoiceRecnognizerSetting();
            } else {
                changeToDefaultRingtoneSetting(this.context, false);
            }
        }
        if (this.userAskedToPutInMute) {
            return;
        }
        startLooperForTypeAndSeconds(TimerHandlerTask.TimerHandlerTaskSpeakUpName, 4000);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDelegate
    public void onSpeechRecognizerResult(ArrayList<String> arrayList, BoloSpeechRecognizer.SpeechRecognizerResultType speechRecognizerResultType, BoloSpeechRecognizer.SpeechRecognizerResultFrom speechRecognizerResultFrom, String str) {
        if (this.callRingingManager != null) {
            if (checkIfWordContainsInArray(arrayList, BoloSingleTon.getInstance(this.context).getAcceptList(), Constants.acceptTag)) {
                stopAndCleanUpRingingManager();
                Helper.acceptCall(CallRingingManager.CallResponseGesture.Voice, false, this.context, this.callModel.getCall());
            } else if (checkIfWordContainsInArray(arrayList, BoloSingleTon.getInstance(this.context).getSpeakerList(), Constants.speakerTag)) {
                stopAndCleanUpRingingManager();
                Helper.acceptCall(CallRingingManager.CallResponseGesture.Voice, true, this.context, this.callModel.getCall());
            } else if (checkIfWordContainsInArray(arrayList, BoloSingleTon.getInstance(this.context).getDeclineList(), Constants.declineTag)) {
                stopAndCleanUpRingingManager();
                Helper.declineCall(CallRingingManager.CallResponseGesture.Voice, this.context, this.callModel.getCall(), null);
            } else if (checkIfWordContainsInArray(arrayList, BoloSingleTon.getInstance(this.context).getAutoMessageList(), Constants.autoReplyTag)) {
                stopAndCleanUpRingingManager();
                Helper.declineCall(CallRingingManager.CallResponseGesture.Voice, this.context, this.callModel.getCall(), SettingsActivity.getAutoReplyMsg(BoloApplication.getApplication()));
            } else if (checkIfWordContainsInArray(arrayList, BoloSingleTon.getInstance(this.context).getMuteList(), Constants.muteTag)) {
                stopAndCleanUpRingingManager();
                BoloAudioManager.muteCall(BoloApplication.getApplication());
            } else if (checkIfWordContainsInArray(arrayList, BoloSingleTon.getInstance(this.context).getBlockList(), Constants.blockTag)) {
                stopAndCleanUpRingingManager();
                String phoneNumberOfCall = Utility.getPhoneNumberOfCall(this.callModel, BoloApplication.getApplication());
                Helper.declineCall(CallRingingManager.CallResponseGesture.Voice, this.context, this.callModel.getCall(), null);
                if (phoneNumberOfCall != null) {
                    BlockHelper.addToBlockList(phoneNumberOfCall, this.context);
                }
            }
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDataSource
    public boolean shouldStartSpeechRecognizer() {
        return PreferenceUtils.getInstance().getBoolean(Constants.VOCIE_RECOGITION);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDataSource
    public String speakUpText() {
        String nameFromCall = Utility.getNameFromCall(this.callModel, this.context);
        return nameFromCall != null ? nameFromCall + " " + BoloApplication.getApplication().getString(R.string.calling) : "";
    }

    public void startLooperForSpeakNameOrRingtone(int i) {
        if (this.userAskedToPutInMute) {
            cleanUpTimer();
            return;
        }
        if (this.timerHandler != null) {
            cleanUpTimer();
        }
        Handler handler = new Handler(Looper.getMainLooper());
        this.timerHandler = handler;
        handler.postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallOpreationHandler.3
            @Override 
            public void run() {
                if (CallOpreationHandler.this.timerHandler != null) {
                    if (CallOpreationHandler.this.timerHandlerTask != TimerHandlerTask.TimerHandlerTaskSpeakUpName) {
                        if (CallOpreationHandler.this.timerHandlerTask != TimerHandlerTask.TimerHandlerTaskStartRingingTone) {
                            if (CallOpreationHandler.this.timerHandlerTask != TimerHandlerTask.TimerHandlerTaskStopRingingTone || CallOpreationHandler.this.callRingingManager == null) {
                                return;
                            }
                            CallOpreationHandler.this.stopRingtone();
                            return;
                        }
                        CallOpreationHandler.this.startRingtone();
                        return;
                    }
                    CallOpreationHandler.this.startSpeakUp(false);
                }
            }
        }, i);
    }

    public void startSpeakUpAndListing(CallModel callModel) {
        Utility.isConnected(true);
        this.callModel = callModel;
        startSpeakUp(false);
    }

    public void userAskedToPutCallOnMute() {
        this.userAskedToPutInMute = true;
        stopAndCleanUpRingingManager();
        LocalBroadcastManager.getInstance(BoloApplication.getApplication()).sendBroadcast(new Intent(Constants.PhoneInMuteBroadcast));
        CallHandler callHandler = CallHandler.sharedInstance;
        if (callHandler != null) {
            callHandler.stopLED();
        }
    }

    public void startSpeakUp() {
        CallRingingManager callRingingManager = this.callRingingManager;
        if (callRingingManager != null) {
            callRingingManager.startNameSpeakUp(this.context, true);
        }
    }
}
