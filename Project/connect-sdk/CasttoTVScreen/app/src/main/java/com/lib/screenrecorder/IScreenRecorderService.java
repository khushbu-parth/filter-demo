package com.lib.screenrecorder;

import android.media.projection.MediaProjection;

public interface IScreenRecorderService {
    void destroyRecorder();

    AudioEncodeConfig getAudioEncodeConfig();

    String getSavingFilePath();

    VideoEncodeConfig getVideoEncodeConfig();

    boolean hasPrepared();

    void prepareAndStartRecorder(MediaProjection mediaProjection, VideoEncodeConfig videoEncodeConfig, AudioEncodeConfig audioEncodeConfig);

    void registerRecorderCallback(IRecorderCallback iRecorderCallback);

    void startRecorder();

    void startRecorder(VideoEncodeConfig videoEncodeConfig, AudioEncodeConfig audioEncodeConfig);

    void stopRecorder();
}
