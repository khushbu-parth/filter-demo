package com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.audiomanager.BoloAudioManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockCallService;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDataSource;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.interfaces.RingingCallManagerDelegate;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.components.BoloSpeechRecognizer;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.components.TextToVoice;

import java.util.ArrayList;


public class CallRingingManager {
    private RingingCallManagerDataSource dataSource;
    private RingingCallManagerDelegate delegate;
    private BoloSpeechRecognizer mSpeechRecognizer;
    private TextToVoice mTextToVoice;
    private int defaultRingingMode = 0;
    private int defaultRingingIndex = 0;
    private int defaultSystemSound = 0;
    private boolean isSpeakUpInProcess = false;
    private boolean isCleaningInProcess = false;
    private boolean isInBluetoothMode = false;

    
    public enum CallResponseGesture {
        Hand,
        Voice
    }

    public CallRingingManager(RingingCallManagerDataSource ringingCallManagerDataSource, RingingCallManagerDelegate ringingCallManagerDelegate, Context context) {
        setDataSource(ringingCallManagerDataSource);
        setDelegate(ringingCallManagerDelegate);
        Utility.checkAndUpdateVoiceRecognizerStatus(context);
        updateDeafultSetting(context);
    }

    public void cleanUpSpeakUp(TextToVoice textToVoice, Context context, boolean z) {
        if (this.mTextToVoice != null) {
            textToVoice.destory();
            this.mTextToVoice = null;
        }
    }

    private void cleanUpSpeechRecognizer(Context context) {
        try {
            BoloSpeechRecognizer boloSpeechRecognizer = this.mSpeechRecognizer;
            if (boloSpeechRecognizer != null) {
                boloSpeechRecognizer.destory();
                this.mSpeechRecognizer = null;
            }
            this.isInBluetoothMode = false;
        } catch (Exception unused) {
        }
    }

    public void handleOnSpeakUpStarted(Context context) {
        this.delegate.onSpeakUpStarted();
    }

    public void handleOnSpeechRecognizerResult(Context context, ArrayList<String> arrayList, BoloSpeechRecognizer.SpeechRecognizerResultType speechRecognizerResultType, BoloSpeechRecognizer.SpeechRecognizerResultFrom speechRecognizerResultFrom, String str) {
        this.delegate.onSpeechRecognizerResult(arrayList, speechRecognizerResultType, speechRecognizerResultFrom, str);
    }

