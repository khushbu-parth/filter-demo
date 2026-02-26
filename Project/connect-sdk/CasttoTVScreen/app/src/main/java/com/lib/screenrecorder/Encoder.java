package com.lib.screenrecorder;

import java.io.IOException;

public interface Encoder {

    void prepare() throws IOException;

    void release();

    void setCallback(Callback callback);

    void stop();

    public interface Callback {
        void onError(Encoder encoder, Exception exc);
    }
}
