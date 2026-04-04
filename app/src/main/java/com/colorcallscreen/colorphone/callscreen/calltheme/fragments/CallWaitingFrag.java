package com.colorcallscreen.colorphone.callscreen.calltheme.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.SmallPopUpHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;


public class CallWaitingFrag extends BaseCallFragment implements View.OnClickListener {
    private ImageView mute_icn;
    private TextView newCallInfo;
    private View newCallView;
    private ImageView pop_accept;
    private ImageView pop_decline;
    private FrameLayout swap_icn;

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void cleanUp() {
        super.cleanUp();
        this.parent = null;
    }

    public void create(View view, CallModel callModel, Context context) {
        this.parent = view;
        if (view != null) {
            setCallModel(callModel, context);
            initViewInstance();
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void initViewInstance() {
        Activity activity;
        if (this.parent == null) {
            return;
        }
        this.simName = (TextView) this.parent.findViewById(R.id.pop_sim_name);
        this.simName.setVisibility(8);
        FrameLayout frameLayout = (FrameLayout) this.parent.findViewById(R.id.swap_icn);
        this.swap_icn = frameLayout;
        frameLayout.setOnClickListener(this);
        ImageView imageView = (ImageView) this.parent.findViewById(R.id.mute_icn);
        this.mute_icn = imageView;
        imageView.setVisibility(8);
        this.tvUserName = (TextView) this.parent.findViewById(R.id.pop_name);
        ImageView imageView2 = (ImageView) this.parent.findViewById(R.id.pop_accept);
        this.pop_accept = imageView2;
        imageView2.setOnClickListener(this);
        ImageView imageView3 = (ImageView) this.parent.findViewById(R.id.pop_decline);
        this.pop_decline = imageView3;
        imageView3.setOnClickListener(this);
        View findViewById = this.parent.findViewById(R.id.newCallView);
        this.newCallView = findViewById;
        findViewById.setVisibility(0);
        TextView textView = (TextView) this.parent.findViewById(R.id.call_info);
        this.newCallInfo = textView;
        textView.setVisibility(0);
        setData(this.callModel);
        SmallPopUpHandler.starZoomEffect(this.userImg, -1);
        showHideRequiredButtons();
        try {
            if (Build.VERSION.SDK_INT >= 28 && (activity = (Activity) this.context) != null && activity.getWindow().getDecorView().getRootWindowInsets().getDisplayCutout() != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.newCallView.getLayoutParams();
                marginLayoutParams.setMargins(0, Helper.getDP(this.context, 24), 0, 0);
                this.newCallView.setLayoutParams(marginLayoutParams);
            }
        } catch (Exception unused) {
        }
    }

    @Override 
    public void onClick(View view) {
        if (view.equals(this.pop_accept)) {
            answerCall(false);
            this.pop_accept.setVisibility(8);
            this.newCallInfo.setText(BoloApplication.getApplication().getString(R.string.hold));
            this.swap_icn.setVisibility(0);
        } else if (view.equals(this.swap_icn)) {
            this.callModel.getCall().unhold();
        } else if (!view.equals(this.pop_decline) || this.callModel == null) {
        } else {
            if (this.callModel.getCall().getState() == 2) {
                declineCall(false);
            } else {
                this.callModel.getCall().disconnect();
            }
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void onStateChangeOfCall(CallModel callModel) {
        super.onStateChangeOfCall(callModel);
        callModel.getCall().getState();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void setData(CallModel callModel) {
        super.setData(callModel);
        String nameFromCall = Utility.getNameFromCall(callModel, this.context);
        if (nameFromCall != null && !nameFromCall.equals(this.context.getString(R.string.unknown))) {
            this.tvUserName.setText(nameFromCall);
        } else {
            this.tvUserName.setText(Utility.getPhoneNumberOfCall(callModel, this.context));
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void setName(String str) {
        super.setName(str);
        Utility.resizeText(this.tvUserName, 20, 14);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void setSimName() {
        this.simName.setVisibility(8);
    }

    public void showHideRequiredButtons() {
        CallModel callModel = this.callModel;
        if (callModel != null && callModel.getCall().getState() == 2) {
            this.pop_accept.setVisibility(0);
            this.swap_icn.setVisibility(8);
            this.newCallInfo.setText(BoloApplication.getApplication().getString(R.string.call_waiting));
            return;
        }
        CallModel callModel2 = this.callModel;
        if (callModel2 == null || callModel2.getCall().getState() != 1) {
            return;
        }
        this.pop_accept.setVisibility(8);
        this.newCallInfo.setText(BoloApplication.getApplication().getString(R.string.hold));
        this.swap_icn.setVisibility(0);
    }

    public void showHideView(boolean z) {
        if (z) {
            this.newCallView.setVisibility(0);
        } else {
            this.newCallView.setVisibility(8);
        }
    }
}
