package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.Globle;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adsdemo.vdapps.adsload.AdsManager;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.Ad_SplashActivity;

public class Ad_MainLoad extends AppCompatActivity {

    private static final int APP_UPDATE_REQ_CODE = 123;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdsMainload();
    }

    private void AdsMainload() {
        if (AdsManager.AllAsdStutas == 1) {
            if (AdsManager.app_updateAppDialogStatus == 1) {
                appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
                installStateUpdatedListener = state -> {
                    if (state.installStatus() == InstallStatus.INSTALLED) {
                        removeInstallStateUpdateListener();
                    }
                };

                appUpdateManager.registerListener(installStateUpdatedListener);
                com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

                appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        startUpdateFlow(appUpdateInfo);
                    }
                });
            }

        } else {
            appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
            installStateUpdatedListener = state -> {
                if (state.installStatus() == InstallStatus.INSTALLED) {
                    removeInstallStateUpdateListener();
                }
            };

            appUpdateManager.registerListener(installStateUpdatedListener);
            com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    startUpdateFlow(appUpdateInfo);
                }
            });
        }
    }
    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeInstallStateUpdateListener();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_UPDATE_REQ_CODE) {

        }
    }
}
