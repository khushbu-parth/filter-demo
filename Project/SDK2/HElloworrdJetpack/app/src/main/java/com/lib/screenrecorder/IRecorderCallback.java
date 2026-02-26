package com.lib.screenrecorder;

import android.media.MediaCodec;

public interface IRecorderCallback {
    void onDestroyRecord();

    void onMuxAudio(byte[] bArr, int i, int i2, MediaCodec.BufferInfo bufferInfo);

    void onMuxVideo(byte[] bArr, int i, int i2, MediaCodec.BufferInfo bufferInfo);

    void onPrepareRecord();

    void onRecording(long j);

    void onStartRecord();

    void onStopRecord(Throwable th);
}
