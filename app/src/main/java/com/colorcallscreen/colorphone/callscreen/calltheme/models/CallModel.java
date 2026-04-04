package com.colorcallscreen.colorphone.callscreen.calltheme.models;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.SystemClock;
import android.telecom.Call;
import android.telecom.TelecomManager;
import androidx.core.content.ContextCompat;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import java.util.ArrayList;
import java.util.List;


public class CallModel {
    private Call call;
    private Call.Details callDetails;
    private String name;
    private String phnNumber;
    private String sim;
    private Bitmap userImg;
    private boolean isConfressCall = false;
    private boolean isUserImgFetched = false;
    private boolean isPartOfConfressCall = false;
    private List<CallModel> childCallModel = new ArrayList();
    private String callType = Constants.CallTypeOutgoing;
    private boolean isNameFetched = false;

    public static CallModel addNewCallModel(List<CallModel> list, Call call) {
        CallModel callModelForCall = callModelForCall(list, call);
        if (callModelForCall == null) {
            callModelForCall = new CallModel();
            list.add(callModelForCall);
        }
        callModelForCall.setCall(call);
        return callModelForCall;
    }

    public static CallModel callModelForCall(List<CallModel> list, Call call) {
        for (CallModel callModel : list) {
            if (isCallModelSame(callModel, call)) {
                return callModel;
            }
            if (!callModel.getChildCallModel().isEmpty()) {
                for (CallModel callModel2 : callModel.getChildCallModel()) {
                    if (isCallModelSame(callModel2, call)) {
                        return callModel2;
                    }
                }
                continue;
            }
        }
        return null;
    }

    private static boolean isCallModelSame(CallModel callModel, Call call) {
        if (callModel.getCall() != null || callModel.getCallDetails() == null || !Utility.getPhoneNumberOfCall(callModel, BoloApplication.getApplication()).equals(Utility.numberFromCall(call))) {
            return callModel.getCall() != null && callModel.getCall().equals(call);
        }
        callModel.setCall(call);
        return true;
    }

    public static void removeNewCallModel(List<CallModel> list, Call call) {
        if (call == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            CallModel callModel = list.get(i);
            if (callModel.getCall() == null) {
                if (callModel.getCallDetails().equals(call.getDetails())) {
                    if (list.get(i).getCall() != null) {
                        list.get(i).getCall().registerCallback(null);
                    }
                    list.remove(i);
                    return;
                }
            } else if (callModel.getCall().equals(call)) {
                if (list.get(i).getCall() != null) {
                    list.get(i).getCall().registerCallback(null);
                }
                list.remove(i);
                return;
            }
        }
    }

    public long getBaseTime() {
        Call call;
        if (Build.VERSION.SDK_INT < 23 || (call = this.call) == null || call.getDetails() == null) {
            return 0L;
        }
        return SystemClock.elapsedRealtime() - (System.currentTimeMillis() - this.call.getDetails().getConnectTimeMillis());
    }

    public Call getCall() {
        return this.call;
    }

    public Call.Details getCallDetails() {
        return this.callDetails;
    }

    public String getCallType() {
        return this.callType;
    }

    public List<CallModel> getChildCallModel() {
        return this.childCallModel;
    }

    public String getName() {
        return this.name;
    }

    public String getPhnNumber() {
        return this.phnNumber;
    }

    public String getSim() {
        if (this.sim == null && this.callDetails != null) {
            TelecomManager telecomManager = (TelecomManager) BoloApplication.getApplication().getSystemService("telecom");
            if (ContextCompat.checkSelfPermission(BoloApplication.getApplication(), BoloPermission.READ_PHONE_STATE) != 0 || telecomManager.getCallCapablePhoneAccounts().size() < 2) {
                return null;
            }
            try {
                this.sim = ((TelecomManager) BoloApplication.getApplication().getSystemService("telecom")).getPhoneAccount(this.callDetails.getAccountHandle()).getLabel().toString();
            } catch (Exception unused) {
            }
        }
        String str = this.sim;
        return str == null ? "" : str;
    }

    public Bitmap getUserImg() {
        return this.userImg;
    }

    public boolean isConfressCall() {
        return this.isConfressCall;
    }

    public boolean isNameFetched() {
        return this.isNameFetched;
    }

    public boolean isPartOfConfressCall() {
        return this.isPartOfConfressCall;
    }

    public boolean isUserImgFetched() {
        return this.isUserImgFetched;
    }

    public void setCall(Call call) {
        this.callDetails = call.getDetails();
        this.call = call;
    }

    public void setCallDetails(Call.Details details) {
        this.callDetails = details;
    }

    public void setCallType(String str) {
        this.callType = str;
    }

    public void setChildCallModel(List<CallModel> list) {
        this.childCallModel = list;
    }

    public void setConfressCall(boolean z) {
        this.isConfressCall = z;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setNameFetched(boolean z) {
        this.isNameFetched = z;
    }

    public void setPartOfConfressCall(boolean z) {
        this.isPartOfConfressCall = z;
    }

    public void setPhnNumber(String str) {
        this.phnNumber = str;
    }

    public void setSim(String str) {
        this.sim = str;
    }

    public void setUserImg(Bitmap bitmap) {
        this.userImg = bitmap;
    }

    public void setUserImgFetched(boolean z) {
        this.isUserImgFetched = z;
    }

    public static CallModel addNewCallModel(List<CallModel> list, Call.Details details) {
        CallModel callModel = new CallModel();
        list.add(callModel);
        callModel.setCallDetails(details);
        return callModel;
    }
}
