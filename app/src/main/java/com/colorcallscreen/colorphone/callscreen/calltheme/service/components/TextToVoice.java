package com.colorcallscreen.colorphone.callscreen.calltheme.service.components;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import java.util.Locale;


public class TextToVoice implements TextToSpeech.OnInitListener {
    private Context context;
    private TextToSpeech speech;
    private String textToSpeak;
    private TextToVoiceReciver textToVoiceReciver;


    public interface TextToVoiceReciver {
        void onSpeakingCaughtError(TextToVoice textToVoice);

        void onSpeakingEnd(TextToVoice textToVoice);

        void onSpeakingStarted(TextToVoice textToVoice);
    }

    public TextToVoice(Context context, String str, TextToVoiceReciver textToVoiceReciver) {
        this.context = context;
        this.textToSpeak = str;
        this.textToVoiceReciver = textToVoiceReciver;
        this.speech = new TextToSpeech(context, this);
        setCompletionHandler();
        Log.e("TTV", this.textToSpeak);
    }

    private void setCompletionHandler() {
        this.speech.setOnUtteranceProgressListener(new UtteranceProgressListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.components.TextToVoice.1
            @Override // android.speech.tts.UtteranceProgressListener
            public void onDone(String str) {
                TextToVoice.this.textToVoiceReciver.onSpeakingEnd(TextToVoice.this);
            }

            @Override // android.speech.tts.UtteranceProgressListener
            public void onError(String str) {
                TextToVoice.this.textToVoiceReciver.onSpeakingCaughtError(TextToVoice.this);
                Log.e("error", "Speak up error");
            }

            @Override // android.speech.tts.UtteranceProgressListener
            public void onStart(String str) {
                TextToVoice.this.textToVoiceReciver.onSpeakingStarted(TextToVoice.this);
            }
        });
    }

    private void speakUp() {
        Log.e("final speak", this.textToSpeak);
        this.speech.speak(this.textToSpeak, 1, null, "com.audio.play.codeplay.bolo.utterance");
    }

    public void destory() {
        TextToSpeech textToSpeech = this.speech;
        if (textToSpeech != null) {
            try {
                textToSpeech.stop();
                this.speech.shutdown();
                this.speech = null;
                this.context = null;
                this.textToVoiceReciver = null;
            } catch (Exception unused) {
            }
        }
    }

    @Override // android.speech.tts.TextToSpeech.OnInitListener
    public void onInit(int i) {
        if (this.speech == null) {
            this.textToVoiceReciver.onSpeakingCaughtError(this);
            return;
        }
        Locale locale = Locale.getDefault();
        if (locale != null) {
            this.speech.setLanguage(locale);
        }
        if (i != 0 || this.textToSpeak == null) {
            return;
        }
        speakUp();
    }
}
