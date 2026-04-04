package com.colorcallscreen.colorphone.callscreen.calltheme.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import java.util.List;


public abstract class BluetoothHeadsetUtils {
    private static final String TAG = "BluetoothHeadsetUtils";
    private AudioManager mAudioManager;
    private BluetoothHeadset mBluetoothHeadset;
    private BluetoothDevice mConnectedHeadset;
    private Context mContext;
    private boolean mIsCountDownOn;
    private boolean mIsOnHeadsetSco;
    private boolean mIsStarted;
    private boolean mIsStarting;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.BluetoothHeadsetUtils.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int deviceClass;
            String action = intent.getAction();
            if (action.equals("android.bluetooth.device.action.ACL_CONNECTED")) {
                BluetoothHeadsetUtils.this.mConnectedHeadset = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                BluetoothClass bluetoothClass = BluetoothHeadsetUtils.this.mConnectedHeadset.getBluetoothClass();
                if (bluetoothClass != null && ((deviceClass = bluetoothClass.getDeviceClass()) == 1032 || deviceClass == 1028)) {
                    BluetoothHeadsetUtils.this.mAudioManager.setMode(2);
                    BluetoothHeadsetUtils.this.mIsCountDownOn = true;
                    BluetoothHeadsetUtils.this.mCountDown.start();
                    BluetoothHeadsetUtils.this.onHeadsetConnected();
                }
                Log.d(BluetoothHeadsetUtils.TAG, BluetoothHeadsetUtils.this.mConnectedHeadset.getName() + " connected");
            } else if (action.equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                Log.d(BluetoothHeadsetUtils.TAG, "Headset disconnected");
                if (BluetoothHeadsetUtils.this.mIsCountDownOn) {
                    BluetoothHeadsetUtils.this.mIsCountDownOn = false;
                    BluetoothHeadsetUtils.this.mCountDown.cancel();
                }
                BluetoothHeadsetUtils.this.mAudioManager.setMode(0);
                BluetoothHeadsetUtils.this.onHeadsetDisconnected();
            } else if (action.equals("android.media.SCO_AUDIO_STATE_CHANGED")) {
                int intExtra = intent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", -1);
                if (intExtra != 1) {
                    if (intExtra == 0) {
                        Log.d(BluetoothHeadsetUtils.TAG, "Sco disconnected");
                        if (BluetoothHeadsetUtils.this.mIsStarting) {
                            return;
                        }
                        BluetoothHeadsetUtils.this.mIsOnHeadsetSco = false;
                        BluetoothHeadsetUtils.this.mAudioManager.stopBluetoothSco();
                        BluetoothHeadsetUtils.this.onScoAudioDisconnected();
                        return;
                    }
                    return;
                }
                BluetoothHeadsetUtils.this.mIsOnHeadsetSco = true;
                if (BluetoothHeadsetUtils.this.mIsStarting) {
                    BluetoothHeadsetUtils.this.mIsStarting = false;
                    BluetoothHeadsetUtils.this.onHeadsetConnected();
                }
                if (BluetoothHeadsetUtils.this.mIsCountDownOn) {
                    BluetoothHeadsetUtils.this.mIsCountDownOn = false;
                    BluetoothHeadsetUtils.this.mCountDown.cancel();
                }
                BluetoothHeadsetUtils.this.onScoAudioConnected();
                Log.d(BluetoothHeadsetUtils.TAG, "Sco connected");
            }
        }
    };
    private CountDownTimer mCountDown = new CountDownTimer(10000, 1000) { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.BluetoothHeadsetUtils.2
        @Override
        public void onFinish() {
            BluetoothHeadsetUtils.this.mIsCountDownOn = false;
            BluetoothHeadsetUtils.this.mAudioManager.setMode(0);
            Log.d(BluetoothHeadsetUtils.TAG, "\nonFinish fail to connect to headset audio");
        }

        @Override
        public void onTick(long j) {
            BluetoothHeadsetUtils.this.mAudioManager.startBluetoothSco();
            Log.d(BluetoothHeadsetUtils.TAG, "\nonTick start bluetooth Sco");
        }
    };
    private BluetoothProfile.ServiceListener mHeadsetProfileListener = new BluetoothProfile.ServiceListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.BluetoothHeadsetUtils.3
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            Log.d(BluetoothHeadsetUtils.TAG, "Profile listener onServiceConnected");
            BluetoothHeadsetUtils.this.mBluetoothHeadset = (BluetoothHeadset) bluetoothProfile;
            List<BluetoothDevice> connectedDevices = BluetoothHeadsetUtils.this.mBluetoothHeadset.getConnectedDevices();
            if (connectedDevices.size() > 0) {
                BluetoothHeadsetUtils.this.mConnectedHeadset = connectedDevices.get(0);
                BluetoothHeadsetUtils.this.onHeadsetConnected();
                BluetoothHeadsetUtils.this.mIsCountDownOn = true;
                BluetoothHeadsetUtils.this.mCountDown11.start();
                Log.d(BluetoothHeadsetUtils.TAG, "Start count down");
            }
            BluetoothHeadsetUtils.this.mContext.registerReceiver(BluetoothHeadsetUtils.this.mHeadsetBroadcastReceiver, new IntentFilter("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED"));
            BluetoothHeadsetUtils.this.mContext.registerReceiver(BluetoothHeadsetUtils.this.mHeadsetBroadcastReceiver, new IntentFilter("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED"));
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            Log.d(BluetoothHeadsetUtils.TAG, "Profile listener onServiceDisconnected");
            BluetoothHeadsetUtils.this.stopBluetooth11();
        }
    };
    private BroadcastReceiver mHeadsetBroadcastReceiver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.BluetoothHeadsetUtils.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED")) {
                int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                Log.d(BluetoothHeadsetUtils.TAG, "\nAction = " + action + "\nState = " + intExtra);
                if (intExtra == 2) {
                    BluetoothHeadsetUtils.this.mConnectedHeadset = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    BluetoothHeadsetUtils.this.mIsCountDownOn = true;
                    BluetoothHeadsetUtils.this.mCountDown11.start();
                    BluetoothHeadsetUtils.this.onHeadsetConnected();
                    Log.d(BluetoothHeadsetUtils.TAG, "Start count down");
                    return;
                } else if (intExtra == 0) {
                    if (BluetoothHeadsetUtils.this.mIsCountDownOn) {
                        BluetoothHeadsetUtils.this.mIsCountDownOn = false;
                        BluetoothHeadsetUtils.this.mCountDown11.cancel();
                    }
                    BluetoothHeadsetUtils.this.mConnectedHeadset = null;
                    BluetoothHeadsetUtils.this.onHeadsetDisconnected();
                    Log.d(BluetoothHeadsetUtils.TAG, "Headset disconnected");
                    return;
                } else {
                    return;
                }
            }
            int intExtra2 = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 10);
            Log.d(BluetoothHeadsetUtils.TAG, "\nAction = " + action + "\nState = " + intExtra2);
            if (intExtra2 == 12) {
                Log.d(BluetoothHeadsetUtils.TAG, "\nHeadset audio connected");
                BluetoothHeadsetUtils.this.mIsOnHeadsetSco = true;
                if (BluetoothHeadsetUtils.this.mIsCountDownOn) {
                    BluetoothHeadsetUtils.this.mIsCountDownOn = false;
                    BluetoothHeadsetUtils.this.mCountDown11.cancel();
                }
                BluetoothHeadsetUtils.this.onScoAudioConnected();
            } else if (intExtra2 == 10) {
                BluetoothHeadsetUtils.this.mIsOnHeadsetSco = false;
                BluetoothHeadsetUtils.this.mBluetoothHeadset.stopVoiceRecognition(BluetoothHeadsetUtils.this.mConnectedHeadset);
                BluetoothHeadsetUtils.this.onScoAudioDisconnected();
                Log.d(BluetoothHeadsetUtils.TAG, "Headset audio disconnected");
            }
        }
    };
    private CountDownTimer mCountDown11 = new CountDownTimer(10000, 1000) { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.BluetoothHeadsetUtils.5
        @Override
        public void onFinish() {
            BluetoothHeadsetUtils.this.mIsCountDownOn = false;
            Log.d(BluetoothHeadsetUtils.TAG, "\nonFinish fail to connect to headset audio");
        }

        @Override
        public void onTick(long j) {
            BluetoothHeadsetUtils.this.mBluetoothHeadset.startVoiceRecognition(BluetoothHeadsetUtils.this.mConnectedHeadset);
            Log.d(BluetoothHeadsetUtils.TAG, "onTick startVoiceRecognition");
        }
    };
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public abstract void onHeadsetConnected();

    public abstract void onHeadsetDisconnected();

    public abstract void onScoAudioConnected();

    public abstract void onScoAudioDisconnected();

    public BluetoothHeadsetUtils(Context context) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    private boolean startBluetooth() {
        Log.d(TAG, "startBluetooth");
        if (this.mBluetoothAdapter == null || !this.mAudioManager.isBluetoothScoAvailableOffCall()) {
            return false;
        }
        this.mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
        this.mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
        this.mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.media.SCO_AUDIO_STATE_CHANGED"));
        this.mAudioManager.setMode(2);
        this.mIsCountDownOn = true;
        this.mCountDown.start();
        this.mIsStarting = true;
        return true;
    }

    private boolean startBluetooth11() {
        Log.d(TAG, "startBluetooth11");
        return this.mBluetoothAdapter != null && this.mAudioManager.isBluetoothScoAvailableOffCall() && this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mHeadsetProfileListener, 1);
    }

    private void stopBluetooth() {
        Log.d(TAG, "stopBluetooth");
        if (this.mIsCountDownOn) {
            this.mIsCountDownOn = false;
            this.mCountDown.cancel();
        }
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        this.mAudioManager.stopBluetoothSco();
        this.mAudioManager.setMode(0);
    }

    public boolean isOnHeadsetSco() {
        return this.mIsOnHeadsetSco;
    }

    public boolean start() {
        if (!this.mIsStarted) {
            this.mIsStarted = true;
            if (Build.VERSION.SDK_INT < 11) {
                this.mIsStarted = startBluetooth();
            } else {
                this.mIsStarted = startBluetooth11();
            }
        }
        return this.mIsStarted;
    }

    public void stop() {
        if (this.mIsStarted) {
            this.mIsStarted = false;
            if (Build.VERSION.SDK_INT < 11) {
                stopBluetooth();
            } else {
                stopBluetooth11();
            }
        }
    }

    protected void stopBluetooth11() {
        Log.d(TAG, "stopBluetooth11");
        if (this.mIsCountDownOn) {
            this.mIsCountDownOn = false;
            this.mCountDown11.cancel();
        }
        BluetoothHeadset bluetoothHeadset = this.mBluetoothHeadset;
        if (bluetoothHeadset != null) {
            bluetoothHeadset.stopVoiceRecognition(this.mConnectedHeadset);
            this.mContext.unregisterReceiver(this.mHeadsetBroadcastReceiver);
            this.mBluetoothAdapter.closeProfileProxy(1, this.mBluetoothHeadset);
            this.mBluetoothHeadset = null;
        }
    }
}
