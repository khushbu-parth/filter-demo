package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.sony;

public enum SonyButtonKeyCode {
    Power("AAAAAQAAAAEAAAAVAw=="),
    Input("AAAAAQAAAAEAAAAlAw=="),
    SyncMenu("AAAAAgAAABoAAABYAw=="),
    Num1("AAAAAQAAAAEAAAAAAw=="),
    Num2("AAAAAQAAAAEAAAABAw=="),
    Num3("AAAAAQAAAAEAAAACAw=="),
    Num4("AAAAAQAAAAEAAAADAw=="),
    Num5("AAAAAQAAAAEAAAAEAw=="),
    Num6("AAAAAQAAAAEAAAAFAw=="),
    Num7("AAAAAQAAAAEAAAAGAw=="),
    Num8("AAAAAQAAAAEAAAAHAw=="),
    Num9("AAAAAQAAAAEAAAAIAw=="),
    Num0("AAAAAQAAAAEAAAAJAw=="),
    NumOK("AAAAAQAAAAEAAABlAw=="),
    Dot("AAAAAgAAAJcAAAAdAw=="),
    CC("AAAAAgAAAJcAAAAoAw=="),
    Red("AAAAAgAAAJcAAAAlAw=="),
    Green("AAAAAgAAAJcAAAAmAw=="),
    Yellow("AAAAAgAAAJcAAAAnAw=="),
    Blue("AAAAAgAAAJcAAAAkAw=="),
    Up("AAAAAQAAAAEAAAB0Aw=="),
    Down("AAAAAQAAAAEAAAB1Aw=="),
    Right("AAAAAQAAAAEAAAAzAw=="),
    Left("AAAAAQAAAAEAAAA0Aw=="),
    Confirm("AAAAAQAAAAEAAABlAw=="),
    Help("AAAAAgAAAMQAAABNAw=="),
    Display("AAAAAQAAAAEAAAA6Aw=="),
    Options("AAAAAgAAAJcAAAA2Aw=="),
    Back("AAAAAgAAAJcAAAAjAw=="),
    Home("AAAAAQAAAAEAAABgAw=="),
    VolumeUp("AAAAAQAAAAEAAAASAw=="),
    VolumeDown("AAAAAQAAAAEAAAATAw=="),
    Mute("AAAAAQAAAAEAAAAUAw=="),
    Audio("AAAAAQAAAAEAAAAXAw=="),
    ChannelUp("AAAAAQAAAAEAAAAQAw=="),
    ChannelDown("AAAAAQAAAAEAAAARAw=="),
    Play("AAAAAgAAAJcAAAAaAw=="),
    Pause("AAAAAgAAAJcAAAAZAw=="),
    Stop("AAAAAgAAAJcAAAAYAw=="),
    Prev("AAAAAgAAAJcAAAA8Aw=="),
    Next("AAAAAgAAAJcAAAA9Aw=="),
    FlashPlus("AAAAAgAAAJcAAAB4Aw=="),
    FlashMinus("AAAAAgAAAJcAAAB5Aw==");
    
    private final String value;

    private SonyButtonKeyCode(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }
}
