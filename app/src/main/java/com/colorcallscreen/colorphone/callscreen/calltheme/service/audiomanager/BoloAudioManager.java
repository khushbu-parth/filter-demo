package com.colorcallscreen.colorphone.callscreen.calltheme.service.audiomanager;

import android.content.Context;
import android.media.AudioManager;
import android.telecom.TelecomManager;
import android.util.Log;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.CallService;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallOpreationHandler;


public class BoloAudioManager {
    public static AudioManager audioManager(Context context) {
        return (AudioManager) context.getSystemService("audio");
    }

    public static int getAudioStatus(Context context) {
        return audioManager(context).getRingerMode();
    }

    public static int getAudioStreamVolume(Context context) {
        return audioManager(context).getStreamVolume(2);
    }

    public static int getAudioStreamVolumeForStream(int i, Context context) {
        Log.e("hello", "getVolume of music");
        return audioManager(context).getStreamVolume(i);
    }

    public static int getMaxAudioStreamVolume(Context context) {
        return audioManager(context).getStreamMaxVolume(2);
    }

    public static boolean isStreamMute(Context context, int i) {
        try {
            boolean isStreamMute = audioManager(context).isStreamMute(i);
            Log.e("d", "he");
            return isStreamMute;
        } catch (Exception unused) {
            return false;
        }
    }

    public static void muteCall(Context context) {
        CallOpreationHandler callOpreationHandler;
        try {
            ((TelecomManager) BoloApplication.getApplication().getSystemService("telecom")).silenceRinger();
        } catch (Exception unused) {
        }
        CallHandler callHandler = CallHandler.sharedInstance;
        if (callHandler == null || (callOpreationHandler = callHandler.callOpreationHandler) == null) {
            return;
        }
        callOpreationHandler.userAskedToPutCallOnMute();
    }

    public static void muteMic(Context context) {
        audioManager(context).setMicrophoneMute(true);
    }

    public static void muteVolume(Context context) {
        AudioManager audioManager = audioManager(context);
        audioManager.setStreamMute(3, true);
        audioManager.adjustStreamVolume(3, -100, 0);
    }

    public static void putOnSpeaker(Context context) {
        AudioManager audioManager = audioManager(context);
        CallService callService = CallService.callService;
        if (callService != null) {
            try {
                callService.setAudioRoute(8);
                return;
            } catch (Exception unused) {
                return;
            }
        }
        audioManager.setSpeakerphoneOn(true);
    }

    public static void removeSpeakerMode(Context context) {
        CallService callService;
        AudioManager audioManager = audioManager(context);
        audioManager.setSpeakerphoneOn(false);
        if (!audioManager.isSpeakerphoneOn() || (callService = CallService.callService) == null) {
            return;
        }
        try {
            callService.setAudioRoute(5);
        } catch (Exception unused) {
        }
    }

    public static void setAudioStatus(int i, Context context) {
        audioManager(context).setRingerMode(i);
    }

    public static void setAudioStreamMaxVolumeForStream(Context context, int i) {
        AudioManager audioManager = audioManager(context);
        audioManager.getStreamMaxVolume(i);
        audioManager.setStreamVolume(i, audioManager.getStreamMaxVolume(i), 0);
    }

    public static void setAudioStreamVolume(Context context, int i) {
        try {
            if (i != getAudioStreamVolume(BoloApplication.getApplication())) {
                audioManager(context).setStreamVolume(2, i, 0);
            }
        } catch (Exception unused) {
        }
    }

    public static void setAudioStreamVolumeForStream(Context context, int i, int i2) {
        try {
            Log.e("hello", "getVolume of music " + i2);
            if (i2 != getAudioStreamVolumeForStream(i, BoloApplication.getApplication())) {
                audioManager(context).setStreamVolume(i, i2, 0);
            }
        } catch (Exception unused) {
        }
    }

    public static void setAudioToSpeaker(Context context) {
        AudioManager audioManager = audioManager(context);
        audioManager.setMode(2);
        audioManager.setMicrophoneMute(false);
        audioManager.setSpeakerphoneOn(true);
    }

    public static void setAudioToUnMute(Context context) {
        AudioManager audioManager = audioManager(context);
        audioManager.setMode(2);
        audioManager.setMicrophoneMute(false);
    }

    public static void unMuteMic(Context context) {
        audioManager(context).setMicrophoneMute(false);
    }

    public static void unMuteVolume(Context context) {
        audioManager(context).adjustStreamVolume(3, 100, 0);
    }
}
