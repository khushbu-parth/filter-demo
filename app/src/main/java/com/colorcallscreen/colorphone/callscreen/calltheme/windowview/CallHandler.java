package com.colorcallscreen.colorphone.callscreen.calltheme.windowview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.telecom.Call;
import android.telecom.PhoneAccountHandle;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.custom.NotificationCallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ParentActivityLaunchModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.CallService;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockCallService;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.FlashLight;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityBlockCallList;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.AskPopupActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ParentCallAcitvity;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.SimChooseActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.BoloCallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.ContactsHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class CallHandler {
    public static AskPopupActivity askPopupActivity;
    public static CallHandler sharedInstance;
    private CallHead callHead;
    private Context context;
    private PhoneAccountHandle currentPhoneAccount;
    public PowerManager.WakeLock mProximityWakeLock;
    private NotificationCallHandler notificationCallHandler;
    public ParentActivityLaunchModel parentActivityLaunchModel;
    private SmallPopUpHandler popUpHandler;
    private BroadcastReceiver screenOffReceiver;
    public List<CallModel> calls = new ArrayList();
    public CallOpreationHandler callOpreationHandler = new CallOpreationHandler();
    public CallModel tempCallModelForActivity = null;
    public ParentCallAcitvity onCallActivity = new ParentCallAcitvity();
    private boolean isActvityOpenInit = false;
    private boolean isLedOn = false;

    private void addListnerToCall(CallModel callModel) {
        if (callModel.getCall() == null) {
            return;
        }
        callModel.getCall().registerCallback(new Call.Callback() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.1
            @Override // android.telecom.Call.Callback
            public void onCallDestroyed(Call call) {
                super.onCallDestroyed(call);
                try {
                    CallHandler.sharedInstance.onCallDestroyed(call);
                } catch (Exception unused) {
                }
            }

            @Override // android.telecom.Call.Callback
            public void onChildrenChanged(Call call, List<Call> list) {
                super.onChildrenChanged(call, list);
                CallHandler.this.handleConfressCall(call, list);
            }

            @Override // android.telecom.Call.Callback
            public void onConferenceableCallsChanged(Call call, List<Call> list) {
                super.onConferenceableCallsChanged(call, list);
            }

            @Override // android.telecom.Call.Callback
            public void onDetailsChanged(Call call, Call.Details details) {
                super.onDetailsChanged(call, details);
            }

            @Override // android.telecom.Call.Callback
            public void onParentChanged(Call call, Call call2) {
                super.onParentChanged(call, call2);
                CallHandler.this.handleOnParentChange(call, call2);
            }

            @Override // android.telecom.Call.Callback
            public void onPostDialWait(Call call, String str) {
                super.onPostDialWait(call, str);
            }

            @Override // android.telecom.Call.Callback
            public void onStateChanged(Call call, int i) {
                super.onStateChanged(call, i);
                CallHandler.this.onCallStateChanged(call, i);
            }
        });
    }

    private void cleanCallOperation() {
        CallOpreationHandler callOpreationHandler = this.callOpreationHandler;
        if (callOpreationHandler != null) {
            callOpreationHandler.cleanUpEverything();
            this.callOpreationHandler = null;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            BlockCallService.cleanRingtone();
        }
    }

    public void cleanUp(Call call) {
        removeProxmitySensor();
        if (Build.VERSION.SDK_INT >= 24) {
            BlockCallService.cleanRingtone();
        }
        cleanUpSmallWindowInfoForIncomingCall(false);
        cleanCallOperation();
        ParentCallAcitvity parentCallAcitvity = this.onCallActivity;
        if (parentCallAcitvity != null) {
            parentCallAcitvity.cleanUpAndRemoveActivity();
            this.onCallActivity = null;
        }
        this.parentActivityLaunchModel = null;
        this.currentPhoneAccount = null;
        this.context = null;
        this.isActvityOpenInit = false;
        sharedInstance = null;
        if (call != null) {
            showAskPopupIfRequired(call);
        }
        cleanUpNotification();
        try {
            if (this.screenOffReceiver != null) {
                BoloApplication.getApplication().unregisterReceiver(this.screenOffReceiver);
                this.screenOffReceiver = null;
            }
        } catch (Exception unused) {
        }
        try {
            stopLED();
        } catch (Exception unused2) {
        }
        destoryCallHead();
    }

    public static void cleanUpAskPop(boolean z) {
        new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.2
            @Override 
            public void run() {
                if (CallHandler.askPopupActivity != null) {
                    CallHandler.askPopupActivity.finish();
                    CallHandler.askPopupActivity = null;
                }
            }
        }, 200L);
    }

    private void cleanUpNotification() {
        NotificationCallHandler notificationCallHandler = this.notificationCallHandler;
        if (notificationCallHandler != null) {
            notificationCallHandler.removeNotification();
            this.notificationCallHandler.cleanUponCallComplete();
            this.notificationCallHandler = null;
        }
    }

    public void handleConfressCall(Call call, List<Call> list) {
        boolean z;
        CallModel callModelForCall = CallModel.callModelForCall(this.calls, call);
        if (callModelForCall == null) {
            callModelForCall = CallModel.addNewCallModel(this.calls, call);
        }
        if (callModelForCall.getChildCallModel().size() == list.size()) {
            return;
        }
        callModelForCall.setConfressCall(true);
        if (!callModelForCall.getChildCallModel().isEmpty() && list.isEmpty()) {
            callModelForCall.getChildCallModel().clear();
        } else {
            if (callModelForCall.getChildCallModel().size() < list.size()) {
                for (Call call2 : list) {
                    Iterator<CallModel> it = callModelForCall.getChildCallModel().iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (it.next().getCall().equals(call2)) {
                                z = true;
                                break;
                            }
                        } else {
                            z = false;
                            break;
                        }
                    }
                    if (!z) {
                        CallModel callModelForCall2 = CallModel.callModelForCall(this.calls, call2);
                        callModelForCall.getChildCallModel().add(callModelForCall2);
                        this.calls.remove(callModelForCall2);
                    }
                }
            } else if (callModelForCall.getChildCallModel().size() > list.size()) {
                for (int i = 0; i < callModelForCall.getChildCallModel().size(); i++) {
                    if (!list.contains(callModelForCall.getChildCallModel().get(i).getCall())) {
                        callModelForCall.getChildCallModel().remove(i);
                    }
                }
            }
        }
        ParentCallAcitvity parentCallAcitvity = this.onCallActivity;
        if (parentCallAcitvity != null) {
            parentCallAcitvity.updateForConfressCall(callModelForCall);
        }
    }

    private void handleIncomingCall(final CallModel callModel) {
        if (callModel != null) {
            callModel.setCallType(Constants.CallTypeIncoming);
        }
        if (this.calls.size() > 1) {
            handleOutgoingOrActiveCallActivity(callModel, true);
        } else {
            if (callModel.getCall() != null) {
                getNotificationCallHandler().createIncomingNotification(0, callModel, true, true, new NotificationCallHandler.NotificationHandler() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.3
                    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.NotificationCallHandler.NotificationHandler
                    public void onActivityOpened() {
                        if (callModel != null) {
                            CallHandler.this.getNotificationCallHandler().createIncomingNotification(0, callModel, false, false, null);
                        }
                    }
                });
            }
            if (callModel.getCall() != null) {
                new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.4
                    @Override 
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.4.1
                            @Override 
                            public void run() {
                                CallModel callModel2;
                                if (CallHandler.this.callOpreationHandler == null || (callModel2 = callModel) == null) {
                                    return;
                                }
                                CallHandler.this.callOpreationHandler.startSpeakUpAndListing(callModel2);
                            }
                        });
                    }
                }).start();
                new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.5
                    @Override 
                    public void run() {
                        CallHandler.this.starLED();
                    }
                }).start();
            }
            this.currentPhoneAccount = callModel.getCallDetails().getAccountHandle();
        }
        Utility.logEventNew(Constants.CallCategory, "Call_type_incoming");
    }

    public void handleOnParentChange(Call call, Call call2) {
        if (call2 != null || call.getState() == 7 || call.getState() == 10 || isConfressCallGoingOn(call)) {
            return;
        }
        CallModel callModelForCall = CallModel.callModelForCall(this.calls, call);
        if (callModelForCall == null) {
            callModelForCall = CallModel.addNewCallModel(this.calls, call);
        }
        startUpdateActivity(callModelForCall, 2, false);
        addListnerToCall(callModelForCall);
    }

    private boolean isConfressCallGoingOn(Call call) {
        for (CallModel callModel : this.calls) {
            if (!callModel.getChildCallModel().isEmpty()) {
                for (CallModel callModel2 : callModel.getChildCallModel()) {
                    if (!callModel2.getCall().equals(call)) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    public static void onCallAdded(Context context, String str, Call call) {
        CallModel callModel;
        if (sharedInstance == null) {
            CallHandler callHandler = new CallHandler();
            sharedInstance = callHandler;
            callHandler.callHandlerSetup();
            CallHandler callHandler2 = sharedInstance;
            callHandler2.context = context;
            callModel = CallModel.addNewCallModel(callHandler2.calls, call);
            if (callModel.getCall().getState() == 2) {
                sharedInstance.callOpreationHandler.init(context, callModel);
            }
        } else {
            callModel = null;
        }
        if (callModel == null) {
            callModel = CallModel.addNewCallModel(sharedInstance.calls, call);
        }
        sharedInstance.onNewCallAdded(callModel);
        cleanUpAskPop(false);
    }

    public void onCallDestroyed(Call call) {
        CallModel.removeNewCallModel(this.calls, call);
        if (CallService.callService.getCalls().isEmpty()) {
            cleanUp(call);
        }
    }

    public static void onCallEnded(Context context, Call call) {
        CallHandler callHandler = sharedInstance;
        if (callHandler != null) {
            callHandler.onCallDestroyed(call);
        }
    }

    public void onCallStateChanged(Call call, int i) {
        CallModel callModelForCall = CallModel.callModelForCall(this.calls, call);
        if (callModelForCall == null) {
            return;
        }
        handleOutgoingOrActiveCallActivity(callModelForCall, true);
        if (i == 4) {
            BoloCallHandler.getInstance().onOffHookCall(Utility.getPhoneNumberOfCall(callModelForCall, BoloApplication.getApplication()));
        }
    }

    private void onNewCallAdded(CallModel callModel) {
        addListnerToCall(callModel);
        if (callModel.getCall() == null) {
            handleIncomingCall(callModel);
        } else if (callModel.getCall().getState() == 8) {
            PhoneAccountHandle phoneAccountHandle = this.currentPhoneAccount;
            if (phoneAccountHandle != null) {
                onSimSelected(phoneAccountHandle, callModel);
            } else {
                showSelectSIM(callModel);
            }
        } else if (callModel.getCall().getState() == 2) {
            handleIncomingCall(callModel);
        } else if (callModel.getCall().getState() == 1 || callModel.getCall().getState() == 9) {
            handleOutgoingOrActiveCallActivity(callModel, true);
        }
    }

    public static void onNewIncomingCallDetailsAdded(Context context, Call.Details details) {
        CallModel callModel;
        CallHandler callHandler = sharedInstance;
        if (callHandler == null) {
            CallHandler callHandler2 = new CallHandler();
            sharedInstance = callHandler2;
            callHandler2.callHandlerSetup();
            if (sharedInstance.calls.size() > 0) {
                return;
            }
            CallHandler callHandler3 = sharedInstance;
            callHandler3.context = context;
            callModel = CallModel.addNewCallModel(callHandler3.calls, details);
            sharedInstance.callOpreationHandler.init(context, callModel);
        } else if (callHandler.calls.size() > 0) {
            return;
        } else {
            callModel = null;
        }
        if (callModel == null) {
            callModel = CallModel.addNewCallModel(sharedInstance.calls, details);
        }
        sharedInstance.onNewCallAdded(callModel);
        CallModel finalCallModel = callModel;
        new Timer().schedule(new TimerTask() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.6
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                CallHandler callHandler4;
                CallHandler callHandler5;
                CallModel callModel2 = finalCallModel;
                if (callModel2 == null || callModel2.getCall() == null) {
                    if (CallService.callService == null && (callHandler5 = CallHandler.sharedInstance) != null) {
                        callHandler5.cleanUp(callModel2.getCall());
                        return;
                    }
                    CallService callService = CallService.callService;
                    if (callService == null || !callService.getCalls().isEmpty() || (callHandler4 = CallHandler.sharedInstance) == null) {
                        return;
                    }
                    callHandler4.cleanUp(callModel2.getCall());
                }
            }
        }, 1000L);
        cleanUpAskPop(false);
    }

    private void showAskPopupIfRequired(Call call) {
        try {
            if (ActivityBlockCallList.isCallBlockEnabled()) {
                String numberFromCall = Utility.numberFromCall(call);
                String contactNameFromNumber = ContactsHandler.contactNameFromNumber(numberFromCall, BoloApplication.getApplication());
                if (numberFromCall != null && contactNameFromNumber != null && contactNameFromNumber.isEmpty()) {
                    Intent intent = new Intent(BoloApplication.getApplication(), AskPopupActivity.class);
                    intent.addFlags(268435456);
                    intent.putExtra("number", numberFromCall);
                    BoloApplication.getApplication().startActivity(intent);
                }
            }
        } catch (Exception e) {
            Log.e(NotificationCompat.CATEGORY_ERROR, e.getLocalizedMessage());
        }
    }

    private void showSelectSIM(CallModel callModel) {
        Intent intent = new Intent(BoloApplication.getApplication(), SimChooseActivity.class);
        this.tempCallModelForActivity = callModel;
        intent.addFlags(268435456);
        BoloApplication.getApplication().startActivity(intent);
    }

    private void smallWindowInfoForIncomingCall(CallModel callModel) {
        if (this.popUpHandler == null) {
            SmallPopUpHandler smallPopUpHandler = new SmallPopUpHandler(BoloApplication.getApplication());
            this.popUpHandler = smallPopUpHandler;
            smallPopUpHandler.setAsWindow();
            this.popUpHandler.setCallModel(callModel);
        }
    }

    private void startUpdateActivity(CallModel callModel, int i, boolean z) {
        ParentCallAcitvity parentCallAcitvity = this.onCallActivity;
        if (parentCallAcitvity != null) {
            parentCallAcitvity.upateViewForNewCall(callModel, i);
        } else if (callModel.getCall() != null && (callModel.getCall().getState() == 10 || callModel.getCall().getState() == 7)) {
            cleanUpNotification();
        } else if (!this.isActvityOpenInit) {
            this.isActvityOpenInit = true;
            this.parentActivityLaunchModel = new ParentActivityLaunchModel(i, z, this.popUpHandler != null);
            this.tempCallModelForActivity = callModel;
        }
        if (callModel.getCall() == null || callModel.getCall().getState() != 4) {
            return;
        }
        cleanCallOperation();
    }

    public void callHandlerSetup() {
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.7
            @Override 
            public void run() {
                CallHandler.this.screenOffReceiver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.7.1
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context, Intent intent) {
                        CallOpreationHandler callOpreationHandler;
                        if (intent.getAction().equals("android.intent.action.USER_PRESENT")) {
                            if (CallHandler.this.calls.isEmpty()) {
                                return;
                            }
                            CallHandler.this.showCallHead(CallHandler.this.calls.get(0));
                            return;
                        }
                        CallHandler callHandler = CallHandler.sharedInstance;
                        if (callHandler == null || (callOpreationHandler = callHandler.callOpreationHandler) == null) {
                            return;
                        }
                        callOpreationHandler.userAskedToPutCallOnMute();
                    }
                };
                try {
                    if (Build.VERSION.SDK_INT < 24) {
                        BoloApplication.getApplication().registerReceiver(CallHandler.this.screenOffReceiver, new IntentFilter("android.intent.action.SCREEN_OFF"));
                    }
                    BoloApplication.getApplication().registerReceiver(CallHandler.this.screenOffReceiver, new IntentFilter("android.intent.action.USER_PRESENT"));
                } catch (Exception unused) {
                }
            }
        }).start();
    }

    public void cleanUpSmallWindowInfoForIncomingCall(boolean z) {
        SmallPopUpHandler smallPopUpHandler = this.popUpHandler;
        if (smallPopUpHandler != null) {
            if (!z) {
                smallPopUpHandler.destroyWindow();
            }
            this.popUpHandler = null;
        }
    }

    public void destoryCallHead() {
        CallHead callHead = this.callHead;
        if (callHead != null) {
            callHead.cleanUp();
            this.callHead = null;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public CallHead getCallHead(final CallModel callModel) {
        if (this.callHead == null) {
            CallHead callHead = new CallHead(BoloApplication.getApplication());
            this.callHead = callHead;
            callHead.setTapListener(new CallHead.OnTapListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.8
                @Override // com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHead.OnTapListener
                public void onReturnToCall() {
                    CallModel callModel2 = callModel;
                    CallHandler.this.parentActivityLaunchModel = new ParentActivityLaunchModel((callModel2 == null || callModel2.getCall() == null || !(callModel.getCall().getState() == 4 || callModel.getCall().getState() == 1 || callModel.getCall().getState() == 9 || callModel.getCall().getState() == 3)) ? 0 : 2, false, CallHandler.this.popUpHandler != null);
                    CallHandler.this.tempCallModelForActivity = callModel;
                    Helper.startParentCallActivity(BoloApplication.getApplication(), false);
                }

                @Override // com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHead.OnTapListener
                public void onDeclineCall() {
                    callModel.getCall().disconnect();
                }
            });
        }
        return this.callHead;
    }

    public NotificationCallHandler getNotificationCallHandler() {
        if (this.notificationCallHandler == null) {
            this.notificationCallHandler = new NotificationCallHandler(BoloApplication.getApplication());
        }
        return this.notificationCallHandler;
    }

    public void handleOutgoingOrActiveCallActivity(final CallModel callModel, boolean z) {
        startUpdateActivity(callModel, 2, z);
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.9
            @Override 
            public void run() {
                CallHandler.this.stopLED();
            }
        }).start();
        getNotificationCallHandler().createIncomingNotification(2, callModel, true, true, new NotificationCallHandler.NotificationHandler() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.10
            @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.NotificationCallHandler.NotificationHandler
            public void onActivityOpened() {
                try {
                    if (callModel != null) {
                        CallHandler.sharedInstance.getNotificationCallHandler().createIncomingNotification(2, callModel, false, false, null);
                    }
                } catch (Exception unused) {
                }
            }
        });
        if (callModel == null || !callModel.getCallType().equals(Constants.CallTypeOutgoing)) {
            return;
        }
        Utility.logEventNew(Constants.CallCategory, "Call_type_outgoing");
    }

    public void onActivityOpened() {
        cleanUpSmallWindowInfoForIncomingCall(false);
    }

    public void onSimSelected(PhoneAccountHandle phoneAccountHandle, CallModel callModel) {
        CallService callService;
        this.currentPhoneAccount = phoneAccountHandle;
        if (callModel == null && (callService = CallService.callService) != null && callService.getCalls().size() > 0) {
            Iterator<Call> it = CallService.callService.getCalls().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Call next = it.next();
                if (next.getState() != 7 && next.getState() != 10) {
                    callModel = CallModel.callModelForCall(this.calls, next);
                    break;
                }
            }
        }
        if (callModel == null) {
            return;
        }
        callModel.getCall().phoneAccountSelected(phoneAccountHandle, false);
    }

    public void openFullScreenIncomingView(CallModel callModel, boolean z) {
        startUpdateActivity(callModel, 0, z);
        cleanUpSmallWindowInfoForIncomingCall(false);
    }

    public void removeProxmitySensor() {
        try {
            PowerManager.WakeLock wakeLock = this.mProximityWakeLock;
            if (wakeLock != null && wakeLock.isHeld()) {
                this.mProximityWakeLock.release();
                this.mProximityWakeLock = null;
            }
        } catch (Exception unused) {
        }
    }

    public void showCallHead(final CallModel callModel) {
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler.11
            @Override 
            public void run() {
                CallHandler.this.getCallHead(callModel).setAsWindow();
            }
        }).run();
    }

    public void showNotificationAndScreen() {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.setFlags(268697600);
        intent.setClass(this.context, ParentCallAcitvity.class);
        PendingIntent activity = PendingIntent.getActivity(this.context, 1, intent, 0);
        Notification.Builder builder = new Notification.Builder(this.context);
        builder.setOngoing(true);
        builder.setPriority(1);
        builder.setContentIntent(activity);
        builder.setFullScreenIntent(activity, true);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Your notification title");
        builder.setContentText("Your notification content.");
        ((NotificationManager) BoloApplication.getApplication().getSystemService(NotificationManager.class)).notify("YOUR_CHANNEL_ID", 12, builder.build());
    }

    public void starLED() {
        if (PreferenceUtils.getInstance().getBoolean(Constants.LED_FLASH)) {
            FlashLight.get(this.context).blink(800, -1);
            this.isLedOn = true;
        }
    }

    public void stopLED() {
        if (this.isLedOn) {
            FlashLight.get(this.context).stopBlinking();
            FlashLight.get(this.context).blink(800, 1);
            this.isLedOn = false;
        }
    }
}
