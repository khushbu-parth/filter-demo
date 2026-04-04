package com.colorcallscreen.colorphone.callscreen.calltheme.callRecording;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;

import java.io.File;
import java.io.IOException;
import java.util.Locale;


public class CallRecordingHandler {
    private String currentFile;
    public MediaRecorder mediaRecorder;
    private String parentDir;
    public RecordingStatus recordingStatus = RecordingStatus.None;


    public enum RecordingStatus {
        None,
        Recording,
        Pause,
        Stop
    }

    private String fileNameForCall(CallModel callModel) {
        String l = Long.valueOf(System.currentTimeMillis() / 1000).toString();
        File file = Constants.CallRecordingFolder;
        if (!file.exists()) {
            file.mkdirs();
        }
        if (callModel == null || callModel.getPhnNumber() == null || callModel.getPhnNumber().isEmpty()) {
            return null;
        }
        String formatNumber = PhoneNumberUtils.formatNumber(callModel.getPhnNumber(), Locale.getDefault().getISO3Country());
        if (formatNumber == null) {
            formatNumber = callModel.getPhnNumber();
        }
        File file2 = new File(file, formatNumber);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (callModel.getName() != null && !callModel.getName().isEmpty()) {
            formatNumber = callModel.getName();
        }
        String str = file2.getAbsolutePath() + File.separator + formatNumber + "_" + l + "_" + callModel.getCallType() + "_favbol0.amr";
        this.currentFile = str;
        this.parentDir = file2.getAbsolutePath();
        return str;
    }

    public void cleanUp() {
        stopCallRecording();
        this.recordingStatus = RecordingStatus.None;
        this.parentDir = null;
        this.currentFile = null;
    }

    public boolean pauseRecording() {
        MediaRecorder mediaRecorder = this.mediaRecorder;
        if (mediaRecorder == null || Build.VERSION.SDK_INT < 24) {
            return false;
        }
        try {
            mediaRecorder.pause();
            this.recordingStatus = RecordingStatus.Pause;
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean resumeRecording() {
        MediaRecorder mediaRecorder = this.mediaRecorder;
        if (mediaRecorder == null || Build.VERSION.SDK_INT < 24) {
            return false;
        }
        try {
            mediaRecorder.resume();
            this.recordingStatus = RecordingStatus.Recording;
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean startCallRecordingForCall(CallModel callModel) {
        String fileNameForCall;
        if (callModel == null || this.mediaRecorder != null || (fileNameForCall = fileNameForCall(callModel)) == null) {
            return false;
        }
        if (Helper.shouldUseMediaRecorder()) {
            MediaRecorder mediaRecorder = new MediaRecorder();
            this.mediaRecorder = mediaRecorder;
            mediaRecorder.setAudioSource(4);
            this.mediaRecorder.setOutputFormat(0);
            this.mediaRecorder.setAudioEncoder(0);
            this.mediaRecorder.setOutputFile(fileNameForCall);
            try {
                this.mediaRecorder.prepare();
                this.mediaRecorder.start();
                this.recordingStatus = RecordingStatus.Recording;
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                this.recordingStatus = RecordingStatus.None;
                return false;
            }
        }
        return true;
    }

    public void stopCallRecording() {
        MediaRecorder mediaRecorder = this.mediaRecorder;
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                this.mediaRecorder.reset();
                this.mediaRecorder.release();
            } catch (Exception unused) {
            }
            this.mediaRecorder = null;
        }
        if (this.parentDir == null || this.currentFile == null) {
            return;
        }
        Intent intent = new Intent(Constants.OnNewCallRecordingAdded);
        intent.putExtra("parent", this.parentDir);
        intent.putExtra("current", this.currentFile);
        LocalBroadcastManager.getInstance(BoloApplication.getApplication()).sendBroadcast(intent);
    }
}
