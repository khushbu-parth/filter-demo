package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser;

import java.util.List;

/* loaded from: classes4.dex */
public interface ServerServiceListener {
    void onServerStatusChanged(boolean z);

    void onWebServerError(int i);

    void onWsServerConnChanged(List<String> list);

    void onWsServerError(int i);
}
