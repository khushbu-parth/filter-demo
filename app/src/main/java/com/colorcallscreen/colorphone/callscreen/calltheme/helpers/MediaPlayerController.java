package com.colorcallscreen.colorphone.callscreen.calltheme.helpers;

import android.media.MediaPlayer;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.SeekBar;
import java.io.IOException;


public class MediaPlayerController extends MediaPlayer implements SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {
    private String audioPath;
    MediaPlayerCallback callback;
    private Chronometer chronometer;
    private long pauseBase;
    private SeekBar seekBar;
    private boolean reverse = false;
    public boolean isCompleted = false;

    
    public interface MediaPlayerCallback {
        void onMediaPause();

        void onMediaPlaying(boolean z);

        void onMediaStop(boolean z);
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public MediaPlayerController() {
        setAudioStreamType(3);
        setOnCompletionListener(this);
    }

    public static MediaPlayerController getPlayer() {
        return new MediaPlayerController();
    }

    private void startChoronometer(boolean z) {
        this.chronometer.setBase(SystemClock.elapsedRealtime() - getCurrentPosition());
        this.chronometer.start();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.colorcallscreen.colorphone.callscreen.calltheme.helpers.MediaPlayerController$1] */
    private void startSeekProgress() {
        this.seekBar.setMax(getDuration());
        new Thread() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.helpers.MediaPlayerController.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                while (MediaPlayerController.this.isPlaying()) {
                    MediaPlayerController.this.seekBar.setProgress(MediaPlayerController.this.getCurrentPosition());
                }
            }
        }.start();
    }

    private void stopChronometer(boolean z) {
        Chronometer chronometer = this.chronometer;
        if (chronometer != null) {
            if (z) {
                chronometer.setBase(SystemClock.elapsedRealtime());
            }
            this.chronometer.stop();
        }
    }

    public void forceStop() {
        stop();
        stopChronometer(true);
        this.callback.onMediaStop(this.isCompleted);
    }

    public String getAudioPath() {
        return this.audioPath;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        this.isCompleted = true;
        forceStop();
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (z) {
            seekTo(i);
            startChoronometer(this.reverse);
        }
    }

    public void playPause() {
        if (isPlaying()) {
            pause();
            stopChronometer(false);
            this.callback.onMediaPause();
            return;
        }
        start();
        startSeekProgress();
        startChoronometer(this.reverse);
        this.callback.onMediaPlaying(true);
    }

    public MediaPlayerController setAudioPath(String str) {
        this.audioPath = str;
        return this;
    }

    public MediaPlayerController setCallback(MediaPlayerCallback mediaPlayerCallback) {
        this.callback = mediaPlayerCallback;
        return this;
    }

    public MediaPlayerController setChronometer(Chronometer chronometer, boolean z) {
        this.chronometer = chronometer;
        this.reverse = z;
        return this;
    }

    public MediaPlayerController setSeekBarProgress(SeekBar seekBar) {
        this.seekBar = seekBar;
        seekBar.setOnSeekBarChangeListener(this);
        return this;
    }

    public void startPlayer() {
        try {
            setDataSource(getAudioPath());
            prepare();
            start();
            startSeekProgress();
            startChoronometer(this.reverse);
            this.callback.onMediaPlaying(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.isCompleted = false;
    }
}
