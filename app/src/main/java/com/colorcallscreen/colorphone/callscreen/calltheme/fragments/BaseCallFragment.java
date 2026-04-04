package com.colorcallscreen.colorphone.callscreen.calltheme.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.CallRingingManager;


public abstract class BaseCallFragment extends Fragment {
    public CallModel callModel;
    public Chronometer chronometer;
    public Context context;
    private BroadcastReceiver localBroadcastReciver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BaseCallFragment.this.onNewBroadcastRecived(intent);
        }
    };
    public View parent;
    public TextView pipLetterTxt;
    public TextView pipNameTxt;
    public TextView pipNumberTxt;
    public ImageView pipUserImg;
    public Chronometer pip_chronometer;
    public TextView simName;
    public TextView tvLetterName;
    public TextView tvPhoneNo;
    public TextView tvUserName;
    public ImageView userImg;

    public void initViewInstance() {
    }

    public void onNewBroadcastRecived(Intent intent) {
    }

    public void addBrodcastReciver(IntentFilter intentFilter) {
        try {
            LocalBroadcastManager.getInstance(BoloApplication.getApplication()).registerReceiver(this.localBroadcastReciver, intentFilter);
        } catch (Exception unused) {
        }
    }

    public void answerCall(boolean z) {
        CallModel callModel = this.callModel;
        if (callModel == null || callModel.getCall() == null) {
            return;
        }
        Helper.acceptCall(CallRingingManager.CallResponseGesture.Hand, z, this.context, this.callModel.getCall());
    }

    public void cleanUp() {
        this.parent = null;
        this.callModel = null;
        LocalBroadcastManager.getInstance(BoloApplication.getApplication()).unregisterReceiver(this.localBroadcastReciver);
    }

    public void declineCall(boolean z) {
        if (z) {
            Helper.declineCall(CallRingingManager.CallResponseGesture.Hand, this.context, this.callModel.getCall(), SettingsActivity.getAutoReplyMsg(BoloApplication.getApplication()));
        } else {
            Helper.declineCall(CallRingingManager.CallResponseGesture.Hand, this.context, this.callModel.getCall(), null);
        }
    }

    public void fetchAndUpdateUserImg() {
        final Bitmap imageOfUserCall = Utility.getImageOfUserCall(this.callModel, this.context);
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment.2
            @Override 
            public void run() {
                Bitmap bitmap = imageOfUserCall;
                if (bitmap != null) {
                    BaseCallFragment.this.setUserImg(bitmap, null);
                    return;
                }
                BaseCallFragment baseCallFragment = BaseCallFragment.this;
                baseCallFragment.setUserImg(null, baseCallFragment.getFirstLetter());
            }
        });
    }

    public CallModel getCallModel() {
        return this.callModel;
    }

    public String getFirstLetter() {
        try {
            return (getName() != null ? getName() : getNumber()).substring(0, 1);
        } catch (Exception unused) {
            return "";
        }
    }

    public String getName() {
        TextView textView = this.tvUserName;
        if (textView == null) {
            return null;
        }
        return textView.getText().toString();
    }

    public String getNumber() {
        TextView textView = this.tvPhoneNo;
        return textView == null ? "" : textView.getText().toString();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        initViewInstance();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(BoloApplication.getApplication()).unregisterReceiver(this.localBroadcastReciver);
        } catch (Exception unused) {
        }
    }

    public void onStateChangeOfCall(CallModel callModel) {
        Chronometer chronometer;
        if (callModel == null || callModel.getCall() == null || (chronometer = this.chronometer) == null) {
            return;
        }
        chronometer.stop();
        Chronometer chronometer2 = this.pip_chronometer;
        if (chronometer2 != null) {
            chronometer2.stop();
        }
        int state = callModel.getCall().getState();
        if (state == 4) {
            this.chronometer.setBase(callModel.getBaseTime());
            this.chronometer.start();
            Chronometer chronometer3 = this.pip_chronometer;
            if (chronometer3 != null) {
                chronometer3.setBase(callModel.getBaseTime());
                this.pip_chronometer.start();
                return;
            }
            return;
        }
        if (state == 9) {
            this.chronometer.setText(this.context.getResources().getString(R.string.connecting));
        } else if (state == 10) {
            this.chronometer.setText(this.context.getResources().getString(R.string.disconnecting));
        } else if (state == 1) {
            this.chronometer.setText(this.context.getResources().getString(R.string.dialing));
        } else if (state == 3) {
            this.chronometer.setText(this.context.getResources().getString(R.string.hold));
        } else if (state == 2) {
            this.chronometer.setText(this.context.getResources().getString(R.string.ringing));
        }
        Chronometer chronometer4 = this.pip_chronometer;
        if (chronometer4 != null) {
            chronometer4.setText(this.chronometer.getText());
        }
    }

    public void setCallModel(CallModel callModel, Context context) {
        this.context = context;
        this.callModel = callModel;
        onStateChangeOfCall(callModel);
    }

    public void setData(final CallModel callModel) {
        this.callModel = callModel;
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment.3
            @Override 
            public void run() {
                BaseCallFragment baseCallFragment = BaseCallFragment.this;
                baseCallFragment.setName(Utility.getNameFromCall(callModel, baseCallFragment.context));
            }
        }).run();
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment.4
            @Override 
            public void run() {
                BaseCallFragment baseCallFragment = BaseCallFragment.this;
                baseCallFragment.setNumber(Utility.getPhoneNumberOfCall(callModel, baseCallFragment.context));
            }
        }).run();
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment.5
            @Override 
            public void run() {
                BaseCallFragment.this.setSimName();
            }
        }).run();
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment.6
            @Override 
            public void run() {
                if (BaseCallFragment.this.userImg != null) {
                    BaseCallFragment.this.fetchAndUpdateUserImg();
                }
            }
        }).start();
        onStateChangeOfCall(callModel);
    }

    public void setLetter(String str) {
        TextView textView = this.tvLetterName;
        if (textView == null) {
            return;
        }
        textView.setVisibility(0);
        this.tvLetterName.setText(str);
        ImageView imageView = this.userImg;
        if (imageView != null) {
            imageView.setImageResource(R.color.orange);
        }
    }

    public void setName(String str) {
        Log.println(7, "str----", str + "==");
        TextView textView = this.tvUserName;
        if (textView == null) {
            return;
        }
        if (str == null) {
            textView.setVisibility(8);
            return;
        }
        textView.setText(str);
        new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.BaseCallFragment.7
            @Override 
            public void run() {
                try {
                    if (BaseCallFragment.this.tvUserName != null) {
                        BaseCallFragment.this.tvUserName.setSelected(true);
                    }
                } catch (Exception unused) {
                }
            }
        }, 1000L);
        TextView textView2 = this.pipNameTxt;
        if (textView2 != null) {
            textView2.setText(str);
        }
    }

    public void setNumber(String str) {
        TextView textView = this.tvPhoneNo;
        if (textView == null) {
            return;
        }
        textView.setText(str);
        TextView textView2 = this.pipNumberTxt;
        if (textView2 != null) {
            textView2.setText(str);
        }
    }

    public void setSimName() {
        if (this.simName == null) {
            return;
        }
        if (this.callModel.getSim() == null) {
            this.simName.setVisibility(8);
        } else if (this.callModel.getSim().isEmpty()) {
            this.simName.setVisibility(8);
        } else {
            this.simName.setVisibility(0);
            this.simName.setText(this.callModel.getSim());
        }
    }

    public void setUserImg(Bitmap bitmap, String str) {
        ImageView imageView = this.userImg;
        if (imageView == null) {
            return;
        }
        if (bitmap == null) {
            if (str != null) {
                setLetter(str);
                return;
            }
            return;
        }
        imageView.setVisibility(0);
        this.userImg.setImageBitmap(bitmap);
        ImageView imageView2 = this.pipUserImg;
        if (imageView2 != null) {
            imageView2.setVisibility(0);
            this.pipUserImg.setImageBitmap(bitmap);
        }
        TextView textView = this.tvLetterName;
        if (textView != null) {
            textView.setVisibility(8);
        }
        TextView textView2 = this.pipLetterTxt;
        if (textView2 != null) {
            textView2.setVisibility(8);
        }
    }

    public void startProximitySensor() {
        PowerManager.WakeLock wakeLock;
        try {
            PowerManager powerManager = (PowerManager) BoloApplication.getApplication().getSystemService("power");
            CallHandler callHandler = CallHandler.sharedInstance;
            if (callHandler != null && callHandler.mProximityWakeLock == null && powerManager != null) {
                callHandler.mProximityWakeLock = powerManager.newWakeLock(32, "bolo:Salut_ddd");
            }
            CallHandler callHandler2 = CallHandler.sharedInstance;
            if (callHandler2 != null && (wakeLock = callHandler2.mProximityWakeLock) != null && !wakeLock.isHeld()) {
                CallHandler.sharedInstance.mProximityWakeLock.acquire();
            }
        } catch (Exception e) {
            Log.e("", e.getLocalizedMessage());
        }
    }
}
