package com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces;


public interface RingingCallManagerDataSource {

    
    public enum CallManagerState {
        SpeakUp,
        SpeechRecognizer
    }

    int ringingModeInState(CallManagerState callManagerState);

    boolean shouldSpeakUpCallerName();

    boolean shouldSpeechRecoginzerRestartAfterFinish();

    boolean shouldStartSpeechRecognizer();

    String speakUpText();
}
