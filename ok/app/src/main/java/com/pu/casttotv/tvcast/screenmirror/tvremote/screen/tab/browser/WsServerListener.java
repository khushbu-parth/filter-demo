package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser;

import java.util.List;

/* loaded from: classes4.dex */
public interface WsServerListener {
    void onWsServerConnChanged(List<String> list);

    void onWsServerError(int i);

    void onWsServerStatusChanged(boolean z);
}
