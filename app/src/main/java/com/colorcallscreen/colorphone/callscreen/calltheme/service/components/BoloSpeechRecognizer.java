package com.colorcallscreen.colorphone.callscreen.calltheme.service.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.BuildConfig;
import com.colorcallscreen.colorphone.callscreen.calltheme.singleton.BoloSingleTon;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;


public class BoloSpeechRecognizer implements RecognitionListener {
    public static boolean isUsingGooleOnlineSpeechToText = false;
    private boolean canUseGoogleOnlineSpeechToText;
    private Context context;
    private String currentLanguageInUse;
    private boolean isForDuringCall;
    private String languageToUse;
    private BluetoothHelper mBluetoothHelper;
    private Intent recognizerIntent;
    private boolean shouldRestart;
    private SpeechRecognizer speech;
    private SpeechRecognizerReciver speechRecognizerReciver;
    private Handler timerHanler;
    private boolean shouldWorkOffline = false;
    private boolean isObserverAdded = false;
    private String specificLanguage = null;
    private boolean isGAEventSendForT2S = false;
    private boolean isWorkDone = false;
    private BroadcastReceiver localBroadcastReciver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.components.BoloSpeechRecognizer.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BoloSpeechRecognizer.this.onNewBroadcastRecived(intent);
        }
    };


    public interface SpeechRecognizerReciver {
        void onSpeachRestart(BoloSpeechRecognizer boloSpeechRecognizer);

        void onSpeachStarted(BoloSpeechRecognizer boloSpeechRecognizer);

        void onSpeechRecognizerError(BoloSpeechRecognizer boloSpeechRecognizer, int i);

        void onSpeechRecognizerResult(BoloSpeechRecognizer boloSpeechRecognizer, ArrayList<String> arrayList, SpeechRecognizerResultType speechRecognizerResultType, SpeechRecognizerResultFrom speechRecognizerResultFrom, String str);
    }


    public enum SpeechRecognizerResultFrom {
        GCD,
        RecognizerIntent,
        Unknown
    }


    public enum SpeechRecognizerResultType {
        PartialResult,
        FinalResult,
        Error
    }

    @Override // android.speech.RecognitionListener
    public void onBeginningOfSpeech() {
    }

    @Override // android.speech.RecognitionListener
    public void onBufferReceived(byte[] bArr) {
    }

    @Override // android.speech.RecognitionListener
    public void onEndOfSpeech() {
    }

    @Override // android.speech.RecognitionListener
    public void onEvent(int i, Bundle bundle) {
    }

    @Override // android.speech.RecognitionListener
    public void onRmsChanged(float f) {
    }

    public BoloSpeechRecognizer(Context context, boolean z, boolean z2, SpeechRecognizerReciver speechRecognizerReciver) {
        this.context = context;
        isUsingGooleOnlineSpeechToText = false;
        this.shouldRestart = z;
        this.speechRecognizerReciver = speechRecognizerReciver;
        this.canUseGoogleOnlineSpeechToText = z2;
        this.isForDuringCall = z2;
        addObserver();
    }

    private void addObserver() {
        if (!this.isObserverAdded) {
            try {
                LocalBroadcastManager.getInstance(BoloApplication.getApplication()).registerReceiver(this.localBroadcastReciver, new IntentFilter(Constants.InternetStatusChanged));
            } catch (Exception unused) {
            }
        }
        this.isObserverAdded = true;
    }

    private void checkInternetIfPresent() {
        boolean z = false;
        try {
            if (!Utility.isConnected(false)) {
                z = true;
            }
            this.shouldWorkOffline = z;
        } catch (Exception unused) {
        }
    }

    private void cleanUpTimerHandler() {
        Handler handler = this.timerHanler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.timerHanler = null;
        }
    }

    private String getPerferedLangague() {
        String string = PreferenceUtils.getInstance().getString("PreferredLanguage");
        return (string == null || string.isEmpty()) ? Utility.getLanguageCode().getLanguage() : string;
    }

    public void onNewBroadcastRecived(Intent intent) {
        try {
            if (intent.getAction().equals(Constants.InternetStatusChanged)) {
                startVoiceRecgnozier(false);
                System.out.println("[Sachgggin] progress : onNewBroadcastRecived");
            }
        } catch (Exception unused) {
        }
    }

    private void removeObserver() {
        try {
            LocalBroadcastManager.getInstance(BoloApplication.getApplication()).unregisterReceiver(this.localBroadcastReciver);
        } catch (Exception unused) {
        }
    }

    public void setUpVoiceRecgnozier() {
        String str;
        SpeechRecognizer speechRecognizer = this.speech;
        if (speechRecognizer != null) {
            try {
                speechRecognizer.destroy();
                this.speech = null;
            } catch (Exception unused) {
            }
        }
        Context context = this.context;
        if (context != null) {
            if (!this.shouldWorkOffline && this.canUseGoogleOnlineSpeechToText) {
                isUsingGooleOnlineSpeechToText = true;
                return;
            }
            if (this.isForDuringCall) {
                this.shouldWorkOffline = true;
            }
            isUsingGooleOnlineSpeechToText = false;
            if (SpeechRecognizer.isRecognitionAvailable(context) && Utility.isVoiceRecogzinerAvailable) {
                SpeechRecognizer createSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.context);
                this.speech = createSpeechRecognizer;
                createSpeechRecognizer.setRecognitionListener(this);
                if (this.recognizerIntent == null) {
                    Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                    this.recognizerIntent = intent;
                    intent.putExtra("calling_package", BuildConfig.APPLICATION_ID);
                    this.recognizerIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                    this.recognizerIntent.putExtra("android.speech.extra.DICTATION_MODE", true);
                    this.recognizerIntent.putExtra("android.speech.extra.MAX_RESULTS", 1);
                    if (BoloSingleTon.getInstance(this.context).supportedLanguages != null) {
                        this.recognizerIntent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", (String[]) BoloSingleTon.getInstance(this.context).supportedLanguages.toArray(new String[0]));
                    }
                    String str2 = this.specificLanguage;
                    if (str2 == null) {
                        this.recognizerIntent.putExtra("android.speech.extra.LANGUAGE_PREFERENCE", Locale.getDefault());
                    } else {
                        this.recognizerIntent.putExtra("android.speech.extra.LANGUAGE_PREFERENCE", str2);
                    }
                    this.recognizerIntent.putExtra("android.speech.extra.PROMPT", "");
                    this.recognizerIntent.putExtra("android.speech.extra.PARTIAL_RESULTS", true);
                    this.recognizerIntent.putExtra("android.speech.extra.LANGUAGE", "en-US");
                }
                boolean booleanExtra = this.recognizerIntent.getBooleanExtra("android.speech.extra.PREFER_OFFLINE", false);
                boolean z = this.shouldWorkOffline;
                if (booleanExtra != z) {
                    this.recognizerIntent.putExtra("android.speech.extra.PREFER_OFFLINE", z);
                }
                if (!this.isForDuringCall && ((str = this.languageToUse) == null || str.isEmpty())) {
                    this.currentLanguageInUse = Locale.getDefault().getLanguage();
                    this.recognizerIntent.putExtra("android.speech.extra.LANGUAGE", Locale.getDefault());
                } else {
                    String str3 = this.languageToUse;
                    if (str3 != null && !str3.isEmpty()) {
                        this.recognizerIntent.putExtra("android.speech.extra.LANGUAGE", Locale.forLanguageTag(this.languageToUse));
                        this.currentLanguageInUse = this.languageToUse;
                    } else {
                        String perferedLangague = getPerferedLangague();
                        this.currentLanguageInUse = perferedLangague;
                        this.recognizerIntent.putExtra("android.speech.extra.LANGUAGE", Locale.forLanguageTag(perferedLangague));
                    }
                }
            } else {
                this.speechRecognizerReciver.onSpeechRecognizerResult(this, null, SpeechRecognizerResultType.Error, SpeechRecognizerResultFrom.RecognizerIntent, this.languageToUse);
                this.speechRecognizerReciver.onSpeechRecognizerError(this, Constants.Speech_Recognizer_Not_Avilable);
            }
            if (this.isGAEventSendForT2S) {
                return;
            }
            this.isGAEventSendForT2S = true;
            return;
        }
        destory();
    }

    private ArrayList<String> udpateMatches(ArrayList<String> arrayList) {
        if (arrayList != null) {
            ArrayList<String> arrayList2 = new ArrayList<>();
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (next.length() > 1 && !next.toLowerCase().equals("are") && !next.toLowerCase().equals(NotificationCompat.CATEGORY_CALL) && !next.toLowerCase().equals("oh") && !arrayList2.contains(next)) {
                    arrayList2.add(next.toLowerCase());
                }
            }
            return arrayList2;
        }
        return new ArrayList<>();
    }

    public void destory() {
        this.isWorkDone = true;
        Utility.cleanUpInternetSpeedChecker();
        removeObserver();
        if (this.speech != null) {
            try {
                Log.e("Sach", "destory");
                this.speech.setRecognitionListener(null);
                this.speech.destroy();
                this.speech = null;
            } catch (Exception unused) {
            }
        }
        if (this.recognizerIntent != null) {
            this.recognizerIntent = null;
        }
        cleanUpTimerHandler();
        this.context = null;
    }

    @Override // android.speech.RecognitionListener
    public void onError(int i) {
        if ((i == 4 || i == 2) && this.shouldWorkOffline) {
            if (this.currentLanguageInUse.equals("en-US")) {
                this.speechRecognizerReciver.onSpeechRecognizerResult(this, null, SpeechRecognizerResultType.Error, SpeechRecognizerResultFrom.RecognizerIntent, this.currentLanguageInUse);
                this.speechRecognizerReciver.onSpeechRecognizerError(this, i);
                return;
            }
            PreferenceUtils.getInstance().putPreference("IsVoiceRecognizerUsingOnline", true);
            this.languageToUse = "en-US";
            startVoiceRecgnozier(false);
            return;
        }
        if (this.shouldRestart) {
            startVoiceRecgnozier(false);
        }
        this.speechRecognizerReciver.onSpeechRecognizerResult(this, new ArrayList<>(), SpeechRecognizerResultType.Error, SpeechRecognizerResultFrom.RecognizerIntent, this.currentLanguageInUse);
        this.speechRecognizerReciver.onSpeechRecognizerError(this, i);
    }

    @Override // android.speech.RecognitionListener
    public void onPartialResults(Bundle bundle) {
        cleanUpTimerHandler();
        ArrayList<String> stringArrayList = bundle.getStringArrayList("results_recognition");
        if (stringArrayList != null) {
            Log.e("Sachgggin partial", TextUtils.join(",", stringArrayList));
        }
        this.speechRecognizerReciver.onSpeechRecognizerResult(this, udpateMatches(stringArrayList), SpeechRecognizerResultType.PartialResult, SpeechRecognizerResultFrom.RecognizerIntent, this.currentLanguageInUse);
        if (this.currentLanguageInUse.equals(Locale.getDefault().getLanguage())) {
            PreferenceUtils.getInstance().putPreference("IsVoiceRecognizerUsingOnline", false);
        }
    }

    @Override // android.speech.RecognitionListener
    public void onReadyForSpeech(Bundle bundle) {
        this.speechRecognizerReciver.onSpeachStarted(this);
    }

    @Override // android.speech.RecognitionListener
    public void onResults(Bundle bundle) {
        cleanUpTimerHandler();
        ArrayList<String> stringArrayList = bundle.getStringArrayList("results_recognition");
        if (stringArrayList != null) {
            this.speechRecognizerReciver.onSpeechRecognizerResult(this, udpateMatches(stringArrayList), SpeechRecognizerResultType.FinalResult, SpeechRecognizerResultFrom.RecognizerIntent, this.currentLanguageInUse);
        }
        if (this.shouldRestart) {
            startVoiceRecgnozier(false);
        }
    }

    public void startVoiceRecgnozier(boolean z) {
        if (this.isWorkDone) {
            return;
        }
        this.speechRecognizerReciver.onSpeachRestart(this);
        checkInternetIfPresent();
        if (Looper.myLooper() == Looper.getMainLooper()) {
            setUpVoiceRecgnozier();
            SpeechRecognizer speechRecognizer = this.speech;
            if (speechRecognizer != null) {
                speechRecognizer.startListening(this.recognizerIntent);
                return;
            }
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.components.BoloSpeechRecognizer.2
            @Override 
            public void run() {
                BoloSpeechRecognizer.this.setUpVoiceRecgnozier();
                if (BoloSpeechRecognizer.this.speech != null) {
                    BoloSpeechRecognizer.this.speech.startListening(BoloSpeechRecognizer.this.recognizerIntent);
                }
            }
        });
    }
}
