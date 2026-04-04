package com.colorcallscreen.colorphone.callscreen.calltheme.windowview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.custom.CircleRippleView;
import com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.audiomanager.BoloAudioManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.CallRingingManager;

import de.hdodenhof.circleimageview.CircleImageView;


public class SmallPopUpHandler extends WindowViewer implements View.OnClickListener {
    private ImageView accept;
    private TextView alphabets;
    private LinearLayout block;
    private LinearLayout btnPannel;
    private CallModel callModel;
    private Context context;
    private ImageView decline;
    private boolean isBtnPannelVisible;
    private BroadcastReceiver localBroadcastReciver;
    private View muteArea;
    private Chronometer muteBtn;
    private TextView name;
    private TextView number;
    private LinearLayout revealEffectLayout;
    private CircleRippleView rippleView;
    private FrameLayout rootLayout;
    private ImageView silent;
    private TextView sim;
    private LinearLayout sms;
    private LinearLayout speaker;
    private ImageView tap;
    private CircleImageView userImg;
    private View view;
    private LinearLayout whatsapp;

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    protected boolean dragEnable() {
        return true;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    protected int getWindowGravity() {
        return 48;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    protected void onWindowCreated() {
    }

    
    public static class BlockConfirm extends WindowViewer {
        private TextView blockBtn;
        private CallModel callModel;
        private TextView cancelBtn;
        private Context context;
        public boolean isAlertShowing;
        private TextView msg;
        private TextView title;

        @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
        protected int getWindowGravity() {
            return 17;
        }

        public BlockConfirm(Context context, CallModel callModel) {
            super(context);
            this.isAlertShowing = false;
            this.callModel = callModel;
            this.context = context;
        }

        @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
        protected void findViews(View view) {
            this.cancelBtn = (TextView) view.findViewById(R.id.cancel);
            this.blockBtn = (TextView) view.findViewById(R.id.block);
            this.title = (TextView) view.findViewById(R.id.title);
            this.msg = (TextView) view.findViewById(R.id.message);
        }

        @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
        public void finishWindow() {
            super.finishWindow();
            this.isAlertShowing = false;
        }

        @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
        public View initView(LayoutInflater layoutInflater) {
            return layoutInflater.inflate(R.layout.confirm_block_view, (ViewGroup) null, false);
        }

        @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
        protected void onWindowCreated() {
            this.cancelBtn.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.BlockConfirm.1
                @Override 
                public void onClick(View view) {
                    BlockConfirm.this.finishWindow();
                }
            });
            this.blockBtn.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.BlockConfirm.2
                @Override 
                public void onClick(View view) {
                    Helper.declineCall(CallRingingManager.CallResponseGesture.Hand, BlockConfirm.this.context, BlockConfirm.this.callModel.getCall(), null);
                    BlockHelper.addToBlockList(BlockConfirm.this.callModel.getPhnNumber(), BlockConfirm.this.context);
                    BlockConfirm.this.finishWindow();
                }
            });
            this.title.setTypeface(Utility.getBoldAppFont(this.context));
            this.msg.setTypeface(Utility.getNormalAppFont(this.context));
        }

        @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
        public void setAsWindow() {
            super.setAsWindow();
            this.isAlertShowing = true;
        }
    }

    public SmallPopUpHandler(Context context) {
        super(context);
        this.isBtnPannelVisible = false;
        this.localBroadcastReciver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                SmallPopUpHandler.this.onNewBroadcastRecived(intent);
            }
        };
        this.context = context;
    }

    private void confirmBlock() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(this.context.getString(R.string.block_this_caller));
        builder.setMessage(this.context.getString(R.string.block_info));
        builder.setCancelable(false);
        builder.setPositiveButton(this.context.getString(R.string.block), new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.2
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                BlockHelper.addToBlockList(SmallPopUpHandler.this.getNumber(), SmallPopUpHandler.this.context);
            }
        });
        builder.setNegativeButton(this.context.getString(R.string.cancel), (DialogInterface.OnClickListener) null);
        builder.show();
    }

    public String getFirstLetter() {
        try {
            return (getName() != null ? getName() : getNumber()).substring(0, 1);
        } catch (Exception unused) {
            return "";
        }
    }

    private void handleWhatsappMessage() {
        if (Utility.sendAutoReplyWithSocialMedia(this.callModel.getCall(), SettingsActivity.getAutoReplyMsg(BoloApplication.getApplication()))) {
            Toast.makeText(BoloApplication.getApplication(), "Whatsapp sent", 0);
        } else {
            Helper.sendWhatsAppMsg(BoloApplication.getApplication(), Utility.getPhoneNumberOfCall(this.callModel, BoloApplication.getApplication()), SettingsActivity.getAutoReplyMsg(BoloApplication.getApplication()));
        }
    }

    private void moveWithReveal(final AnimatorListenerAdapter animatorListenerAdapter) {
        if (this.revealEffectLayout == null || this.params == null) {
            return;
        }
        this.revealEffectLayout.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.3
            @Override 
            public void onClick(View view) {
                Helper.startParentCallActivity(SmallPopUpHandler.this.getContext(), false);
            }
        });
        WindowManager.LayoutParams layoutParams = this.params;
        layoutParams.height = -1;
        this.windowManager.updateViewLayout(this.view, layoutParams);
        this.revealEffectLayout.post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.4
            @Override 
            public void run() {
                if (SmallPopUpHandler.this.rootLayout != null) {
                    SmallPopUpHandler.this.rootLayout.setVisibility(8);
                    Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(SmallPopUpHandler.this.revealEffectLayout, (SmallPopUpHandler.this.revealEffectLayout.getLeft() + SmallPopUpHandler.this.revealEffectLayout.getRight()) / 2, SmallPopUpHandler.this.revealEffectLayout.getTop(), 0.0f, Math.max(SmallPopUpHandler.this.revealEffectLayout.getWidth(), SmallPopUpHandler.this.revealEffectLayout.getHeight()));
                    createCircularReveal.addListener(animatorListenerAdapter);
                    SmallPopUpHandler.this.revealEffectLayout.setBackgroundColor(SmallPopUpHandler.this.context.getResources().getColor(R.color.reveal_color));
                    createCircularReveal.start();
                }
            }
        });
    }

    public void onNewBroadcastRecived(Intent intent) {
        if (!intent.getAction().equals(Constants.PhoneInMuteBroadcast) || CallHandler.sharedInstance == null) {
            return;
        }
        destroyWindow();
        CallHandler.sharedInstance.showCallHead(this.callModel);
    }

    public void openIncomingDetailActivity() {
        if (CallHandler.sharedInstance != null) {
            CallHandler.sharedInstance.openFullScreenIncomingView(this.callModel, false);
        }
    }

    public static void starZoomEffect(View view, int i) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat("scaleX", 1.2f), PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        ofPropertyValuesHolder.setDuration(800L);
        ofPropertyValuesHolder.setRepeatCount(i);
        ofPropertyValuesHolder.setRepeatMode(2);
        ofPropertyValuesHolder.start();
    }

    protected void addBrodcastReciver(IntentFilter intentFilter) {
        try {
            LocalBroadcastManager.getInstance(BoloApplication.getApplication()).registerReceiver(this.localBroadcastReciver, intentFilter);
        } catch (Exception unused) {
        }
    }

    public void blockNumber(View.OnClickListener onClickListener) {
        this.block.setOnClickListener(onClickListener);
    }

    public void cleanUp() {
        try {
            LocalBroadcastManager.getInstance(BoloApplication.getApplication()).unregisterReceiver(this.localBroadcastReciver);
        } catch (Exception unused) {
        }
        this.context = null;
        this.params = null;
        this.rootLayout = null;
        this.rippleView = null;
    }

    public void clickOnSpeakerMode(View.OnClickListener onClickListener) {
        this.speaker.setOnClickListener(onClickListener);
    }

    public void destroyWindow() {
        cleanUp();
        finishWindow();
        CallHandler.sharedInstance.cleanUpSmallWindowInfoForIncomingCall(true);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    protected void findViews(final View view) {
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.5
            @Override 
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.5.1
                    @Override 
                    public void run() {
                        SmallPopUpHandler.this.rippleView = (CircleRippleView) view.findViewById(R.id.content);
                        SmallPopUpHandler.this.rippleView.startRippleAnimation();
                        SmallPopUpHandler.this.revealEffectLayout = (LinearLayout) view.findViewById(R.id.pop_test);
                        SmallPopUpHandler.this.rootLayout = (FrameLayout) view.findViewById(R.id.pop_rootLayout);
                        SmallPopUpHandler.this.name = (TextView) view.findViewById(R.id.pop_name);
                        SmallPopUpHandler.this.muteArea = view.findViewById(R.id.mute_area);
                        SmallPopUpHandler.this.name.setTypeface(Utility.getBoldAppFont(SmallPopUpHandler.this.context));
                        SmallPopUpHandler.this.number = (TextView) view.findViewById(R.id.pop_number);
                        SmallPopUpHandler.this.number.setTypeface(Utility.getNormalAppFont(SmallPopUpHandler.this.context));
                        SmallPopUpHandler.this.sim = (TextView) view.findViewById(R.id.pop_sim_name);
                        SmallPopUpHandler.this.tap = (ImageView) view.findViewById(R.id.pop_tap);
                        SmallPopUpHandler.this.tap.setOnClickListener(SmallPopUpHandler.this);
                        SmallPopUpHandler.this.btnPannel = (LinearLayout) view.findViewById(R.id.pop_btn_pannel);
                        SmallPopUpHandler.this.silent = (ImageView) view.findViewById(R.id.pop_silent_btn);
                        SmallPopUpHandler.this.silent.setOnClickListener(SmallPopUpHandler.this);
                        SmallPopUpHandler.this.userImg = (CircleImageView) view.findViewById(R.id.pop_user_img);
                        SmallPopUpHandler.starZoomEffect(SmallPopUpHandler.this.userImg, 10);
                        SmallPopUpHandler.this.alphabets = (TextView) view.findViewById(R.id.pop_textView);
                        SmallPopUpHandler.this.sms = (LinearLayout) view.findViewById(R.id.pop_smsBtn);
                        SmallPopUpHandler.this.block = (LinearLayout) view.findViewById(R.id.pop_blockBtn);
                        SmallPopUpHandler.this.speaker = (LinearLayout) view.findViewById(R.id.pop_speakerBtn);
                        SmallPopUpHandler.this.whatsapp = (LinearLayout) view.findViewById(R.id.pop_whatsappBtn);
                        SmallPopUpHandler.this.whatsapp.setOnClickListener(SmallPopUpHandler.this);
                        if (Utility.hasWhatsappInstalled(SmallPopUpHandler.this.context)) {
                            SmallPopUpHandler.this.whatsapp.setVisibility(0);
                        } else {
                            SmallPopUpHandler.this.whatsapp.setVisibility(8);
                        }
                        SmallPopUpHandler.this.accept = (ImageView) view.findViewById(R.id.pop_accept);
                        SmallPopUpHandler.this.accept.setOnClickListener(SmallPopUpHandler.this);
                        SmallPopUpHandler.this.decline = (ImageView) view.findViewById(R.id.pop_decline);
                        SmallPopUpHandler.this.decline.setOnClickListener(SmallPopUpHandler.this);
                        SmallPopUpHandler.this.muteBtn = (Chronometer) view.findViewById(R.id.call_info);
                        SmallPopUpHandler.this.muteBtn.setText(BoloApplication.getApplication().getResources().getString(R.string.mute));
                        SmallPopUpHandler.this.muteArea.setOnClickListener(SmallPopUpHandler.this);
                        SmallPopUpHandler.this.setButtonAction();
                        if (SmallPopUpHandler.this.callModel != null) {
                            SmallPopUpHandler smallPopUpHandler = SmallPopUpHandler.this;
                            smallPopUpHandler.setCallModel(smallPopUpHandler.callModel);
                        }
                        SmallPopUpHandler.this.addBrodcastReciver(new IntentFilter(Constants.PhoneInMuteBroadcast));
                    }
                });
            }
        }).start();
    }

    public String getName() {
        return this.name.getText().toString();
    }

    public String getNumber() {
        return this.number.getText().toString();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    public View initView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.incoming_call_popup, (ViewGroup) null, false);
        this.view = inflate;
        return inflate;
    }

    @Override 
    public void onClick(View view) {
        if (view.getId() == R.id.pop_tap) {
            if (this.isBtnPannelVisible) {
                this.isBtnPannelVisible = false;
                this.btnPannel.setVisibility(8);
            } else {
                this.btnPannel.setVisibility(0);
                this.isBtnPannelVisible = true;
            }
        }
        if (view.equals(this.accept)) {
            moveWithReveal(new AnimatorListenerAdapter() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.6
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    Helper.acceptCall(CallRingingManager.CallResponseGesture.Hand, false, SmallPopUpHandler.this.context, SmallPopUpHandler.this.callModel.getCall());
                    CallHandler callHandler = CallHandler.sharedInstance;
                    if (callHandler != null) {
                        callHandler.handleOutgoingOrActiveCallActivity(SmallPopUpHandler.this.callModel, false);
                    }
                }
            });
        } else if (view.equals(this.decline)) {
            Helper.declineCall(CallRingingManager.CallResponseGesture.Hand, this.context, this.callModel.getCall(), null);
        } else if (view.equals(this.silent)) {
        } else {
            if (view.equals(this.view)) {
                moveWithReveal(new AnimatorListenerAdapter() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.7
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        SmallPopUpHandler.this.openIncomingDetailActivity();
                    }
                });
            } else if (view.equals(this.muteArea)) {
                BoloAudioManager.muteCall(BoloApplication.getApplication());
            } else if (view.equals(this.whatsapp)) {
                Helper.declineCall(CallRingingManager.CallResponseGesture.Hand, this.context, this.callModel.getCall(), null);
                handleWhatsappMessage();
            }
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    public void onWindowClick() {
        super.onWindowClick();
        moveWithReveal(new AnimatorListenerAdapter() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.8
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SmallPopUpHandler.this.openIncomingDetailActivity();
            }
        });
    }

    public void sendMessage(View.OnClickListener onClickListener) {
        this.sms.setOnClickListener(onClickListener);
    }

    public void sendWhatsApp(View.OnClickListener onClickListener) {
        this.whatsapp.setOnClickListener(onClickListener);
    }

    public void setButtonAction() {
        sendMessage(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.9
            @Override 
            public void onClick(View view) {
                Helper.declineCall(CallRingingManager.CallResponseGesture.Hand, SmallPopUpHandler.this.context, SmallPopUpHandler.this.callModel.getCall(), SettingsActivity.getAutoReplyMsg(BoloApplication.getApplication()));
            }
        });
        blockNumber(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.10
            @Override 
            public void onClick(View view) {
                new BlockConfirm(SmallPopUpHandler.this.context, SmallPopUpHandler.this.callModel).setAsWindow();
            }
        });
        clickOnSpeakerMode(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.11
            @Override 
            public void onClick(View view) {
                Helper.acceptCall(CallRingingManager.CallResponseGesture.Hand, true, SmallPopUpHandler.this.context, SmallPopUpHandler.this.callModel.getCall());
                CallHandler callHandler = CallHandler.sharedInstance;
                if (callHandler != null) {
                    callHandler.handleOutgoingOrActiveCallActivity(SmallPopUpHandler.this.callModel, false);
                }
            }
        });
    }

    public void setCallModel(final CallModel callModel) {
        this.callModel = callModel;
        if (this.name == null) {
            return;
        }
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.12
            @Override 
            public void run() {
                SmallPopUpHandler smallPopUpHandler = SmallPopUpHandler.this;
                smallPopUpHandler.setName(Utility.getNameFromCall(callModel, smallPopUpHandler.context));
            }
        }).run();
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.13
            @Override 
            public void run() {
                SmallPopUpHandler.this.setSimName(callModel.getSim());
            }
        }).run();
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.14
            @Override 
            public void run() {
                SmallPopUpHandler smallPopUpHandler = SmallPopUpHandler.this;
                smallPopUpHandler.setNumber(Utility.getPhoneNumberOfCall(callModel, smallPopUpHandler.context));
            }
        }).run();
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.15
            @Override 
            public void run() {
                final Bitmap imageOfUserCall = Utility.getImageOfUserCall(callModel, SmallPopUpHandler.this.context);
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.15.1
                    @Override 
                    public void run() {
                        if (imageOfUserCall != null) {
                            SmallPopUpHandler.this.setUserImg(imageOfUserCall, null);
                        } else {
                            SmallPopUpHandler.this.setUserImg(null, SmallPopUpHandler.this.getFirstLetter());
                        }
                    }
                });
            }
        }).start();
    }

    public void setLetter(String str) {
        this.alphabets.setVisibility(0);
        this.alphabets.setText(str);
    }

    public void setName(String str) {
        if (str == null) {
            this.name.setVisibility(8);
            return;
        }
        this.name.setText(str);
        new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler.16
            @Override 
            public void run() {
                try {
                    if (SmallPopUpHandler.this.name != null) {
                        SmallPopUpHandler.this.name.setSelected(true);
                    }
                } catch (Exception unused) {
                }
            }
        }, 1000L);
        Utility.resizeText(this.name, 20, 14);
    }

    public void setNumber(String str) {
        this.number.setText(str);
    }

    public void setSimName(String str) {
        TextView textView = this.sim;
        if (textView == null) {
            return;
        }
        if (str == null) {
            textView.setVisibility(8);
        } else if (str.isEmpty()) {
            this.sim.setVisibility(8);
        } else {
            this.sim.setVisibility(0);
            this.sim.setText(str);
        }
    }

    public void setUserImg(Bitmap bitmap, String str) {
        if (bitmap == null && str == null) {
            return;
        }
        if (str == null) {
            this.userImg.setImageBitmap(bitmap);
            this.alphabets.setVisibility(8);
        }
        if (bitmap == null) {
            setLetter(str);
        }
    }
}
