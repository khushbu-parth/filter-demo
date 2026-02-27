package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.firetv;

public enum FireTVButtonKeyEvent {
    UP("input keyevent 19"),
    DOWN("input keyevent 20"),
    LEFT("input keyevent 21"),
    RIGHT("input keyevent 22"),
    OK("input keyevent 23"),
    BACK("input keyevent 4"),
    HOME("input keyevent 3"),
    MENU("input keyevent 82"),
    REWIND("input keyevent 89"),
    FASTFORWARD("input keyevent 90"),
    PLAYPAUSE("input keyevent 85");
    
    private final String value;

    private FireTVButtonKeyEvent(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }
}
