package com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces;

import com.colorcallscreen.colorphone.callscreen.calltheme.service.components.BoloSpeechRecognizer;
import java.util.ArrayList;


public interface RingingCallManagerDelegate {

    
    public enum SpeakUpError {
        OnSilent,
        SystemError,
        InvalidText
    }

    void onSpeakUpStarted();

    void onSpeakingEnded();

    void onSpeechRecognizerError(int i);

    void onSpeechRecognizerResult(ArrayList<String> arrayList, BoloSpeechRecognizer.SpeechRecognizerResultType speechRecognizerResultType, BoloSpeechRecognizer.SpeechRecognizerResultFrom speechRecognizerResultFrom, String str);

    void onSpeechStarted();

    void unableToStartSpeakUp(SpeakUpError speakUpError);
}
