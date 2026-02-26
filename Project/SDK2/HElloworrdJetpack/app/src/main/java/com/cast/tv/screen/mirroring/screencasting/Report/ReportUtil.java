package com.cast.tv.screen.mirroring.screencasting.Report;

import com.cast.tv.screen.mirroring.screencasting.CastApp;
import com.cast.tv.screen.mirroring.screencasting.Contract.SPContracts;
import com.cast.tv.screen.mirroring.screencasting.Utils.DeviceInfoUtils;
import com.cast.tv.screen.mirroring.screencasting.Utils.L;
import com.cast.tv.screen.mirroring.screencasting.Utils.SPUtils;
import com.cast.tv.screen.mirroring.screencasting.Utils.net.NetUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.listener.OnGetOaidListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportUtil {
    private static final String TAG = "ReportUtil";

    private ReportUtil() {
    }

    public static void openApp() {
        report(ReportEventId.Loading_000);
    }

    public static void loadHomePage() {
        report(ReportEventId.Frontpage_001);
    }

    public static void loadPhotoPage() {
        report(ReportEventId.Photo_002);
    }

    public static void loadVideoPage() {
        report(ReportEventId.Video_003);
    }

    public static void loadAudioPage() {
        report(ReportEventId.Audio_004);
    }

    public static void loadSettingPage() {
        report(ReportEventId.Setting_005);
    }

    public static void loadControlVAPage() {
        report(ReportEventId.ControlVA_006);
    }

    public static void loadControlPPage() {
        report(ReportEventId.ControlP_007);
    }

    public static void clickScreenMirror() {
        report(ReportEventId._001_ScreenMirror);
    }

    public static void clickAudio() {
        report(ReportEventId._001_Audio);
    }

    public static void clickVideo() {
        report(ReportEventId._001_Video);
    }

    public static void clickYoutube() {
        report(ReportEventId._001_Youtube);
    }

    public static void clickPhoto() {
        report(ReportEventId._001_Photo);
    }

    public static void clickShare() {
        report(ReportEventId._001_Share);
    }

    public static void clickSetting() {
        report(ReportEventId._001_Setting);
    }

    public static void clickHomeInstruction() {
        report(ReportEventId._001_instruction);
    }

    public static void quitApp() {
        report(ReportEventId._001_quit);
    }

    public static void clickPhotoDir() {
        report(ReportEventId._002_click);
    }

    public static void backPhoto() {
        report(ReportEventId._002_back);
    }

    public static void clickVideoDir() {
        report(ReportEventId._003_click);
    }

    public static void backVideo() {
        report(ReportEventId._003_back);
    }

    public static void clickAudioDir() {
        report(ReportEventId._004_click);
    }

    public static void backAudio() {
        report(ReportEventId._004_back);
    }

    public static void clickSettingInstruction() {
        report(ReportEventId._005_instruction);
    }

    public static void clickRemoveAds() {
        report(ReportEventId._005_removeads);
    }

    public static void clickProductQR() {
        report(ReportEventId._005_productqr);
    }

    public static void clickProductPDF() {
        report(ReportEventId._005_productpdf);
    }

    public static void stayAVDuration(long j) {
        HashMap hashMap = new HashMap();
        hashMap.put("StayDuration", Long.valueOf(obtainCurTime() - j));
        report(ReportEventId._006_Duration, hashMap);
    }

    public static void stayPDuration(long j) {
        HashMap hashMap = new HashMap();
        hashMap.put("StayDuration", Long.valueOf(obtainCurTime() - j));
        report(ReportEventId._007_Duration, hashMap);
    }

    private static void report(String str) {
        report(str, null);
    }

    private static void report(final String str, final Map<String, Object> map) {
        if (str == null || str.isEmpty() || isReportEventId(str)) {
            return;
        }
        UMConfigure.getOaid(CastApp.mContext, new OnGetOaidListener() {
            @Override
            public final void onGetOaid(String str2) {
                ReportUtil.report$0(map, str, str2);
            }
        });
    }

    public static void report$0(Map map, String str, String str2) {
        if (map == null) {
            map = new HashMap();
        }
        if (str2 != null && !str2.isEmpty()) {
            map.put("phone_id", str2);
        }
        map.put("event_time", String.valueOf(obtainCurTime()));
        map.put("device_type", DeviceInfoUtils.getDeviceBrand() + " " + DeviceInfoUtils.getDeviceModel());
        StringBuilder sb = new StringBuilder();
        sb.append("Android ");
        sb.append(DeviceInfoUtils.getDeviceAndroidVersion());
        map.put("device_version", sb.toString());
        map.put("net_type", String.valueOf(NetUtil.getNetworkType(CastApp.mContext)));
        map.put("language", DeviceInfoUtils.getSystemLanguage());
        if (CastApp.DEBUG) {
            L.i(TAG, "-------------------------------- REPORT PARAM --------------------------------");
            L.i(TAG, "eventId: " + str);
            L.i(TAG, "-------------------------------- REPORT PARAM --------------------------------");
            L.w(TAG, "debug: ---------- Do Not Report ----------");
            return;
        }
//        MobclickAgent.onEventObject(CastApp.mContext, str, map);
//        reportFirebase(str, map);
    }

//    private static void reportFirebase(String str, Map<String, Object> map) {
//        if (mFirebaseAnalytics == null) {
//            mFirebaseAnalytics = FirebaseAnalytics.getInstance(CastApp.mContext);
//        }
//        Bundle bundle = new Bundle();
//        if (map != null) {
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                bundle.putString(entry.getKey(), entry.getValue().toString());
//            }
//        }
//        mFirebaseAnalytics.logEvent("cast_" + str, bundle);
//        L.i(TAG, "------------------ ReportFirebase: eventId ------------------");
//    }

    private static long obtainCurTime() {
        return System.currentTimeMillis();
    }

    private static boolean isReportEventId(String str) {
        List<String> format2List = format2List((String) SPUtils.get(CastApp.mContext, SPContracts.REPORT_EVENT_IDS, ""));
        if (format2List == null || format2List.isEmpty() || !format2List.contains(str)) {
            saveEventIds(str);
            return false;
        }
        L.i(TAG, "这位用户的这个事件已经上报过了: " + str);
        return true;
    }

    private static void saveEventIds(String str) {
        String str2 = (String) SPUtils.get(CastApp.mContext, SPContracts.REPORT_EVENT_IDS, "");
        if (str2 != null && !str2.isEmpty()) {
            str = str2 + ";" + str;
        }
        SPUtils.put(CastApp.mContext, SPContracts.REPORT_EVENT_IDS, str);
    }

    private static List<String> format2List(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return Arrays.asList(str.split(";").clone());
    }
}
