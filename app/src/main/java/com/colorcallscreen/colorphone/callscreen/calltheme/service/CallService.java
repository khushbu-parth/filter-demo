package com.colorcallscreen.colorphone.callscreen.calltheme.service;

import android.content.Intent;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.InCallService;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ParentCallAcitvity;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallOpreationHandler;


public class CallService extends InCallService {
    public static CallService callService;
    public boolean isOnCallAddedCalled = false;

    @Override // android.telecom.InCallService
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        this.isOnCallAddedCalled = true;
        Log.e("Sach Ring", "onCallAdded");
        callService = this;
        String numberFromCall = Utility.numberFromCall(call);
        if (numberFromCall != null && !Helper.isCallScreeiningDone && call.getState() == 2 && BlockHelper.isPhoneNumberInBlockedList(numberFromCall, this)) {
            call.disconnect();
            return;
        }
        Intent intent = new Intent(this, ParentCallAcitvity.class);
        intent.setFlags(272629760);
        startActivity(intent);
        CallHandler.onCallAdded(this, numberFromCall, call);
    }

    @Override // android.telecom.InCallService
    public void onCallAudioStateChanged(CallAudioState callAudioState) {
        super.onCallAudioStateChanged(callAudioState);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.CallAudioRouteChangedBroadcast));
    }

    @Override // android.telecom.InCallService
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        this.isOnCallAddedCalled = false;
        CallHandler.onCallEnded(this, call);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.CallDisconnected));
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        callService = this;
    }

    @Override // android.telecom.InCallService
    public void onSilenceRinger() {
        CallOpreationHandler callOpreationHandler;
        super.onSilenceRinger();
        CallHandler callHandler = CallHandler.sharedInstance;
        if (callHandler == null || (callOpreationHandler = callHandler.callOpreationHandler) == null) {
            return;
        }
        callOpreationHandler.userAskedToPutCallOnMute();
    }
}
