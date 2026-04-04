package com.colorcallscreen.colorphone.callscreen.calltheme.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ConfereceCallAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.controller.DialerController;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.CallService;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockCallService;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PausableChronometer;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ParentCallAcitvity;
import com.colorcallscreen.colorphone.callscreen.calltheme.callRecording.CallRecordingHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.controller.VideoCallController;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.audiomanager.BoloAudioManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.BoloCallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.DialerView;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class OutgoingCallFrag extends BaseCallFragment implements View.OnClickListener, DialerView.OnKeypadClickListener {
    private LinearLayout addCallLayout;
    private LinearLayout block_layout;
    private LinearLayout bluetoohLayout;
    private LinearLayout bluetooth_circle_opt;
    private LinearLayout bluetooth_opt;
    private LinearLayout confLayout;
    private LinearLayout contactLayout;
    private LinearLayout declineBtn;
    private DialerController dialerController;
    private ImageView drop_down_icn;
    private FrameLayout frmConf;
    private FrameLayout frmLogo;
    private LinearLayout full_scrren_view;
    private LinearLayout headsetLayout;
    private LinearLayout headset_opt;
    private LinearLayout headsetcircle_opt;
    private LinearLayout holdLayout;
    AppCompatImageView ivBack;
    ImageView ivBluetoothCall;
    ImageView ivConfCall;
    ImageView ivHeadsetCall;
    ImageView ivHold;
    ImageView ivMuteCall;
    ImageView ivSpeakerCall;
    LinearLayout linConfLay;
    LinearLayout linConfView;
    private LinearLayout muteLayout;
    private ParentCallAcitvity parentActivity;
    private LinearLayout pip_liner_layout;
    private FrameLayout progressConf;
    private PausableChronometer recodingChronometer;
    private LinearLayout recordLayout;
    private LinearLayout record_circle;
    private RecyclerView rvConference;
    private LinearLayout speakerLayout;
    private View speakerOptView;
    private LinearLayout speaker_circle_opt;
    private LinearLayout speaker_opt;
    TextView txtTotalConf;
    private VideoCallController.VideoCallApps videoCallApp;
    private VideoCallController videoCallController;
    private Long videoCallId;
    private LinearLayout videoLayout;
    private CallWaitingFrag waitingController;
    private boolean isRecordingStarted = false;
    private boolean isBluetoothConnected = false;
    private boolean isCommandedForSpeaker = false;
    private CallRecordingHandler callRecordingHandler = new CallRecordingHandler();
    boolean showStartCallRecord = false;

    
    public enum ICON {
        SPEAKER,
        MUTE,
        HOLD,
        CONFERENCE,
        RECORD,
        BLUETOOTH,
        HEADSET
    }

    /* renamed from: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag$AnonymousClass1  reason: case insensitive filesystem */
    
    class RunnableC0035AnonymousClass1 implements Runnable {
        RunnableC0035AnonymousClass1() {
        }

        @Override 
        public void run() {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag.AnonymousClass1.1
                @Override 
                public void run() {
                    try {
                        if (OutgoingCallFrag.this.parent != null) {
                            OutgoingCallFrag.this.progressConf = (FrameLayout) OutgoingCallFrag.this.parent.findViewById(R.id.progress);
                            OutgoingCallFrag.this.chronometer = (Chronometer) OutgoingCallFrag.this.parent.findViewById(R.id.chronometer);
                            OutgoingCallFrag.this.frmLogo = (FrameLayout) OutgoingCallFrag.this.parent.findViewById(R.id.frmLogo);
                            OutgoingCallFrag.this.linConfView = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.linConfView);
                            OutgoingCallFrag.this.simName = (TextView) OutgoingCallFrag.this.parent.findViewById(R.id.sim_name);
                            OutgoingCallFrag.this.tvUserName = (TextView) OutgoingCallFrag.this.parent.findViewById(R.id.tvUserName);
                            OutgoingCallFrag.this.tvPhoneNo = (TextView) OutgoingCallFrag.this.parent.findViewById(R.id.tvPhoneNo);
                            OutgoingCallFrag.this.userImg = (ImageView) OutgoingCallFrag.this.parent.findViewById(R.id.user_img);
                            OutgoingCallFrag.this.tvLetterName = (TextView) OutgoingCallFrag.this.parent.findViewById(R.id.tvLetterName);
                            OutgoingCallFrag.this.declineBtn = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.decline);
                            OutgoingCallFrag.this.full_scrren_view = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.full_scrren_view);
                            OutgoingCallFrag.this.frmConf = (FrameLayout) OutgoingCallFrag.this.parent.findViewById(R.id.frmConf);
                            OutgoingCallFrag.this.pip_liner_layout = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.pip_liner_layout);
                            OutgoingCallFrag.this.drop_down_icn = (ImageView) OutgoingCallFrag.this.parent.findViewById(R.id.drop_down);
                            OutgoingCallFrag.this.pip_chronometer = (Chronometer) OutgoingCallFrag.this.parent.findViewById(R.id.pip_chronometer);
                            OutgoingCallFrag.this.pipNameTxt = (TextView) OutgoingCallFrag.this.parent.findViewById(R.id.pip_name);
                            OutgoingCallFrag.this.pipNumberTxt = (TextView) OutgoingCallFrag.this.parent.findViewById(R.id.pip_number);
                            OutgoingCallFrag.this.block_layout = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.block_lay);
                            OutgoingCallFrag.this.linConfLay = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.linConfLay);
                            OutgoingCallFrag.this.txtTotalConf = (TextView) OutgoingCallFrag.this.parent.findViewById(R.id.txtTotalConf);
                            OutgoingCallFrag.this.ivSpeakerCall = (ImageView) OutgoingCallFrag.this.parent.findViewById(R.id.ivSpeakerCall);
                            OutgoingCallFrag.this.ivMuteCall = (ImageView) OutgoingCallFrag.this.parent.findViewById(R.id.ivMuteCall);
                            OutgoingCallFrag.this.ivHold = (ImageView) OutgoingCallFrag.this.parent.findViewById(R.id.ivHold);
                            OutgoingCallFrag.this.ivConfCall = (ImageView) OutgoingCallFrag.this.parent.findViewById(R.id.ivConfCall);
                            OutgoingCallFrag.this.record_circle = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.record_circle);
                            OutgoingCallFrag.this.ivHeadsetCall = (ImageView) OutgoingCallFrag.this.parent.findViewById(R.id.ivHeadsetCall);
                            OutgoingCallFrag.this.ivBluetoothCall = (ImageView) OutgoingCallFrag.this.parent.findViewById(R.id.ivBluetoothCall);
                            OutgoingCallFrag.this.speaker_circle_opt = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.speaker_circle_opt);
                            OutgoingCallFrag.this.bluetooth_circle_opt = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.bluetooth_circle_opt);
                            OutgoingCallFrag.this.headsetcircle_opt = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.headset_circle_opt);
                            OutgoingCallFrag.this.recodingChronometer = (PausableChronometer) OutgoingCallFrag.this.parent.findViewById(R.id.record_time);
                            OutgoingCallFrag.this.recodingChronometer.setText(R.string.call_record);
                            OutgoingCallFrag.this.holdLayout = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.linearLayout);
                            OutgoingCallFrag.this.speakerLayout = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.linearLayout2);
                            OutgoingCallFrag.this.muteLayout = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.linearLayout3);
                            OutgoingCallFrag.this.contactLayout = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.linearLayout4);
                            OutgoingCallFrag.this.addCallLayout = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.linearLayout5);
                            OutgoingCallFrag.this.confLayout = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.linearLayout6);
                            OutgoingCallFrag.this.videoLayout = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.linearLayout9);
                            OutgoingCallFrag.this.recordLayout = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.linearLayout10);
                            OutgoingCallFrag.this.bluetoohLayout = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.bluetooth_layout);
                            OutgoingCallFrag.this.headsetLayout = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.headset_layout);
                            OutgoingCallFrag.this.speakerOptView = OutgoingCallFrag.this.parent.findViewById(R.id.speakerOptView);
                            OutgoingCallFrag.this.speakerOptView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag.AnonymousClass1.1.1
                                @Override 
                                public void onClick(View view) {
                                    OutgoingCallFrag.this.showSpeakerOption(false);
                                }
                            });
                            OutgoingCallFrag.this.speaker_opt = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.speaker_opt);
                            OutgoingCallFrag.this.bluetooth_opt = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.bluetooth_opt);
                            OutgoingCallFrag.this.headset_opt = (LinearLayout) OutgoingCallFrag.this.parent.findViewById(R.id.headset_opt);
                            OutgoingCallFrag.this.speaker_opt.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.bluetooth_opt.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.headset_opt.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.block_layout.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.declineBtn.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.holdLayout.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.speakerLayout.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.muteLayout.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.contactLayout.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.addCallLayout.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.confLayout.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.videoLayout.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.recordLayout.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.bluetoohLayout.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.headsetLayout.setOnClickListener(OutgoingCallFrag.this);
                            OutgoingCallFrag.this.setData(OutgoingCallFrag.this.callModel);
                            OutgoingCallFrag.this.startProximitySensor();
                            OutgoingCallFrag.this.onStateChangeOfCall(OutgoingCallFrag.this.callModel);
                            OutgoingCallFrag.this.checkAndChangeViewIcon();
                            OutgoingCallFrag.this.addBrodcastReciver(new IntentFilter(Constants.CallAudioRouteChangedBroadcast));
                            OutgoingCallFrag.this.initForVideoCallV2();
                            OutgoingCallFrag.this.checkAndSetSpeaker();
                            if (!Utility.isVirtualHomeButton()) {
                                OutgoingCallFrag.this.parent.findViewById(R.id.virtualButtonPaddingView).setVisibility(8);
                            }
                            if (Build.VERSION.SDK_INT >= 24) {
                                BlockCallService.cleanRingtone();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    
    public static class AnonymousClass11 {
        static final int[] ICONLIST;

        static {
            int[] iArr = new int[ICON.values().length];
            ICONLIST = iArr;
            try {
                iArr[ICON.SPEAKER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                ICONLIST[ICON.MUTE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                ICONLIST[ICON.HOLD.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                ICONLIST[ICON.CONFERENCE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                ICONLIST[ICON.RECORD.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                ICONLIST[ICON.BLUETOOTH.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                ICONLIST[ICON.HEADSET.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    private void changeIconColor(ICON icon, boolean z) {
        switch (AnonymousClass11.ICONLIST[icon.ordinal()]) {
            case 1:
                ImageView imageView = this.ivSpeakerCall;
                if (imageView != null) {
                    if (z) {
                        imageView.setColorFilter(ContextCompat.getColor(this.context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
                        this.speaker_circle_opt.setBackgroundResource(R.drawable.red_outline_circle);
                        this.headsetcircle_opt.setBackgroundResource(R.drawable.white_outline_circle);
                        this.bluetooth_circle_opt.setBackgroundResource(R.drawable.white_outline_circle);
                        return;
                    }
                    imageView.setColorFilter(ContextCompat.getColor(this.context, R.color.white), PorterDuff.Mode.MULTIPLY);
                    this.speaker_circle_opt.setBackgroundResource(R.drawable.white_outline_circle);
                    this.headsetcircle_opt.setBackgroundResource(R.drawable.red_outline_circle);
                    return;
                }
                return;
            case 2:
                ImageView imageView2 = this.ivMuteCall;
                if (imageView2 != null) {
                    if (z) {
                        imageView2.setColorFilter(ContextCompat.getColor(this.context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
                        return;
                    } else {
                        imageView2.setColorFilter(ContextCompat.getColor(this.context, R.color.white), PorterDuff.Mode.MULTIPLY);
                        return;
                    }
                }
                return;
            case 3:
                ImageView imageView3 = this.ivHold;
                if (imageView3 != null) {
                    if (z) {
                        imageView3.setColorFilter(ContextCompat.getColor(this.context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
                        return;
                    } else {
                        imageView3.setColorFilter(ContextCompat.getColor(this.context, R.color.white), PorterDuff.Mode.MULTIPLY);
                        return;
                    }
                }
                return;
            case 4:
                ImageView imageView4 = this.ivConfCall;
                if (imageView4 != null) {
                    if (z) {
                        imageView4.setColorFilter(ContextCompat.getColor(this.context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
                        return;
                    } else {
                        imageView4.setColorFilter(ContextCompat.getColor(this.context, R.color.white), PorterDuff.Mode.MULTIPLY);
                        return;
                    }
                }
                return;
            case 5:
                LinearLayout linearLayout = this.record_circle;
                if (linearLayout != null) {
                    if (z) {
                        linearLayout.setBackgroundResource(R.drawable.red_outline_circle);
                        return;
                    } else {
                        linearLayout.setBackgroundResource(R.drawable.white_outline_circle);
                        return;
                    }
                }
                return;
            case 6:
                ImageView imageView5 = this.ivBluetoothCall;
                if (imageView5 != null) {
                    if (z) {
                        imageView5.setColorFilter(ContextCompat.getColor(this.context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
                        this.bluetooth_circle_opt.setBackgroundResource(R.drawable.red_outline_circle);
                        this.speaker_circle_opt.setBackgroundResource(R.drawable.white_outline_circle);
                        this.headsetcircle_opt.setBackgroundResource(R.drawable.white_outline_circle);
                        return;
                    }
                    imageView5.setColorFilter(ContextCompat.getColor(this.context, R.color.white), PorterDuff.Mode.MULTIPLY);
                    this.bluetooth_circle_opt.setBackgroundResource(R.drawable.white_outline_circle);
                    return;
                }
                return;
            case 7:
                ImageView imageView6 = this.ivHeadsetCall;
                if (imageView6 != null) {
                    if (z) {
                        imageView6.setColorFilter(ContextCompat.getColor(this.context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
                        this.headsetcircle_opt.setBackgroundResource(R.drawable.red_outline_circle);
                        this.bluetooth_circle_opt.setBackgroundResource(R.drawable.white_outline_circle);
                        return;
                    }
                    imageView6.setColorFilter(ContextCompat.getColor(this.context, R.color.white), PorterDuff.Mode.MULTIPLY);
                    this.headsetcircle_opt.setBackgroundResource(R.drawable.white_outline_circle);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void checkAndChangeViewIcon() {
        if (this.callModel != null && this.callModel.getCall() != null && this.callModel.getCall().getState() == 3) {
            changeIconColor(ICON.HOLD, true);
        } else {
            changeIconColor(ICON.HOLD, false);
        }
        CallService callService = CallService.callService;
        if (callService == null || callService.getCallAudioState() == null) {
            return;
        }
        checkAndUpdateAudioRoute();
        if (CallService.callService.getCallAudioState().isMuted()) {
            changeIconColor(ICON.MUTE, true);
        } else {
            changeIconColor(ICON.MUTE, false);
        }
    }

    private void checkAndUpdateAudioRoute() {
        View view;
        CallService callService = CallService.callService;
        if (callService == null || callService.getCallAudioState() == null || (view = this.parent) == null) {
            return;
        }
        if (this.speakerLayout == null) {
            this.speakerLayout = (LinearLayout) view.findViewById(R.id.speaker_layout);
        }
        if (this.bluetoohLayout == null) {
            this.bluetoohLayout = (LinearLayout) this.parent.findViewById(R.id.bluetooth_layout);
        }
        if (this.headsetLayout == null) {
            this.headsetLayout = (LinearLayout) this.parent.findViewById(R.id.headset_layout);
        }
        if (this.drop_down_icn == null) {
            this.drop_down_icn = (ImageView) this.parent.findViewById(R.id.drop_down);
        }
        if (CallService.callService.getCallAudioState().getRoute() == 8) {
            changeIconColor(ICON.SPEAKER, true);
            LinearLayout linearLayout = this.speakerLayout;
            if (linearLayout != null) {
                linearLayout.setVisibility(0);
            }
            LinearLayout linearLayout2 = this.bluetoohLayout;
            if (linearLayout2 != null) {
                linearLayout2.setVisibility(8);
            }
        } else if (CallService.callService.getCallAudioState().getRoute() == 2) {
            changeIconColor(ICON.BLUETOOTH, true);
            this.isBluetoothConnected = true;
            ImageView imageView = this.drop_down_icn;
            if (imageView != null) {
                imageView.setVisibility(0);
            }
            LinearLayout linearLayout3 = this.bluetoohLayout;
            if (linearLayout3 != null) {
                linearLayout3.setVisibility(0);
            }
            LinearLayout linearLayout4 = this.speakerLayout;
            if (linearLayout4 != null) {
                linearLayout4.setVisibility(8);
            }
            if (this.isCommandedForSpeaker) {
                try {
                    BoloAudioManager.putOnSpeaker(BoloApplication.getApplication());
                    this.isCommandedForSpeaker = false;
                } catch (Exception unused) {
                }
            }
        } else {
            changeIconColor(ICON.SPEAKER, false);
            changeIconColor(ICON.HEADSET, true);
            if (this.isBluetoothConnected) {
                LinearLayout linearLayout5 = this.speakerLayout;
                if (linearLayout5 != null) {
                    linearLayout5.setVisibility(8);
                }
                LinearLayout linearLayout6 = this.bluetoohLayout;
                if (linearLayout6 != null) {
                    linearLayout6.setVisibility(8);
                }
                LinearLayout linearLayout7 = this.headsetLayout;
                if (linearLayout7 != null) {
                    linearLayout7.setVisibility(0);
                    return;
                }
                return;
            }
            LinearLayout linearLayout8 = this.speakerLayout;
            if (linearLayout8 != null) {
                linearLayout8.setVisibility(0);
            }
            LinearLayout linearLayout9 = this.bluetoohLayout;
            if (linearLayout9 != null) {
                linearLayout9.setVisibility(8);
            }
        }
    }

    private boolean checkAnyOtherCallIsActive(CallModel callModel) {
        for (CallModel callModel2 : CallHandler.sharedInstance.calls) {
            if (!callModel2.equals(callModel) && callModel2.getCall().getState() == 4) {
                return true;
            }
        }
        return false;
    }

    private void cleanUpRecycleView() {
        this.rvConference = null;
    }

    private CallModel getCurrentCall(CallModel callModel) {
        for (CallModel callModel2 : CallHandler.sharedInstance.calls) {
            if (!callModel2.equals(callModel)) {
                return callModel2;
            }
        }
        return null;
    }

    public void handleCallRecording() {
        CallModel callModel;
        if (Helper.isCallRecordIsSupportedByDevice()) {
            if (this.callRecordingHandler != null && Helper.shouldRecordCalAutomatic() && (callModel = this.callModel) != null && callModel.getCall() != null && this.callModel.getCall().getState() == 4) {
                startCallRecording(true);
            }
            LinearLayout linearLayout = this.block_layout;
            if (linearLayout != null) {
                linearLayout.setVisibility(4);
                return;
            }
            return;
        }
        LinearLayout linearLayout2 = this.recordLayout;
        if (linearLayout2 != null) {
            linearLayout2.setVisibility(4);
        }
        LinearLayout linearLayout3 = this.block_layout;
        if (linearLayout3 != null) {
            linearLayout3.setVisibility(4);
        }
    }

    private void removePopUpCallView(CallModel callModel, boolean z) {
        if (this.waitingController != null) {
            if (!z && callModel.equals(this.callModel)) {
                setData(this.waitingController.getCallModel());
                this.waitingController.getCallModel().getCall().unhold();
            }
            this.waitingController.showHideView(false);
            this.waitingController.cleanUp();
            this.waitingController = null;
            hideUserImg(true);
            if (callModel == null || callModel.isConfressCall()) {
                return;
            }
            initForVideoCallV2();
        }
    }

    private void setIconAudioRoute() {
        if (CallService.callService == null || CallService.callService.getCallAudioState() == null) {
            return;
        }
        if (CallService.callService.getCallAudioState().getRoute() == 8) {
            this.speakerLayout.setVisibility(0);
            this.bluetoohLayout.setVisibility(8);
        } else if (CallService.callService.getCallAudioState().getRoute() == 2) {
            this.bluetoohLayout.setVisibility(0);
            this.speakerLayout.setVisibility(8);
        } else {
            this.speakerLayout.setVisibility(0);
            this.bluetoohLayout.setVisibility(8);
        }
    }

    public void showSpeakerOption(boolean z) {
        if (z) {
            this.speakerOptView.setVisibility(0);
        } else {
            this.speakerOptView.setVisibility(8);
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x0082 -> B:28:0x009e). Please submit an issue!!! */
    public void startCallRecording(boolean z) {
        try {
            if (ContextCompat.checkSelfPermission(BoloApplication.getApplication(), BoloPermission.RECORD_AUDIO) != 0) {
                requestPermissions(new String[]{BoloPermission.RECORD_AUDIO}, 2000);
                this.showStartCallRecord = true;
                return;
            }
            CallRecordingHandler callRecordingHandler = this.callRecordingHandler;
            if (callRecordingHandler != null && callRecordingHandler.recordingStatus == CallRecordingHandler.RecordingStatus.Recording) {
                changeIconColor(ICON.RECORD, true);
            } else if (z && !Helper.isCallRecordingTermAndConditionApproved()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
                builder.setMessage(R.string.recording_tandc_msg).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag.2
                    @Override 
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Helper.onCallRecordingTermAndConditionApproved();
                        OutgoingCallFrag.this.startCallRecording(false);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag.1
                    @Override 
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create().show();
            } else {
                try {
                    if (this.callRecordingHandler.startCallRecordingForCall(this.callModel)) {
                        changeIconColor(ICON.RECORD, true);
                        this.recodingChronometer.start();
                    } else {
                        changeIconColor(ICON.RECORD, true);
                        this.recodingChronometer.setText(R.string.call_record);
                    }
                } catch (Exception e) {
                    Log.e("startCallRecording", "startCallRecording: " + e.getMessage());
                }
            }
        } catch (Exception unused) {
        }
    }

    private void startConfernceCall() {
        CallWaitingFrag callWaitingFrag;
        if (!Utility.mergeCall(this.callModel) || (callWaitingFrag = this.waitingController) == null) {
            return;
        }
        removePopUpCallView(callWaitingFrag.getCallModel(), true);
        this.progressConf.setVisibility(0);
        this.frmConf.setVisibility(0);
        this.frmLogo.setVisibility(8);
    }

    public void checkAndSetSpeaker() {
        if (BoloCallHandler.getInstance().pendingTaskAfterCallConnected.contains(Constants.PUT_CALL_ON_SPEAKER_TASK)) {
            this.isCommandedForSpeaker = true;
            BoloCallHandler.getInstance().pendingTaskAfterCallConnected.remove(Constants.PUT_CALL_ON_SPEAKER_TASK);
            try {
                new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag.3
                    @Override 
                    public void run() {
                        try {
                            BoloAudioManager.putOnSpeaker(BoloApplication.getApplication());
                        } catch (Exception unused) {
                        }
                    }
                }, 400L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void cleanUp() {
        super.cleanUp();
        this.callRecordingHandler.cleanUp();
        this.callRecordingHandler = null;
        if (CallHandler.sharedInstance != null) {
            CallHandler.sharedInstance.removeProxmitySensor();
        }
        this.parentActivity = null;
        CallWaitingFrag callWaitingFrag = this.waitingController;
        if (callWaitingFrag != null) {
            callWaitingFrag.cleanUp();
            this.waitingController = null;
        }
        cleanUpRecycleView();
    }

    public void hideUserImg(boolean z) {
        if (this.frmLogo == null) {
            return;
        }
        if (z && !this.callModel.isConfressCall()) {
            this.frmConf.setVisibility(8);
            this.frmLogo.setVisibility(0);
            return;
        }
        this.frmLogo.setVisibility(8);
        this.frmConf.setVisibility(0);
    }

    public void initForVideoCallV2() {
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag.4
            @Override 
            public void run() {
                String nameFromCall;
                VideoCallController.VideoCallApps app = VideoCallController.Settings.getApp();
                OutgoingCallFrag.this.videoCallController = new VideoCallController(BoloApplication.getApplication());
                OutgoingCallFrag.this.videoCallController.setNumber(Utility.getPhoneNumberOfCall(OutgoingCallFrag.this.callModel, BoloApplication.getApplication()));
                ArrayList arrayList = new ArrayList(Arrays.asList(VideoCallController.VideoCallApps.values()));
                arrayList.remove(app);
                arrayList.add(0, app);
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    VideoCallController.VideoCallApps videoCallApps = (VideoCallController.VideoCallApps) it.next();
                    Log.d("videoCallId===", app.getName() + "::" + videoCallApps.getName());
                    if (app.getName().equals(videoCallApps.getName())) {
                        OutgoingCallFrag.this.videoCallApp = videoCallApps;
                        Long l = null;
                        if (OutgoingCallFrag.this.callModel != null && (nameFromCall = Utility.getNameFromCall(OutgoingCallFrag.this.callModel, BoloApplication.getApplication())) != null && !nameFromCall.isEmpty()) {
                            l = OutgoingCallFrag.this.videoCallController.getVideoCallID1(nameFromCall, videoCallApps.getName());
                        }
                        Log.d("videoCallId===", l + "");
                        if (l != null) {
                            OutgoingCallFrag.this.videoCallId = l;
                            OutgoingCallFrag.this.videoCallApp = videoCallApps;
                            return;
                        }
                        return;
                    }
                }
            }
        }).start();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void initViewInstance() {
        super.initViewInstance();
        new Thread(new RunnableC0035AnonymousClass1()).start();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.parentActivity = (ParentCallAcitvity) getActivity();
    }

    public void onCallUpdated(CallModel callModel) {
        if (callModel.isPartOfConfressCall() || callModel.getCall().getParent() != null) {
            return;
        }
        if (callModel.getCall().getState() != 10 && callModel.getCall().getState() != 7) {
            if (callModel.getCall().getState() != 4 && callModel.getCall().getState() != 9) {
                if (callModel.getCall().getState() == 3) {
                    CallWaitingFrag callWaitingFrag = this.waitingController;
                    if (callWaitingFrag != null) {
                        callWaitingFrag.setData(callModel);
                    } else if (checkAnyOtherCallIsActive(callModel)) {
                        onNewCallAdded(callModel);
                        this.waitingController.setData(callModel);
                    }
                }
            } else {
                setData(callModel);
            }
        } else {
            CallWaitingFrag callWaitingFrag2 = this.waitingController;
            if (callWaitingFrag2 != null && callWaitingFrag2.getCallModel().equals(callModel)) {
                removePopUpCallView(callModel, true);
            } else if (this.waitingController != null && this.callModel.equals(callModel)) {
                removePopUpCallView(callModel, false);
            } else {
                CallModel currentCall = getCurrentCall(callModel);
                if (currentCall != null && !currentCall.equals(this.callModel)) {
                    setData(currentCall);
                }
            }
        }
        CallWaitingFrag callWaitingFrag3 = this.waitingController;
        if (callWaitingFrag3 != null) {
            callWaitingFrag3.onStateChangeOfCall(callModel);
        }
    }

    @Override 
    public void onClick(View view) {
        if (this.callModel == null || this.callModel.getCall() == null) {
            return;
        }
        if (view.equals(this.holdLayout)) {
            if (this.callModel.getCall().getState() == 3) {
                this.callModel.getCall().unhold();
                changeIconColor(ICON.HOLD, false);
                return;
            }
            this.callModel.getCall().hold();
            changeIconColor(ICON.HOLD, true);
        } else if (view.equals(this.speakerLayout)) {
            this.isCommandedForSpeaker = false;
            if (this.isBluetoothConnected) {
                showSpeakerOption(true);
                return;
            }
            CallService callService = CallService.callService;
            if (callService == null || callService.getCallAudioState() == null) {
                return;
            }
            if (CallService.callService.getCallAudioState().getRoute() == 8) {
                CallService.callService.setAudioRoute(5);
                changeIconColor(ICON.SPEAKER, false);
                return;
            }
            CallService.callService.setAudioRoute(8);
            changeIconColor(ICON.SPEAKER, true);
        } else if (view.equals(this.muteLayout)) {
            if (CallService.callService.getCallAudioState().isMuted()) {
                CallService.callService.setMuted(false);
                changeIconColor(ICON.MUTE, false);
                return;
            }
            CallService.callService.setMuted(true);
            changeIconColor(ICON.MUTE, true);
        } else if (view.equals(this.contactLayout)) {
            if (this.parentActivity != null) {
                if (this.dialerController == null) {
                    DialerController dialerController = new DialerController(this.context, true, this.parent);
                    this.dialerController = dialerController;
                    dialerController.setKeypadClickListener(this);
                }
                if (this.dialerController.isDialerVisible()) {
                    return;
                }
                this.dialerController.showDialer();
            }
        } else if (view.equals(this.addCallLayout)) {
            this.parentActivity.isForAddCall = true;
            Utility.openDialer(getContext());
            Utility.logEventNew(Constants.CallCategory, "Add_call_tapped");
        } else if (view.equals(this.confLayout)) {
            startConfernceCall();
            Utility.logEventNew(Constants.CallCategory, "Merge_call_tapped");
        } else if (this.declineBtn.equals(view)) {
            this.callModel.getCall().disconnect();
            Utility.vibrate(this.context);
        } else if (view.equals(this.videoLayout)) {
            if (this.videoCallController != null) {
                new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag.5
                    @Override 
                    public void run() {
                        if (OutgoingCallFrag.this.videoCallId != null) {
                            OutgoingCallFrag.this.videoCallController.makeVideoCall(OutgoingCallFrag.this.videoCallId, OutgoingCallFrag.this.videoCallApp);
                            OutgoingCallFrag.this.callModel.getCall().disconnect();
                            return;
                        }
                        Toast.makeText(OutgoingCallFrag.this.parentActivity, "Unknown caller is not connect video call.", 0).show();
                    }
                }, 500L);
            }
        } else if (view.equals(this.recordLayout)) {
            CallRecordingHandler callRecordingHandler = this.callRecordingHandler;
            if (callRecordingHandler != null && callRecordingHandler.recordingStatus == CallRecordingHandler.RecordingStatus.None) {
                startCallRecording(true);
                return;
            }
            CallRecordingHandler callRecordingHandler2 = this.callRecordingHandler;
            if (callRecordingHandler2 != null && callRecordingHandler2.recordingStatus == CallRecordingHandler.RecordingStatus.Pause) {
                if (this.callRecordingHandler.resumeRecording()) {
                    changeIconColor(ICON.RECORD, true);
                    this.recodingChronometer.start();
                    return;
                }
                changeIconColor(ICON.RECORD, false);
                return;
            }
            CallRecordingHandler callRecordingHandler3 = this.callRecordingHandler;
            if (callRecordingHandler3 == null || callRecordingHandler3.recordingStatus != CallRecordingHandler.RecordingStatus.Recording) {
                return;
            }
            if (this.callRecordingHandler.pauseRecording()) {
                changeIconColor(ICON.RECORD, false);
                this.recodingChronometer.stop();
                return;
            }
            changeIconColor(ICON.RECORD, true);
        } else if (view.equals(this.speaker_opt)) {
            CallService callService2 = CallService.callService;
            if (callService2 != null) {
                callService2.setAudioRoute(8);
                this.bluetoohLayout.setVisibility(8);
                this.headsetLayout.setVisibility(8);
                this.speakerLayout.setVisibility(0);
                showSpeakerOption(false);
            }
        } else if (view.equals(this.bluetooth_opt)) {
            CallService callService3 = CallService.callService;
            if (callService3 != null) {
                callService3.setAudioRoute(2);
                this.bluetoohLayout.setVisibility(0);
                this.speakerLayout.setVisibility(8);
                this.headsetLayout.setVisibility(8);
                showSpeakerOption(false);
            }
        } else if (view.equals(this.headset_opt)) {
            CallService callService4 = CallService.callService;
            if (callService4 != null) {
                callService4.setAudioRoute(5);
                this.bluetoohLayout.setVisibility(8);
                this.speakerLayout.setVisibility(8);
                this.headsetLayout.setVisibility(0);
                showSpeakerOption(false);
            }
        } else if (view.equals(this.bluetoohLayout)) {
            this.isCommandedForSpeaker = false;
            showSpeakerOption(true);
        } else if (view.equals(this.headsetLayout)) {
            this.isCommandedForSpeaker = false;
            showSpeakerOption(true);
        } else if (view.equals(this.block_layout)) {
            new SmallPopUpHandler.BlockConfirm(BoloApplication.getApplication(), this.callModel).setAsWindow();
        }
    }

    public void onConfressCallUpdated(CallModel callModel) {
        Log.e("sachin size", "" + callModel.getChildCallModel().size());
        if (callModel.getChildCallModel().isEmpty()) {
            this.linConfLay.setVisibility(8);
            this.frmConf.setVisibility(8);
            this.frmLogo.setVisibility(0);
            this.linConfView.setVisibility(8);
            this.full_scrren_view.setVisibility(0);
            cleanUpRecycleView();
            CallModel currentCall = getCurrentCall(this.callModel);
            if (currentCall == null || currentCall.equals(this.callModel)) {
                return;
            }
            setData(currentCall);
            return;
        }
        this.txtTotalConf.setText(callModel.getChildCallModel().size() + " person");
        this.linConfLay.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag.6
            @Override 
            public void onClick(View view) {
                OutgoingCallFrag.this.linConfView.setVisibility(0);
                OutgoingCallFrag.this.full_scrren_view.setVisibility(8);
            }
        });
        RecyclerView recyclerView = this.rvConference;
        if (recyclerView == null) {
            AppCompatImageView appCompatImageView = (AppCompatImageView) this.parent.findViewById(R.id.ivBack);
            this.ivBack = appCompatImageView;
            appCompatImageView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag.7
                @Override 
                public void onClick(View view) {
                    OutgoingCallFrag.this.linConfView.setVisibility(8);
                    OutgoingCallFrag.this.full_scrren_view.setVisibility(0);
                }
            });
            RecyclerView recyclerView2 = (RecyclerView) this.parent.findViewById(R.id.rvConference);
            this.rvConference = recyclerView2;
            recyclerView2.setAdapter(new ConfereceCallAdapter(this.context, callModel.getChildCallModel()));
        } else {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
        setData(callModel);
        this.linConfLay.setVisibility(0);
        this.frmConf.setVisibility(0);
        this.frmLogo.setVisibility(8);
        this.progressConf.setVisibility(8);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.parent = layoutInflater.inflate(R.layout.outgoing_call_frag, viewGroup, false);
        return this.parent;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.windowview.DialerView.OnKeypadClickListener
    public void onKeypadClick(Character ch) {
        if (this.callModel != null) {
            this.callModel.getCall().playDtmfTone(ch.charValue());
            this.callModel.getCall().stopDtmfTone();
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void onNewBroadcastRecived(Intent intent) {
        try {
            if (intent.getAction().equals(Constants.CallAudioRouteChangedBroadcast)) {
                checkAndUpdateAudioRoute();
            }
        } catch (Exception unused) {
        }
    }

    public void onNewCallAdded(CallModel callModel) {
        ParentCallAcitvity parentCallAcitvity;
        CallWaitingFrag callWaitingFrag = new CallWaitingFrag();
        this.waitingController = callWaitingFrag;
        callWaitingFrag.create(this.parent, callModel, this.context);
        this.waitingController.showHideView(true);
        hideUserImg(false);
        showVideoCall(false);
        if (Build.VERSION.SDK_INT < 26 || (parentCallAcitvity = this.parentActivity) == null || !parentCallAcitvity.isInPictureInPictureMode()) {
            return;
        }
        Helper.startParentCallActivity(BoloApplication.getApplication(), true);
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPictureInPictureModeChanged(boolean z) {
        if (z) {
            try {
                this.pip_liner_layout.setVisibility(0);
                this.full_scrren_view.setVisibility(8);
                CallWaitingFrag callWaitingFrag = this.waitingController;
                if (callWaitingFrag != null) {
                    callWaitingFrag.showHideView(false);
                }
            } catch (Exception unused) {
            }
        } else {
            LinearLayout linearLayout = this.pip_liner_layout;
            if (linearLayout != null) {
                try {
                    linearLayout.setVisibility(8);
                    Fade fade = new Fade();
                    fade.setDuration(800L);
                    fade.addTarget(this.full_scrren_view);
                    fade.addListener(new Transition.TransitionListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag.8
                        @Override // androidx.transition.Transition.TransitionListener
                        public void onTransitionCancel(Transition transition) {
                        }

                        @Override // androidx.transition.Transition.TransitionListener
                        public void onTransitionPause(Transition transition) {
                        }

                        @Override // androidx.transition.Transition.TransitionListener
                        public void onTransitionResume(Transition transition) {
                        }

                        @Override // androidx.transition.Transition.TransitionListener
                        public void onTransitionStart(Transition transition) {
                        }

                        @Override // androidx.transition.Transition.TransitionListener
                        public void onTransitionEnd(Transition transition) {
                            if (OutgoingCallFrag.this.waitingController != null) {
                                OutgoingCallFrag.this.waitingController.showHideView(true);
                            }
                        }
                    });
                    TransitionManager.beginDelayedTransition((ViewGroup) this.full_scrren_view.getParent(), fade);
                    this.full_scrren_view.setVisibility(0);
                } catch (Exception unused2) {
                    this.full_scrren_view.setVisibility(0);
                    CallWaitingFrag callWaitingFrag2 = this.waitingController;
                    if (callWaitingFrag2 != null) {
                        callWaitingFrag2.showHideView(true);
                    }
                }
            }
        }
        super.onPictureInPictureModeChanged(z);
    }

    @Override // androidx.fragment.app.Fragment
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 2000 && iArr.length > 0 && iArr[0] == 0 && this.showStartCallRecord) {
            startCallRecording(true);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.tvUserName != null) {
            checkAndChangeViewIcon();
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void onStateChangeOfCall(CallModel callModel) {
        super.onStateChangeOfCall(callModel);
        if (callModel == null || callModel.getCall() == null || !callModel.getCallType().equals(Constants.CallTypeOutgoing)) {
            return;
        }
        callModel.getCall().getState();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void setCallModel(CallModel callModel, Context context) {
        super.setCallModel(callModel, context);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void setData(CallModel callModel) {
        if (callModel == null) {
            return;
        }
        super.setData(callModel);
        if (callModel.isConfressCall()) {
            this.linConfLay.setVisibility(0);
        } else {
            this.linConfLay.setVisibility(8);
        }
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag.9
            @Override 
            public void run() {
                OutgoingCallFrag.this.checkAndChangeViewIcon();
            }
        }).run();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void setName(String str) {
        super.setName(str);
        Utility.resizeText(this.tvUserName, 30, 20);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void setUserImg(Bitmap bitmap, String str) {
        super.setUserImg(bitmap, str);
    }

    public void showVideoCall(final boolean z) {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.OutgoingCallFrag.10
            @Override 
            public void run() {
                if (OutgoingCallFrag.this.videoLayout == null || OutgoingCallFrag.this.confLayout == null) {
                    return;
                }
                if (z) {
                    OutgoingCallFrag.this.videoLayout.setVisibility(0);
                    OutgoingCallFrag.this.confLayout.setVisibility(8);
                    return;
                }
                OutgoingCallFrag.this.videoLayout.setVisibility(8);
                OutgoingCallFrag.this.confLayout.setVisibility(0);
            }
        });
    }
}
