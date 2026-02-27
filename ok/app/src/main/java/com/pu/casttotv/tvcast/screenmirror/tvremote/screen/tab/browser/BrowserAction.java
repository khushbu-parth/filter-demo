package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser;

/* loaded from: classes4.dex */
public class BrowserAction {
    public static void playingAudio() {
        RxBus.getDefault().post("{\"command\":1,\"type\":2}");
    }

    public static void pauseAudio() {
        RxBus.getDefault().post("{\"command\":2,\"type\":2}");
    }

    public static void stopAudio() {
        RxBus.getDefault().post("{\"command\":3,\"type\":2}");
    }

    public static void muteAudio() {
        RxBus.getDefault().post("{\"command\":4,\"type\":2,\"volume\":0}");
    }

    public static void unMuteAudio() {
        RxBus.getDefault().post("{\"command\":4,\"type\":2,\"volume\":0}");
    }

    public static void volumeUpAudio(int i) {
        RxBus rxBus = RxBus.getDefault();
        rxBus.post("{\"command\":12,\"type\":2,\"volume\":" + i + "}");
    }

    public static void volumeDownAudio(int i) {
        RxBus rxBus = RxBus.getDefault();
        rxBus.post("{\"command\":13,\"type\":2,\"volume\":" + i + "}");
    }

    public static void seekAudio(int i) {
        RxBus rxBus = RxBus.getDefault();
        rxBus.post("{\"command\":7,\"type\":2,\"position\":" + i + "}");
    }

    public static void play() {
        RxBus.getDefault().post("{\"command\":1,\"type\":1}");
    }

    public static void pause() {
        RxBus.getDefault().post("{\"command\":2,\"type\":1}");
    }

    public static void stop() {
        RxBus.getDefault().post("{\"command\":3,\"type\":1}");
    }

    public static void mute() {
        RxBus.getDefault().post("{\"command\":4,\"type\":1,\"volume\":0}");
    }

    public static void unMute() {
        RxBus.getDefault().post("{\"command\":4,\"type\":1,\"volume\":0}");
    }

    public static void volumeUp(int i) {
        RxBus rxBus = RxBus.getDefault();
        rxBus.post("{\"command\":12,\"type\":1,\"volume\":" + i + "}");
    }

    public static void volumeDown(int i) {
        RxBus rxBus = RxBus.getDefault();
        rxBus.post("{\"command\":13,\"type\":1,\"volume\":" + i + "}");
    }

    public static void seek(int i) {
        RxBus rxBus = RxBus.getDefault();
        rxBus.post("{\"command\":7,\"type\":1,\"position\":" + i + "}");
    }

    public static void playAudio(String str) {
        RxBus rxBus = RxBus.getDefault();
        rxBus.post("{\"command\":9,\"type\":2,\"url\":\"" + str + "\"}");
    }

    public static void playVideo(String str) {
        RxBus rxBus = RxBus.getDefault();
        rxBus.post("{\"command\":9,\"type\":1,\"url\":\"" + str + "\"}");
    }

    public static void displayImage(String str, String str2, String str3, String str4) {
        RxBus rxBus = RxBus.getDefault();
        rxBus.post("{\"type\":3,\"url\":\"" + str + "\",\"height\":\"" + str2 + "\",\"width\":\"" + str3 + "\",\"orientation\":" + str4 + "}");
    }
}