    private void handleSpeakUp(final Context context, final boolean z) {
        if (this.dataSource.shouldSpeakUpCallerName()) {
            if (z && this.defaultRingingMode != 2) {
                Log.e("", "Not Normal Ringer");
                this.delegate.unableToStartSpeakUp(RingingCallManagerDelegate.SpeakUpError.OnSilent);
                return;
            }
            String speakUpText = this.dataSource.speakUpText();
            if (speakUpText != null) {
                this.isSpeakUpInProcess = true;
                this.mTextToVoice = new TextToVoice(context, speakUpText, new TextToVoice.TextToVoiceReciver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.CallRingingManager.1
                    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.components.TextToVoice.TextToVoiceReciver
                    public void onSpeakingCaughtError(TextToVoice textToVoice) {
                        CallRingingManager.this.cleanUpSpeakUp(textToVoice, context, z);
                        CallRingingManager.this.delegate.unableToStartSpeakUp(RingingCallManagerDelegate.SpeakUpError.SystemError);
                        CallRingingManager.this.isSpeakUpInProcess = false;
                    }

                    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.components.TextToVoice.TextToVoiceReciver
                    public void onSpeakingEnd(TextToVoice textToVoice) {
                        CallRingingManager.this.onSpeakUpEnded(textToVoice, context, z);
                    }

                    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.components.TextToVoice.TextToVoiceReciver
                    public void onSpeakingStarted(TextToVoice textToVoice) {
                        CallRingingManager.this.handleOnSpeakUpStarted(context);
                    }
                });
                return;
            }
            this.delegate.unableToStartSpeakUp(RingingCallManagerDelegate.SpeakUpError.InvalidText);
            this.isSpeakUpInProcess = false;
        }
    }

    private void handleVoiceRecongnizer(final Context context, boolean z) {
        if (this.mSpeechRecognizer == null && this.dataSource.shouldStartSpeechRecognizer()) {
            this.isInBluetoothMode = false;
            BoloSpeechRecognizer boloSpeechRecognizer = new BoloSpeechRecognizer(context, this.dataSource.shouldSpeechRecoginzerRestartAfterFinish(), z, new BoloSpeechRecognizer.SpeechRecognizerReciver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.CallRingingManager.2
                @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.components.BoloSpeechRecognizer.SpeechRecognizerReciver
                public void onSpeachRestart(BoloSpeechRecognizer boloSpeechRecognizer2) {
                }

                @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.components.BoloSpeechRecognizer.SpeechRecognizerReciver
                public void onSpeachStarted(BoloSpeechRecognizer boloSpeechRecognizer2) {
                    CallRingingManager.this.delegate.onSpeechStarted();
                }

                @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.components.BoloSpeechRecognizer.SpeechRecognizerReciver
                public void onSpeechRecognizerError(BoloSpeechRecognizer boloSpeechRecognizer2, int i) {
                    CallRingingManager.this.delegate.onSpeechRecognizerError(i);
                }

                @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.components.BoloSpeechRecognizer.SpeechRecognizerReciver
                public void onSpeechRecognizerResult(BoloSpeechRecognizer boloSpeechRecognizer2, ArrayList<String> arrayList, BoloSpeechRecognizer.SpeechRecognizerResultType speechRecognizerResultType, BoloSpeechRecognizer.SpeechRecognizerResultFrom speechRecognizerResultFrom, String str) {
                    CallRingingManager.this.handleOnSpeechRecognizerResult(context, arrayList, speechRecognizerResultType, speechRecognizerResultFrom, str);
                }
            });
            this.mSpeechRecognizer = boloSpeechRecognizer;
            boloSpeechRecognizer.startVoiceRecgnozier(true);
        }
    }

    public void onSpeakUpEnded(TextToVoice textToVoice, Context context, boolean z) {
        this.isSpeakUpInProcess = false;
        cleanUpSpeakUp(textToVoice, context, z);
        this.delegate.onSpeakingEnded();
    }

    private int ringingVolumeForVoiceRecnognizer(Context context) {
        if (BoloSpeechRecognizer.isUsingGooleOnlineSpeechToText) {
            return this.defaultRingingIndex;
        }
        return 0;
    }

    public boolean canContinueRingtone() {
        if (this.mSpeechRecognizer != null) {
            return BoloSpeechRecognizer.isUsingGooleOnlineSpeechToText;
        }
        return false;
    }

    public void changeRingingIndexToBelowTwoPoints() {
        BoloAudioManager.setAudioStreamVolume(BoloApplication.getApplication(), this.defaultRingingIndex);
    }

    public void changeToDeafultSetting(Context context, boolean z, boolean z2) {
        if (this.defaultRingingIndex > 0) {
            Log.e("hello", "changeToDeafultSetting procced" + this.defaultRingingIndex);
            if (z2) {
                BoloAudioManager.setAudioStreamVolume(context, this.defaultRingingIndex);
            } else {
                changeRingingIndexToBelowTwoPoints();
            }
        }
        int i = this.defaultSystemSound;
        if (i <= 0 || !z) {
            return;
        }
        BoloAudioManager.setAudioStreamVolumeForStream(context, 3, i);
    }

    public void changeToMuteSetting(Context context) {
        if (this.isCleaningInProcess || this.defaultSystemSound <= 0) {
            return;
        }
        BoloAudioManager.setAudioStreamVolumeForStream(context, 3, 0);
    }

    public void changeToSpeakUpSetting(Context context) {
        if (this.isCleaningInProcess || this.defaultRingingIndex <= 0) {
            return;
        }
        Log.e("hello", "changeToSpeakUpSetting procced" + this.defaultRingingIndex);
        BoloAudioManager.setAudioStreamVolume(context, 1);
        BoloAudioManager.setAudioStreamVolumeForStream(context, 3, this.defaultSystemSound);
    }

    public void changeToVoiceRecnoginzerSetting(Context context) {
        if (this.isCleaningInProcess) {
            return;
        }
        if (this.defaultRingingIndex > 0) {
            BoloAudioManager.setAudioStreamVolume(context, ringingVolumeForVoiceRecnognizer(context));
        }
        if (this.defaultSystemSound > 0) {
            BoloAudioManager.setAudioStreamVolumeForStream(context, 3, 0);
        }
    }

    public void cleanUpEverything(Context context) {
        this.isCleaningInProcess = true;
        cleanUpSpeechRecognizer(context);
        cleanUpSpeakUp(this.mTextToVoice, context, true);
        changeToDeafultSetting(context, true, true);
    }

    public void setDataSource(RingingCallManagerDataSource ringingCallManagerDataSource) {
        this.dataSource = ringingCallManagerDataSource;
    }

    public void setDelegate(RingingCallManagerDelegate ringingCallManagerDelegate) {
        this.delegate = ringingCallManagerDelegate;
    }

    public void startNameSpeakUp(Context context, boolean z) {
        handleSpeakUp(context, z);
    }

    public void startVoiceRecognizer(Context context, boolean z) {
        handleVoiceRecongnizer(context, z);
    }

    public void stopSpeakUp(Context context, boolean z) {
        TextToVoice textToVoice = this.mTextToVoice;
        if (textToVoice != null) {
            onSpeakUpEnded(textToVoice, context, z);
        }
    }

    public void stopVoiceRecognizer(Context context) {
        cleanUpSpeechRecognizer(context);
    }

    public void updateDeafultSetting(Context context) {
        int i;
        int i2;
        int i3;
        if (this.isCleaningInProcess) {
            return;
        }
        Log.e("hello", "updateDeafultSetting");
        if (this.defaultRingingMode > 0 || this.defaultRingingIndex > 0 || this.defaultSystemSound > 0) {
            return;
        }
        int i4 = Build.VERSION.SDK_INT;
        if (i4 >= 24 && (i3 = BlockCallService.defaultRingingMode) > -1) {
            this.defaultRingingMode = i3;
        } else {
            this.defaultRingingMode = BoloAudioManager.getAudioStatus(context);
        }
        Log.e("hello", "defaultRingingMode" + this.defaultRingingMode);
        if (i4 >= 24 && (i2 = BlockCallService.defaultRingingIndex) > -1) {
            this.defaultRingingIndex = i2;
        } else {
            this.defaultRingingIndex = BoloAudioManager.getAudioStreamVolume(context);
        }
        if (i4 >= 24 && (i = BlockCallService.defaultSystemSound) > -1) {
            this.defaultSystemSound = i;
        } else {
            this.defaultSystemSound = BoloAudioManager.getAudioStreamVolumeForStream(3, context);
        }
        this.defaultRingingMode = BoloAudioManager.getAudioStatus(context);
        Log.e("hello", "defaultRingingMode" + this.defaultRingingMode);
        this.defaultRingingIndex = BoloAudioManager.getAudioStreamVolume(context);
        this.defaultSystemSound = BoloAudioManager.getAudioStreamVolumeForStream(3, context);
        changeRingingIndexToBelowTwoPoints();
    }
}
