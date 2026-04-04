package com.colorcallscreen.colorphone.callscreen.calltheme.callRecording;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class RecordedCallFetchHandler {
    private static RecordedCallFetchHandler sharedInstance;
    private BroadcastReceiver receiver;
    private List<RecordedCallModel> recordedCallModels = new ArrayList();


    public interface OnRecordedCallFetched {
        void onRecordedCallFetched(List<RecordedCallModel> list);
    }

    public void fetchCallRecording() {
        File[] listFiles;
        this.recordedCallModels = new ArrayList();
        File file = Constants.CallRecordingFolder;
        if (file.exists()) {
            for (File file2 : file.listFiles()) {
                if (file2.isDirectory()) {
                    for (File file3 : file2.listFiles()) {
                        RecordedCallModel parseRecordedCallModel = parseRecordedCallModel(file2, file3);
                        if (parseRecordedCallModel != null) {
                            this.recordedCallModels.add(parseRecordedCallModel);
                        }
                        Collections.sort(this.recordedCallModels, new Comparator<RecordedCallModel>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.callRecording.RecordedCallFetchHandler.1
                            @Override // java.util.Comparator
                            public int compare(RecordedCallModel recordedCallModel, RecordedCallModel recordedCallModel2) {
                                return Long.compare(recordedCallModel.getCreationTime(), recordedCallModel2.getCreationTime());
                            }
                        });
                        Collections.reverse(this.recordedCallModels);
                    }
                }
            }
        }
    }

    public static RecordedCallFetchHandler getSharedInstance() {
        if (sharedInstance == null) {
            RecordedCallFetchHandler recordedCallFetchHandler = new RecordedCallFetchHandler();
            sharedInstance = recordedCallFetchHandler;
            recordedCallFetchHandler.registerBroadcast();
        }
        return sharedInstance;
    }

    public RecordedCallModel parseRecordedCallModel(File file, File file2) {
        String[] split;
        if (!file2.isFile() || !file2.exists() || (split = file2.getName().split("_")) == null || split.length < 4) {
            return null;
        }
        RecordedCallModel recordedCallModel = new RecordedCallModel();
        recordedCallModel.setNumber(file.getName());
        recordedCallModel.setFile(file2);
        recordedCallModel.setFileName(file2.getName());
        recordedCallModel.setName(split[0]);
        recordedCallModel.setCreationTime(Long.parseLong(split[1]));
        recordedCallModel.setCallType(split[2]);
        String stripExtension = Utility.stripExtension(split[3]);
        split[3] = stripExtension;
        recordedCallModel.setFav(stripExtension.toString().replace("favbol", "").equals("1"));
        try {
            Uri parse = Uri.parse(file2.getPath());
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(BoloApplication.getApplication(), parse);
            String extractMetadata = mediaMetadataRetriever.extractMetadata(9);
            if (extractMetadata != null) {
                recordedCallModel.setDuration(Integer.parseInt(extractMetadata));
                return recordedCallModel;
            }
            return recordedCallModel;
        } catch (Exception unused) {
            return recordedCallModel;
        }
    }

    public List<RecordedCallModel> recordedCallModelList(boolean z) {
        if (!z) {
            return this.recordedCallModels;
        }
        ArrayList arrayList = new ArrayList();
        for (RecordedCallModel recordedCallModel : this.recordedCallModels) {
            if (recordedCallModel.isFav()) {
                arrayList.add(recordedCallModel);
            }
        }
        return arrayList;
    }

    private void registerBroadcast() {
        unRegisterBroadcast();
        try {
            this.receiver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.callRecording.RecordedCallFetchHandler.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    RecordedCallModel parseRecordedCallModel;
                    String stringExtra = intent.getStringExtra("parent");
                    String stringExtra2 = intent.getStringExtra("current");
                    if (stringExtra == null || stringExtra.isEmpty() || stringExtra2 == null || stringExtra2.isEmpty() || (parseRecordedCallModel = RecordedCallFetchHandler.this.parseRecordedCallModel(new File(stringExtra), new File(stringExtra2))) == null) {
                        return;
                    }
                    RecordedCallFetchHandler.this.recordedCallModels.add(0, parseRecordedCallModel);
                }
            };
            LocalBroadcastManager.getInstance(BoloApplication.getApplication()).registerReceiver(this.receiver, new IntentFilter(Constants.OnNewCallRecordingAdded));
        } catch (Exception unused) {
        }
    }

    private void unRegisterBroadcast() {
        try {
            if (this.receiver != null) {
                LocalBroadcastManager.getInstance(BoloApplication.getApplication()).unregisterReceiver(this.receiver);
            }
        } catch (Exception unused) {
        }
    }

    protected void finalize() throws Throwable {
        unRegisterBroadcast();
        super.finalize();
    }

    public void getRecordedCallsModel(final boolean z, final OnRecordedCallFetched onRecordedCallFetched) {
        if (onRecordedCallFetched == null) {
            return;
        }
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.callRecording.RecordedCallFetchHandler.3
            @Override 
            public void run() {
                if (RecordedCallFetchHandler.this.recordedCallModels == null || RecordedCallFetchHandler.this.recordedCallModels.isEmpty()) {
                    RecordedCallFetchHandler.this.fetchCallRecording();
                }
                final List<RecordedCallModel> recordedCallModelList = RecordedCallFetchHandler.this.recordedCallModelList(z);
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.callRecording.RecordedCallFetchHandler.3.1
                    @Override 
                    public void run() {
                        onRecordedCallFetched.onRecordedCallFetched(recordedCallModelList);
                    }
                });
            }
        }).start();
    }

    public void onRecordingDeleted(RecordedCallModel recordedCallModel) {
        List<RecordedCallModel> list = this.recordedCallModels;
        if (list != null) {
            list.remove(recordedCallModel);
        }
    }
}
