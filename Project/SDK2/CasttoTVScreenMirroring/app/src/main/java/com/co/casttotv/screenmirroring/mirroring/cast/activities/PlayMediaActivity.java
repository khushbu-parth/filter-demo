package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.ads.sdk.SdkManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.Config;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.DeviceManager;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.MediaData;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ActivityPlayMediaBinding;
import com.co.casttotv.screenmirroring.mirroring.cast.server.WebServer;
import com.connectsdk.core.MediaInfo;
import com.connectsdk.service.capability.MediaControl;
import com.connectsdk.service.capability.MediaPlayer;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.command.ServiceCommandError;

import java.io.File;
import java.io.IOException;

public class PlayMediaActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    ActivityPlayMediaBinding binding;
    WebServer webServer;

    int currentPosition = 0;
    String filePath;
    Long fileDuration;

    String mediaType = MediaData.TYPE_VIDEO;

    Handler mSeekbarUpdateHandler = new Handler();

    Boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaType = getIntent().getStringExtra(MediaData.TYPE_MEDIA_KEY);
        currentPosition = Config.selectedPosition;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_media);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        SdkManager.loadNativeBanner(this, binding.frameBanner);

        DeviceManager.getInstance().getVolume(new VolumeControl.VolumeListener() {
            @Override
            public void onSuccess(Float object) {
                binding.seekVolume.setProgress((int) (object.floatValue() * 100.0f));
            }

            @Override
            public void onError(ServiceCommandError error) {

            }
        });

        binding.seekVolume.setOnSeekBarChangeListener(this);
        setupData();

        binding.imagePlay.setOnClickListener(view -> {
            if (DeviceManager.getInstance().getMediaControl() != null) {
                if (isPlaying) {
                    DeviceManager.getInstance().getMediaControl().pause(null);
                    binding.imagePlay.setImageResource(R.drawable.btn_play);
                    isPlaying = false;
                } else {
                    DeviceManager.getInstance().getMediaControl().play(null);
                    binding.imagePlay.setImageResource(R.drawable.btn_pause);
                    isPlaying = true;
                    mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
                }
            }
        });

        binding.imagePrevious.setOnClickListener(view -> {
            int i = currentPosition - 1;
            currentPosition = i;
            if (i < 0) {
                currentPosition = Config.selectedImageFolderList.size() - 1;
            }
            setupData();
        });

        binding.imageNext.setOnClickListener(view -> {
            int i = currentPosition + 1;
            currentPosition = i;
            if (i >= Config.selectedImageFolderList.size()) {
                currentPosition = 0;
            }
            setupData();
        });
    }

    private void setupData() {
        filePath = Config.selectedImageFolderList.get(currentPosition).getPath();
        fileDuration = Config.selectedImageFolderList.get(currentPosition).getDuration();

        binding.seekTimer.setMax(fileDuration.intValue());
        binding.seekTimer.setProgress(0);
        binding.seekTimer.setOnSeekBarChangeListener(this);

        beamMedia();
    }

    public void beamMedia() {
        if (!DeviceManager.getInstance().isConnected()) {
            Toast.makeText(this, getString(R.string.device_not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressView.setVisibility(View.VISIBLE);
        if (mediaType.equals(MediaData.TYPE_VIDEO)) {
            Glide.with(getApplicationContext()).load(filePath).apply((BaseRequestOptions<?>) new RequestOptions().placeholder(R.mipmap.ic_launcher)).into(binding.imgThumbVideo);
            binding.imgThumbAudio.setVisibility(View.GONE);
            binding.imgThumbVideo.setVisibility(View.VISIBLE);
        } else {
            binding.imgThumbAudio.setVisibility(View.VISIBLE);
            binding.imgThumbVideo.setVisibility(View.GONE);
        }

        File imageFile = new File(filePath);
        binding.textTitle.setText(imageFile.getName());

        if (webServer != null) {
            webServer.closeAllConnections();
            webServer.stop();
            webServer = null;
        }
        int port = WebServer.getPort(8000, 999);
        webServer = new WebServer(filePath, port);
        try {
            webServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String finalUrl = WebServer.getLocalURLBase(this, port);
        Log.e("ImageViewActivity", "beamPhoto: " + finalUrl);

        String mediaMimeType = "video/mp4";
        if (!mediaType.equals(MediaData.TYPE_VIDEO)) {
            mediaMimeType = "audio/mpeg";
        }

        MediaInfo mediaInfo = new MediaInfo.Builder(finalUrl, mediaMimeType)
                .setTitle(imageFile.getName())
                .setDescription(finalUrl)
                .build();

        MediaPlayer.LaunchListener launchListener = new MediaPlayer.LaunchListener() {
            @Override
            public void onSuccess(MediaPlayer.MediaLaunchObject object) {
                DeviceManager.getInstance().setMediaControl(object.mediaControl);
                DeviceManager.getInstance().getMediaControl().play(null);

                binding.imagePlay.setImageResource(R.drawable.btn_pause);
                binding.progressView.setVisibility(View.GONE);
                mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
                isPlaying = true;
            }

            @Override
            public void onError(ServiceCommandError error) {

            }
        };
        if (DeviceManager.getInstance().getDevice().getCapability(MediaPlayer.class) != null) {
            ((MediaPlayer) DeviceManager.getInstance().getDevice().getCapability(MediaPlayer.class)).playMedia(mediaInfo, false, launchListener);
        }
    }

    public Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            if (DeviceManager.getInstance().getMediaControl() != null) {
                DeviceManager.getInstance().getMediaControl().getPosition(new MediaControl.PositionListener() {
                    @Override
                    public void onSuccess(Long object) {
                        binding.seekTimer.setProgress((int) (object.longValue()));

                        if (binding.seekTimer.getProgress() >= fileDuration) {
                            DeviceManager.getInstance().getMediaControl().pause(null);
                            binding.imagePlay.setImageResource(R.drawable.btn_play);
                            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
                            binding.seekTimer.setProgress(0);
                            isPlaying = false;
                        }
                    }

                    @Override
                    public void onError(ServiceCommandError error) {
                    }
                });
                mSeekbarUpdateHandler.postDelayed(this, 500);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webServer != null) {
            webServer.closeAllConnections();
            webServer.stop();
            webServer = null;
        }
        if (mSeekbarUpdateHandler != null) {
            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
        }
        isPlaying = false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar.getId() == R.id.seek_timer) {
            if (DeviceManager.getInstance().getMediaControl() != null) {
                DeviceManager.getInstance().getMediaControl().seek(seekBar.getProgress(), null);
                RequestOptions requestOptions = new RequestOptions();
                Glide.with(getApplicationContext()).asBitmap().load(filePath).apply((BaseRequestOptions<?>) requestOptions.frame(seekBar.getProgress())).into(binding.imgThumbVideo);
            }
            return;
        }
        DeviceManager.getInstance().setVolume(seekBar.getProgress() / 100.0f);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}