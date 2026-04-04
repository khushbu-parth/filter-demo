package com.colorcallscreen.colorphone.callscreen.calltheme.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.AcceptDeclineCallManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.CallRingingManager;


public class IncomingDetailFragView extends BaseCallFragment {
    private ImageView iVCallAccept;
    private ImageView iVCallDecline;
    private boolean isCallEventLogged = false;
    private LinearLayout ivMsgReply;

    public void answerCall() {
        answerCall(false);
        if (this.isCallEventLogged) {
            return;
        }
        Utility.logEventNew(Constants.CallCategory, "Call_accept_hand");
        this.isCallEventLogged = true;
    }

    public void declineCall() {
        declineCall(false);
        if (this.isCallEventLogged) {
            return;
        }
        Utility.logEventNew(Constants.CallCategory, "Call_reject_hand");
        this.isCallEventLogged = true;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void cleanUp() {
        super.cleanUp();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void initViewInstance() {
        super.initViewInstance();
        try {
            if (this.parent != null) {
                this.iVCallAccept = (ImageView) this.parent.findViewById(R.id.iVCallAccept);
                this.iVCallDecline = (ImageView) this.parent.findViewById(R.id.iVCallDecline);
                this.ivMsgReply = (LinearLayout) this.parent.findViewById(R.id.ivMsgReply);
                this.tvLetterName = (TextView) this.parent.findViewById(R.id.tvLetterName);
                this.simName = (TextView) this.parent.findViewById(R.id.sim_name);
                this.tvUserName = (TextView) this.parent.findViewById(R.id.tvUserName);
                this.tvPhoneNo = (TextView) this.parent.findViewById(R.id.tvPhoneNo);
                this.userImg = (ImageView) this.parent.findViewById(R.id.user_img);
                if (this.callModel != null) {
                    setData(this.callModel);
                }
                this.iVCallAccept.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.IncomingDetailFragView.1
                    @Override 
                    public void onClick(View view) {
                        Utility.vibrate(IncomingDetailFragView.this.context);
                        IncomingDetailFragView.this.answerCall();
                    }
                });
                this.iVCallDecline.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.IncomingDetailFragView.2
                    @Override 
                    public void onClick(View view) {
                        Utility.vibrate(IncomingDetailFragView.this.context);
                        IncomingDetailFragView.this.declineCall();
                    }
                });
                this.ivMsgReply.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.IncomingDetailFragView.3
                    @Override 
                    public void onClick(View view) {
                        IncomingDetailFragView.this.declineCall(true);
                        if (IncomingDetailFragView.this.isCallEventLogged) {
                            return;
                        }
                        Utility.logEventNew(Constants.CallCategory, "Call_sms_hand");
                        IncomingDetailFragView.this.isCallEventLogged = true;
                    }
                });
            }
        } catch (Exception unused) {
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.parent = layoutInflater.inflate(R.layout.incoming_call_new_ui, viewGroup, false);
        return this.parent;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void setCallModel(CallModel callModel, Context context) {
        this.context = context;
        this.callModel = callModel;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void setData(CallModel callModel) {
        if (callModel == null) {
            return;
        }
        super.setData(callModel);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void setName(String str) {
        super.setName(str);
        Utility.resizeText(this.tvUserName, 30, 14);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void setNumber(String str) {
        super.setNumber(str);
        this.tvPhoneNo.setText(str);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void setUserImg(Bitmap bitmap, String str) {
        if (bitmap == null && str == null) {
            return;
        }
        if (str == null) {
            this.userImg.setImageBitmap(bitmap);
            this.tvLetterName.setVisibility(8);
        }
        if (bitmap == null) {
            setLetter(str);
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void answerCall(boolean z) {
        if (this.callModel != null && this.callModel.getCall() != null) {
            Helper.acceptCall(CallRingingManager.CallResponseGesture.Hand, z, this.context, this.callModel.getCall());
        } else {
            AcceptDeclineCallManager.acceptCall(z, this.context, null);
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment
    public void declineCall(boolean z) {
        Call call = this.callModel != null ? this.callModel.getCall() : null;
        if (z) {
            Helper.declineCall(CallRingingManager.CallResponseGesture.Hand, this.context, call, SettingsActivity.getAutoReplyMsg(BoloApplication.getApplication()));
        } else {
            Helper.declineCall(CallRingingManager.CallResponseGesture.Hand, this.context, call, null);
        }
    }
}
