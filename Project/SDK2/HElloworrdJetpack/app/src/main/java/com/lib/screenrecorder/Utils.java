package com.lib.screenrecorder;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.AsyncTask;

import java.util.ArrayList;

public class Utils {
    public static MediaCodecInfo[] mAacCodecInfos;
    public static MediaCodecInfo[] mAvcCodecInfos;

    Utils() {
    }

    public static MediaCodecInfo[] getmAacCodecInfos() {
        if (mAacCodecInfos == null) {
            mAacCodecInfos = findEncodersByType("audio/mp4a-latm");
        }
        return mAacCodecInfos;
    }

    public static MediaCodecInfo[] getmAvcCodecInfos() {
        if (mAvcCodecInfos == null) {
            mAvcCodecInfos = findEncodersByType("video/avc");
        }
        return mAvcCodecInfos;
    }

    public static void initMediaCodecInfo() {
        findEncodersByTypeAsync("video/avc", new Callback() {
            @Override
            public void onResult(MediaCodecInfo[] mediaCodecInfoArr) {
                Utils.mAvcCodecInfos = mediaCodecInfoArr;
            }
        });
        findEncodersByTypeAsync("audio/mp4a-latm", new Callback() {
            @Override
            public void onResult(MediaCodecInfo[] mediaCodecInfoArr) {
                Utils.mAacCodecInfos = mediaCodecInfoArr;
            }
        });
    }

    static void findEncodersByTypeAsync(String str, Callback callback) {
        new EncoderFinder(callback).execute(str);
    }

    static MediaCodecInfo[] findEncodersByType(String str) {
        MediaCodecList mediaCodecList = new MediaCodecList(1);
        ArrayList arrayList = new ArrayList();
        for (MediaCodecInfo mediaCodecInfo : mediaCodecList.getCodecInfos()) {
            if (mediaCodecInfo.isEncoder()) {
                try {
                    if (mediaCodecInfo.getCapabilitiesForType(str) != null) {
                        arrayList.add(mediaCodecInfo);
                    }
                } catch (IllegalArgumentException unused) {
                }
            }
        }
        return (MediaCodecInfo[]) arrayList.toArray(new MediaCodecInfo[arrayList.size()]);
    }

    public interface Callback {
        void onResult(MediaCodecInfo[] mediaCodecInfoArr);
    }

    public static final class EncoderFinder extends AsyncTask<String, Void, MediaCodecInfo[]> {
        private Callback func;

        EncoderFinder(Callback callback) {
            this.func = callback;
        }

        @Override
        public MediaCodecInfo[] doInBackground(String... strArr) {
            return Utils.findEncodersByType(strArr[0]);
        }

        @Override
        public void onPostExecute(MediaCodecInfo[] mediaCodecInfoArr) {
            this.func.onResult(mediaCodecInfoArr);
        }
    }
}
