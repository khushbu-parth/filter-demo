package com.cast.tv.screen.mirroring.screencasting.UI.main;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseActivity;
import com.cast.tv.screen.mirroring.screencasting.Helper.AudioVisualHelper;
import com.cast.tv.screen.mirroring.screencasting.Helper.DLNAHelper;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.Observer.ConnectStatus;
import com.cast.tv.screen.mirroring.screencasting.Observer.RewardDialogEvent;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Report.ReportUtil;
import com.cast.tv.screen.mirroring.screencasting.splashExit.Second_Activity;
import com.cast.tv.screen.mirroring.screencasting.Utils.L;
import com.cast.tv.screen.mirroring.screencasting.Utils.MaxRewardUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.T;
import com.cast.tv.screen.mirroring.screencasting.Utils.net.NetStateChangeReceiver;
import com.cast.tv.screen.mirroring.screencasting.Utils.net.NetUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.net.NetworkType;
import com.cast.tv.screen.mirroring.screencasting.supportedices.HDMIActivity;
import com.cast.tv.screen.mirroring.screencasting.supportedices.HomeActivity;
import com.cast.tv.screen.mirroring.screencasting.supportedices.iuc_supporteddevice;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.library.info.CastTvAppManager;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


public class MainCastActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CHECK_SETTINGS = 214;
    SettingsClient settingsClient;
    LocationSettingsRequest locationSettingsRequest;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static void safedk_MainActivity_startActivity_01e7c8c258bb36d69606c2f836bdf877(MainCastActivity p0, Intent p1) {
        if (p1 == null) {
            return;
        }
        p0.startActivity(p1);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_cast;
    }

    @Override
    public void onCreate(Bundle bundle) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        NetStateChangeReceiver.registerReceiver(this);
        super.onCreate(bundle);
        ReportUtil.loadHomePage();

    }

    @Override
    protected void init() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(100));
        builder.setAlwaysShow(true);

        CastTvAppManager.getInstance(this).showNativeAds(this, findViewById(R.id.fl_native_banner), findViewById(R.id.native_space_img), 2);
        CastTvAppManager.getInstance(this).showBannerAds(this, findViewById(R.id.fl_banner));

        this.locationSettingsRequest = builder.build();
        this.settingsClient = LocationServices.getSettingsClient((Activity) this);

        findViewById(R.id.view_screen).setOnClickListener(this);
        findViewById(R.id.view_cast).setOnClickListener(this);
        findViewById(R.id.sub_devices).setOnClickListener(this);
        findViewById(R.id.iv_hdmi_guide).setOnClickListener(this);
        findViewById(R.id.wifi).setOnClickListener(this);
        findViewById(R.id.iv_start).setOnClickListener(this);

        setNetworkChange();
        setConnectDlnaStatus();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_screen:

                CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
                    openmirrorsetting();
                });


                return;

            case R.id.view_cast:
                CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
                    startActivity(new Intent(MainCastActivity.this, MainActivity.class));
                });


                return;

            case R.id.sub_devices:
                CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
                    startActivity(new Intent(MainCastActivity.this, iuc_supporteddevice.class));
                });


                return;

            case R.id.iv_hdmi_guide:
                CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
                    startActivity(new Intent(MainCastActivity.this, HDMIActivity.class));
                });


                return;

            case R.id.wifi:
                startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                return;

            case R.id.iv_start:
                CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
                    startActivity(new Intent(MainCastActivity.this, HomeActivity.class));
                });


                return;
            default:
                return;
        }
    }

    private void showScreenMirror() {
        showClickScreenMirror();
    }

    public void startSetting() {
        try {
            ReportUtil.clickScreenMirror();
            safedk_MainActivity_startActivity_01e7c8c258bb36d69606c2f836bdf877(MainCastActivity.this, new Intent("android.settings.CAST_SETTINGS"));
        } catch (Exception unused) {
            T.showShort(this.mContext, "The device does not support wireless projection");
        }
    }

    private void showClickScreenMirror() {
//        startSetting();

        openmirrorsetting();
    }

    private void bomImageStop() {
        FileModel value = AudioVisualHelper.mCastFileModel.getValue();
        if (value == null) {
            DLNAHelper.stop();
        } else if (value.getFileType() == 272) {
            DLNAHelper.stop();
        } else if (DLNAHelper.mConnectStatus.getValue() == ConnectStatus.PLAYING) {
            DLNAHelper.pause();
            DLNAHelper.mConnectStatus.setValue(ConnectStatus.PAUSE);
        } else {
            DLNAHelper.play();
            DLNAHelper.mConnectStatus.setValue(ConnectStatus.PLAYING);
        }
        DLNAHelper.stopPlayPhotoService();
    }


    public void openmirrorsetting() {

        this.settingsClient.checkLocationSettings(this.locationSettingsRequest).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public final void onSuccess(Object obj) {
                setCasting$9$HSMainActivity((LocationSettingsResponse) obj);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public final void onFailure(Exception exc) {
                setCasting$10$HSMainActivity(exc);
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.e("GPS", "checkLocationSettings -> onCanceled");

            }
        });


    }

    public void setCasting$9$HSMainActivity(LocationSettingsResponse locationSettingsResponse) {
        Intent intent;
        try {
            if (Build.MANUFACTURER.equals("Xiaomi")) {
                intent = new Intent("android.settings.WIRELESS_SETTINGS");
            } else {
                intent = new Intent("android.settings.WIFI_DISPLAY_SETTINGS");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                try {
                    startActivity(new Intent("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG"));
                } catch (Exception unused) {
                    startActivity(new Intent("android.settings.CAST_SETTINGS"));
                }
            } catch (Exception unused2) {
                Toast.makeText(this, "Device Not Supported", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setCasting$10$HSMainActivity(Exception exc) {
        int statusCode = ((ApiException) exc).getStatusCode();
        if (statusCode == 6) {
            try {
                ((ResolvableApiException) exc).startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException unused) {
                Log.e("===", "Unable to execute request.");
            }
        } else if (statusCode != 8502) {
            return;
        }
        Log.e("===", "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
    }


    private void setNetworkChange() {
        PermissionX.init(this).permissions("android.permission.ACCESS_FINE_LOCATION").request(new RequestCallback() {
            @Override
            public final void onResult(boolean z, List list, List list2) {
                MainCastActivity.this.setNetworkChange$3$MainActivity(z, list, list2);
            }
        });
    }

    public void setNetworkChange$3$MainActivity(boolean z, List list, List list2) {
        NetUtil.mNetworkType.observeForever(new Observer() {
            @Override
            public final void onChanged(Object obj) {
                MainCastActivity.this.null$2$MainActivity((NetworkType) obj);
            }
        });
    }

    public void null$2$MainActivity(NetworkType networkType) {
        if (DLNAHelper.isConnectDevice()) {

        }
        L.i("Main", "networkType: " + networkType.desc);


    }

    private void setConnectDlnaStatus() {
        DLNAHelper.mConnectStatus.observeForever(new Observer() {
            @Override
            public final void onChanged(Object obj) {
                // MainCastActivity.this.setConnectDlnaStatus$4$MainActivity((ConnectStatus) obj);
            }
        });
    }


    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
        L.i("Main", "onDestroy");
        NetUtil.mNetworkType.removeObservers(this);
        DLNAHelper.mConnectStatus.removeObservers(this);
        NetStateChangeReceiver.unRegisterReceiver(this);

        if (this.mHandler != null) {
            this.mHandler = null;
        }
        DLNAHelper.recycler();
    }

    public void loadRewardAd() {
        MaxRewardUtil.rewardCastNum();
    }

    @Subscribe
    public void handlerRewardEvent(RewardDialogEvent rewardDialogEvent) {
        if (rewardDialogEvent.mViewType == 1) {
            showRewardDialog();
        }
    }

    private void showRewardDialog() {
        //  ShowRewardAdDialog.newInstance().show(getSupportFragmentManager(), "Reward");
    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.exitrateus_dialoag);
        dialog.findViewById(R.id.starbtn).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName()));
                intent.addFlags(1208483840);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException unused) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
        dialog.findViewById(R.id.nobackbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CastTvAppManager.getInstance(MainCastActivity.this).showInterstitialBackAd(MainCastActivity.this, () -> {
                    startActivity(new Intent(MainCastActivity.this, Second_Activity.class));
                    finish();
                });

            }
        });
        dialog.findViewById(R.id.Canclebackbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
