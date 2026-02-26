package com.co.casttotv.screenmirroring.mirroring.cast;

import androidx.databinding.DataBindingUtil;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ads.sdk.SdkManager;
import com.co.casttotv.screenmirroring.mirroring.cast.activities.AudioGalleryActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.activities.BaseActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.activities.ImageGalleryActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.activities.ScanningActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.activities.StartActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.DeviceManager;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.MediaData;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.PermissionUtils;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ActivityMainBinding;
import com.connectsdk.service.capability.Launcher;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.sessions.LaunchSession;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;

    @Override
    protected void onViewCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        SdkManager.loadBanner(MainActivity.this, binding.frameBanner);

        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_cast) {
                if (DeviceManager.getInstance().isConnected()) {
                    DeviceManager.showDeviceDialog(this);
                } else
                    startActivity(new Intent(this, ScanningActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            }
            return false;
        });

        binding.buttonPhotos.setOnClickListener(view -> {
            if (PermissionUtils.requestPermission(this)) {
                SdkManager.showInterstitialAd(MainActivity.this, () -> {
                    startActivity(new Intent(this, ImageGalleryActivity.class)
                            .putExtra(MediaData.TYPE_MEDIA_KEY, false)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                });
            }
        });

        binding.buttonVideos.setOnClickListener(view -> {
            if (PermissionUtils.requestPermission(this)) {
                SdkManager.showInterstitialAd(MainActivity.this, () -> {
                    startActivity(new Intent(this, ImageGalleryActivity.class)
                            .putExtra(MediaData.TYPE_MEDIA_KEY, true)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                });
            }
        });

        binding.buttonAudio.setOnClickListener(view -> {
            if (PermissionUtils.requestPermission(this)) {
                SdkManager.showInterstitialAd(MainActivity.this, () -> {
                    startActivity(new Intent(this, AudioGalleryActivity.class)
                            .putExtra(MediaData.TYPE_MEDIA_KEY, true)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                });
            }
        });

        binding.buttonYoutube.setOnClickListener(view -> {
            beamYoutube();
        });

        binding.buttonMirroring.setOnClickListener(view -> {
            if (PermissionUtils.requestPermission(this)) {
                try {
                    startActivity(new Intent("android.settings.WIFI_DISPLAY_SETTINGS"));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    try {
                        startActivity(new Intent("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG"));
                    } catch (Exception e2) {
                        try {
                            startActivity(new Intent("android.settings.CAST_SETTINGS"));
                        } catch (Exception e3) {
                            Toast.makeText(getApplicationContext(), "Device not supported", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    public void beamYoutube() {
        if (!DeviceManager.getInstance().isConnected()) {
            startActivity(new Intent(this, ScanningActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return;
        }

        String appId = DeviceManager.getInstance().YoutubeAppID();
        if (appId == null) {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            return;
        }

        Launcher.AppLaunchListener launchListener = new Launcher.AppLaunchListener() {
            @Override
            public void onSuccess(LaunchSession object) {
                Toast.makeText(MainActivity.this, "Youtube started!.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(ServiceCommandError error) {
            }
        };
        if (DeviceManager.getInstance().getDevice() != null) {
            DeviceManager.getInstance().getDevice().getLauncher().launchApp(appId, launchListener);
        }
    }

}