package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.custom.GifWallpaper;
import com.colorcallscreen.colorphone.callscreen.calltheme.custom.VideoWallpaper;
import com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment;
import com.colorcallscreen.colorphone.callscreen.calltheme.fragments.IncomingDetailFragView;
import com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ParentActivityLaunchModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHead;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.DialerView;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;

import java.util.ArrayList;
import java.util.List;


public class ParentCallAcitvity extends FragmentActivity implements CallHead.OnTapListener {
    private BaseCallFragment callDetailFragView;
    private CallHead callHead;
    private CallModel callModel;
    public View colorView;
    public DialerView dialerView;
    private GifWallpaper gifWallpaper;
    public boolean isForAddCall;
    private VideoWallpaper videoWallpaper;
    private int stopPosition = -1;
    private boolean isInitDone = false;
    private BroadcastReceiver localBroadcastReciver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ParentCallAcitvity.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            ParentCallAcitvity.this.onNewBroadcastRecived(intent);
        }
    };
    boolean isPIPOn = false;

    private void cleanUpWakeLock() {
    }

    private boolean canEnterInPip() {
        return !this.isForAddCall && Build.VERSION.SDK_INT >= 26 && getPackageManager().hasSystemFeature("android.software.picture_in_picture");
    }

    private void cleanCallHead() {
        if (this.callHead != null) {
            destroyCallHead(false);
            this.callHead.cleanUp();
            this.callHead = null;
        }
    }

    private String getFileNameForRecording() {
        String nameFromCall = Utility.getNameFromCall(this.callModel, BoloApplication.getApplication());
        return nameFromCall != null ? nameFromCall : Utility.getPhoneNumberOfCall(this.callModel, BoloApplication.getApplication());
    }

    private ArrayList<RemoteAction> getPIPActions() {
        return new ArrayList<>();
    }

    private void initActivity(boolean z) {
        this.gifWallpaper = (GifWallpaper) findViewById(R.id.gifImageView);
        VideoWallpaper videoWallpaper = (VideoWallpaper) findViewById(R.id.live_wallpaper);
        this.videoWallpaper = videoWallpaper;
        videoWallpaper.setForCallScreen(true);
        this.colorView = findViewById(R.id.view);
        setFragment(z);
    }

    private boolean moveToPIP() {
        try {
            if (canEnterInPip()) {
                if (Build.VERSION.SDK_INT >= 26) {
                    enterPictureInPictureMode(new PictureInPictureParams.Builder().setAspectRatio(getPipRatio()).build());
                }
                return true;
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public void onNewBroadcastRecived(Intent intent) {
        try {
            if (intent.getAction().equals(Constants.PhoneInMuteBroadcast)) {
                moveTaskToBack(true);
            }
        } catch (Exception unused) {
        }
    }

    private void onPauseStopTask(boolean z) {
        if (Utility.isPhoneLocked(BoloApplication.getApplication())) {
            return;
        }
        if (z) {
            try {
                VideoWallpaper videoWallpaper = this.videoWallpaper;
                if (videoWallpaper != null && videoWallpaper.isPlaying()) {
                    this.stopPosition = this.videoWallpaper.getCurrentPosition();
                    this.videoWallpaper.pause();
                }
            } catch (Exception unused) {
                Log.e("error", "");
                return;
            }
        }
        if (isFinishing()) {
            return;
        }
        showCallHead(this);
    }

    private void removeActivity() {
        try {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
            }
        } catch (Exception unused) {
        }
        finish();
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    private void replaceFragment(BaseCallFragment baseCallFragment) {
        this.callDetailFragView = baseCallFragment;
        baseCallFragment.setCallModel(this.callModel, this);
        try {
            if (baseCallFragment instanceof OutgoingCallFrag) {
                getSupportFragmentManager().beginTransaction().replace(R.id.parent, this.callDetailFragView).commitAllowingStateLoss();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.parent, this.callDetailFragView).commitAllowingStateLoss();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Exc " + e, 0).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.parent, this.callDetailFragView).commitAllowingStateLoss();
        }
    }

    public void returnToCall() {
        try {
            Helper.pendingIntentParentCall(true, false).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    private void setFragment(boolean z) {
        List<CallModel> list;
        if (CallHandler.sharedInstance == null) {
            cleanUpAndRemoveActivity();
            return;
        }
        CallHandler callHandler = CallHandler.sharedInstance;
        if (callHandler != null && (list = callHandler.calls) != null && list.size() == 1) {
            this.callModel = CallHandler.sharedInstance.calls.get(0);
        }
        CallModel callModel = this.callModel;
        if (callModel != null) {
            CallHandler.sharedInstance.tempCallModelForActivity = callModel;
        }
        CallHandler callHandler2 = CallHandler.sharedInstance;
        ParentActivityLaunchModel parentActivityLaunchModel = callHandler2.parentActivityLaunchModel;
        if (parentActivityLaunchModel != null) {
            if (parentActivityLaunchModel.getCallStatus() == 0) {
                CallHandler callHandler3 = CallHandler.sharedInstance;
                this.callModel = callHandler3.tempCallModelForActivity;
                callHandler3.tempCallModelForActivity = null;
                setIncomingFragView(z);
            } else if (CallHandler.sharedInstance.parentActivityLaunchModel.getCallStatus() == 2) {
                CallHandler callHandler4 = CallHandler.sharedInstance;
                this.callModel = callHandler4.tempCallModelForActivity;
                callHandler4.tempCallModelForActivity = null;
                setOutgoingFragView(z);
            }
        } else {
            CallModel callModel2 = callHandler2.tempCallModelForActivity;
            if (callModel2 != null) {
                if (callModel2.getCall() != null && CallHandler.sharedInstance.tempCallModelForActivity.getCall().getState() == 2) {
                    CallHandler callHandler5 = CallHandler.sharedInstance;
                    this.callModel = callHandler5.tempCallModelForActivity;
                    callHandler5.tempCallModelForActivity = null;
                    setIncomingFragView(z);
                } else if (CallHandler.sharedInstance.tempCallModelForActivity.getCall() != null && CallHandler.sharedInstance.tempCallModelForActivity.getCall().getState() != 2) {
                    CallHandler callHandler6 = CallHandler.sharedInstance;
                    this.callModel = callHandler6.tempCallModelForActivity;
                    callHandler6.tempCallModelForActivity = null;
                    setOutgoingFragView(z);
                }
            }
        }
        try {
            Utility.setThemeBackground(this, this.callModel.getPhnNumber(), this.gifWallpaper, this.videoWallpaper);
        } catch (Exception unused) {
        }
    }

    private void setIncomingFragView(boolean z) {
        BaseCallFragment baseCallFragment;
        if (z || (baseCallFragment = this.callDetailFragView) == null || IncomingDetailFragView.class != baseCallFragment.getClass() || this.callDetailFragView.callModel != this.callModel) {
            replaceFragment(new IncomingDetailFragView());
        }
    }

    private void setOutgoingFragView(boolean z) {
        BaseCallFragment baseCallFragment;
        if (z || (baseCallFragment = this.callDetailFragView) == null || OutgoingCallFrag.class != baseCallFragment.getClass() || this.callDetailFragView.callModel != this.callModel) {
            replaceFragment(new OutgoingCallFrag());
        }
    }

    protected void addBrodcastReciver(IntentFilter intentFilter) {
        try {
            LocalBroadcastManager.getInstance(BoloApplication.getApplication()).registerReceiver(this.localBroadcastReciver, intentFilter);
        } catch (Exception unused) {
        }
    }

    public void cleanUpAndRemoveActivity() {
        this.callModel = null;
        BaseCallFragment baseCallFragment = this.callDetailFragView;
        if (baseCallFragment != null) {
            baseCallFragment.cleanUp();
            this.callDetailFragView = null;
        }
        VideoWallpaper videoWallpaper = this.videoWallpaper;
        if (videoWallpaper != null) {
            videoWallpaper.stopPlayback();
            this.videoWallpaper = null;
        }
        if (this.gifWallpaper != null) {
            this.gifWallpaper = null;
        }
        cleanCallHead();
        cleanUpWakeLock();
        removeActivity();
        CallHandler callHandler = CallHandler.sharedInstance;
        if (callHandler != null) {
            callHandler.onCallActivity = null;
        }
        try {
            LocalBroadcastManager.getInstance(BoloApplication.getApplication()).unregisterReceiver(this.localBroadcastReciver);
        } catch (Exception unused) {
        }
    }

    public void destroyCallHead(boolean z) {
        if (z && this.callHead != null) {
            new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ParentCallAcitvity.2
                @Override 
                public void run() {
                    if (ParentCallAcitvity.this.callHead != null) {
                        ParentCallAcitvity.this.callHead.finishWindow();
                    }
                }
            }).start();
            return;
        }
        CallHead callHead = this.callHead;
        if (callHead != null) {
            callHead.finishWindow();
        }
    }

    public CallHead getCallHead() {
        if (this.callHead == null) {
            CallHead callHead = new CallHead(this);
            this.callHead = callHead;
            callHead.setTapListener(this);
        }
        return this.callHead;
    }

    public Rational getPipRatio() {
        int width = getWindow().getDecorView().getWidth();
        int height = getWindow().getDecorView().getHeight();
        if (height > width) {
            return new Rational(width, height);
        }
        return new Rational(height, width);
    }

    @Override 
    public void onBackPressed() {
        DialerView dialerView = this.dialerView;
        if (dialerView == null || !dialerView.isDialerVisible()) {
            return;
        }
        this.dialerView.finishWindow();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        ParentActivityLaunchModel parentActivityLaunchModel;
        CallHandler callHandler;
        super.onCreate(bundle);
        setContentView(R.layout.test_acitvity);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().setFlags(512, 512);
        try {
            setRequestedOrientation(1);
        } catch (Exception unused) {
        }
        try {
            if (Build.VERSION.SDK_INT >= 27) {
                setShowWhenLocked(true);
                setTurnScreenOn(true);
            }
            getWindow().addFlags(524288);
            getWindow().addFlags(4194304);
            getWindow().addFlags(2097152);
            getWindow().addFlags(128);
        } catch (Exception unused2) {
        }
        try {
            if (getIntent().getBooleanExtra("fromNotification", false) && (callHandler = CallHandler.sharedInstance) != null && callHandler.calls.size() > 0) {
                CallHandler callHandler2 = CallHandler.sharedInstance;
                List<CallModel> list = callHandler2.calls;
                callHandler2.tempCallModelForActivity = list.get(list.size() - 1);
            }
        } catch (Exception unused3) {
        }
        CallHandler callHandler3 = CallHandler.sharedInstance;
        if (callHandler3 != null && (parentActivityLaunchModel = callHandler3.parentActivityLaunchModel) != null) {
            if (parentActivityLaunchModel.isFromWindow()) {
                overridePendingTransition(17432576, 17432577);
            }
            if (CallHandler.sharedInstance.parentActivityLaunchModel.isWithAnimation()) {
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        } else {
            if (getIntent().getBooleanExtra("fromWindow", false)) {
                overridePendingTransition(17432576, 17432577);
            }
            if (getIntent().getBooleanExtra("WithAnimation", true)) {
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        }
        CallHandler callHandler4 = CallHandler.sharedInstance;
        if (callHandler4 != null) {
            callHandler4.onCallActivity = this;
            callHandler4.onActivityOpened();
            try {
                findViewById(R.id.rootLayout).setKeepScreenOn(true);
                return;
            } catch (Exception unused4) {
                return;
            }
        }
        cleanUpAndRemoveActivity();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        cleanCallHead();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        cleanUpWakeLock();
        super.onPause();
        onPauseStopTask(false);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onPictureInPictureModeChanged(boolean z) {
        if (z) {
            try {
                VideoWallpaper videoWallpaper = this.videoWallpaper;
                if (videoWallpaper != null && this.stopPosition > -1 && !videoWallpaper.isPlaying()) {
                    this.videoWallpaper.post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ParentCallAcitvity.3
                        @Override 
                        public void run() {
                            if (ParentCallAcitvity.this.videoWallpaper == null || ParentCallAcitvity.this.stopPosition <= -1) {
                                return;
                            }
                            ParentCallAcitvity.this.videoWallpaper.seekTo(ParentCallAcitvity.this.stopPosition);
                            ParentCallAcitvity.this.videoWallpaper.start();
                        }
                    });
                }
                destroyCallHead(true);
                CallHandler callHandler = CallHandler.sharedInstance;
                if (callHandler != null) {
                    callHandler.destoryCallHead();
                }
            } catch (Exception unused) {
            }
        }
        super.onPictureInPictureModeChanged(z);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (!this.isInitDone) {
            addBrodcastReciver(new IntentFilter(Constants.PhoneInMuteBroadcast));
            CallHandler callHandler = CallHandler.sharedInstance;
            if (callHandler != null) {
                callHandler.destoryCallHead();
            }
            try {
                if (CallHandler.sharedInstance != null) {
                    initActivity(false);
                } else {
                    cleanUpAndRemoveActivity();
                }
            } catch (Exception unused) {
                cleanUpAndRemoveActivity();
            }
            this.isInitDone = true;
        }
        this.isForAddCall = false;
        try {
            VideoWallpaper videoWallpaper = this.videoWallpaper;
            if (videoWallpaper != null && this.stopPosition > -1 && !videoWallpaper.isPlaying()) {
                this.videoWallpaper.post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ParentCallAcitvity.4
                    @Override 
                    public void run() {
                        if (ParentCallAcitvity.this.videoWallpaper == null || ParentCallAcitvity.this.stopPosition <= -1) {
                            return;
                        }
                        ParentCallAcitvity.this.videoWallpaper.seekTo(ParentCallAcitvity.this.stopPosition);
                        ParentCallAcitvity.this.videoWallpaper.start();
                    }
                });
            }
            destroyCallHead(false);
            CallHandler callHandler2 = CallHandler.sharedInstance;
            if (callHandler2 != null) {
                callHandler2.destoryCallHead();
            }
        } catch (Exception unused2) {
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHead.OnTapListener
    public void onReturnToCall() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            returnToCall();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ParentCallAcitvity.5
                @Override 
                public void run() {
                    ParentCallAcitvity.this.returnToCall();
                }
            });
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHead.OnTapListener
    public void onDeclineCall() {
        this.callDetailFragView.callModel.getCall().disconnect();
        Utility.vibrate(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        onPauseStopTask(true);
    }

    @Override // android.app.Activity
    public void onUserLeaveHint() {
        this.isPIPOn = moveToPIP();
        super.onUserLeaveHint();
    }

    public void showCallHead(Context context) {
        if (isFinishing()) {
            return;
        }
        getCallHead().setAsWindow();
    }

    public void upateViewForNewCall(CallModel callModel, int i) {
        if (callModel == null) {
            return;
        }
        BaseCallFragment baseCallFragment = this.callDetailFragView;
        if (baseCallFragment != null && baseCallFragment.getClass() == OutgoingCallFrag.class) {
            if (callModel.getCall().getState() == 2) {
                ((OutgoingCallFrag) this.callDetailFragView).onNewCallAdded(callModel);
            } else if (CallHandler.sharedInstance.calls.size() == 2 && callModel.getCall().getState() == 1) {
                Helper.startParentCallActivity(this, true);
                ((OutgoingCallFrag) this.callDetailFragView).onNewCallAdded(callModel);
            } else {
                ((OutgoingCallFrag) this.callDetailFragView).onCallUpdated(callModel);
            }
            if (CallHandler.sharedInstance.calls.size() == 2) {
                if (callModel.getCall().getState() == 10 || callModel.getCall().getState() == 7) {
                    for (CallModel callModel2 : CallHandler.sharedInstance.calls) {
                        if (!callModel2.equals(callModel)) {
                            if (callModel2.getCall().getState() == 2) {
                                this.callModel = callModel2;
                                CallHandler.sharedInstance.tempCallModelForActivity = null;
                                setIncomingFragView(false);
                                return;
                            }
                            return;
                        }
                    }
                    return;
                }
                return;
            }
            return;
        }
        this.callModel = callModel;
        CallHandler.sharedInstance.tempCallModelForActivity = null;
        if (callModel.getCall().getState() == 4) {
            setOutgoingFragView(false);
        }
    }

    public void updateForConfressCall(CallModel callModel) {
        ((OutgoingCallFrag) this.callDetailFragView).onConfressCallUpdated(callModel);
    }
}
