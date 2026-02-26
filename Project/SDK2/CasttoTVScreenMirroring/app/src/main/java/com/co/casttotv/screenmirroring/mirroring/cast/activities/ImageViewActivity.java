package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ads.sdk.SdkManager;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.adapters.ImageViewAdapter;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.Config;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.DeviceManager;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ActivityImageViewBinding;
import com.co.casttotv.screenmirroring.mirroring.cast.server.WebServer;
import com.connectsdk.core.MediaInfo;
import com.connectsdk.service.capability.MediaPlayer;
import com.connectsdk.service.command.ServiceCommandError;

import java.io.File;
import java.io.IOException;

public class ImageViewActivity extends BaseActivity {
    ActivityImageViewBinding binding;
    WebServer webServer;

    @Override
    protected void onViewCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_view);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        binding.viewpager.setAdapter(new ImageViewAdapter(this, Config.selectedImageFolderList));
        binding.viewpager.setCurrentItem(Config.selectedPosition);

        SdkManager.loadBanner(this, binding.bannerView);

        binding.viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                beamPhoto(Config.selectedImageFolderList.get(position).getPath());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        beamPhoto(Config.selectedImageFolderList.get(Config.selectedPosition).getPath());
    }

    public void beamPhoto(String filePath) {
        if (!DeviceManager.getInstance().isConnected()) {
            Toast.makeText(this, getString(R.string.device_not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        File imageFile = new File(filePath);
        binding.toolbar.setTitle(imageFile.getName());

        if (webServer != null) {
            webServer.closeAllConnections();
            webServer.stop();
            webServer = null;
        }
        int port = WebServer.getPort(8000, 999);
        webServer = new WebServer(imageFile.getAbsolutePath(), port);
        try {
            webServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String finalUrl = WebServer.getLocalURLBase(this, port);
        Log.e("ImageViewActivity", "beamPhoto: " + finalUrl);
        MediaInfo mediaInfo = new MediaInfo.Builder(finalUrl, "image/jpeg")
                .setTitle(imageFile.getName())
                .setDescription(finalUrl)
                .build();

        MediaPlayer.LaunchListener launchListener = new MediaPlayer.LaunchListener() {
            @Override
            public void onSuccess(MediaPlayer.MediaLaunchObject object) {
                DeviceManager.getInstance().setMediaControl(object.mediaControl);
            }

            @Override
            public void onError(ServiceCommandError error) {

            }
        };
        if (DeviceManager.getInstance().getDevice().getCapability(MediaPlayer.class) != null) {
            ((MediaPlayer) DeviceManager.getInstance().getDevice().getCapability(MediaPlayer.class)).displayImage(mediaInfo, launchListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webServer != null) {
            webServer.closeAllConnections();
            webServer.stop();
        }
    }
}