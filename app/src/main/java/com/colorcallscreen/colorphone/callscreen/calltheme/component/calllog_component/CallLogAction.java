package com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component;


public interface CallLogAction {
    void onBlock(String str);

    void onCall(String str);

    void onMessage(String str);

    void onUnblock(String str);

    void sendWhatsAppMsg(String str, String str2);
}
