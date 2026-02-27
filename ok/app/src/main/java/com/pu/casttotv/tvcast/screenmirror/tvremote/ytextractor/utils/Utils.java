package com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model.YTMedia;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String loginCookie = "YSC=yNxWJf1cSa4;VISITOR_INFO1_LIVE=sgfw6SW98bw;GPS=1;SID=2wcm6biEhIBQ7mAlI74wOiKiyb_qgHjGqRda45ja9Uo-R_YGPv7BsMw9LxwWxijRYBbl9Q.;__Secure-3PSID=2wcm6biEhIBQ7mAlI74wOiKiyb_qgHjGqRda45ja9Uo-R_YG7NLzd6XbeKd5v4FdcW0fFw.;HSID=AZUFTUl5YDEbXnxXz;SSID=Al9RcjJhYjDs0mH_8;APISID=gno4Jw69CDDSLOoA/Au2BTJQ9vzFfv6jDi;SAPISID=CS66fqRwUvDy_8qr/ASZJ0MtESshD5jMOH;__Secure-3PAPISID=CS66fqRwUvDy_8qr/ASZJ0MtESshD5jMOH;CONSENT=YES+PK.en+202010;LOGIN_INFO=AFmmF2swRQIhAMeOhq-bNVUVUTnNdJnT37v__UYLzOmcDurH2rGtqiqGAiB7hbo1chMtmz5Bn52kKsnRQl6CsWjy8AaWsDOmPQbh1Q:QUQ3MjNmejE5SmExRzFjamk2Wi0xZDJnNW5VU3gwTUVjdjNCYjFGZUo4R2RHeVZfWEFLM2hSWlNXUGpmdzNuemtSUm5MMWJfLVBiMERzemhFQ3ZCMEFjQm1Ud0M4bGpWMmdiTUtYMHpFcmo2WVY2cklBVDJfSWZjMUphMGZQMThCTmZab3J2ZjhYNmNlLXpuRkNSMjZtOXBWRFpraWN0VHEwZTJFLWZVLUF4YmlJb3hrZzVHNktwc05KRUV5aXhJSzRsYzV2N2REVWhW;SIDCC=AJi4QfHl4irM7tcfniTbzgdTmGax9V8uBSUj8uNcPKqSWJJ00Qyl9jSUL1Yhz5WmPl_AmH2XeQ;__Secure-3PSIDCC=AJi4QfFiBQavkEX4ZGQDhud12UroBrzENEaMttnSkCIMUfwlwjtzs5TUWxuVCE-HW2emhh4o";

    public static List<YTMedia> filterInvalidLinks(List<YTMedia> list) {
        ArrayList arrayList = new ArrayList();
        for (YTMedia yTMedia : list) {
            if (!yTMedia.getUrl().contains("&dur=0.0")) {
                arrayList.add(yTMedia);
            }
        }
        return arrayList;
    }

    public static String extractVideoID(String str) {
        return RegexUtils.hasMatch("(?<=(be/|v=))(.*?)(?=(&|\n| |\\z))", str) ? RegexUtils.matchGroup("(?<=(be/|v=))(.*?)(?=(&|\n| |\\z))", str) : str;
    }

    public static boolean isListContain(List<String> list, String str) {
        for (String str2 : list) {
            if (str != null && str.toLowerCase().contains(str2)) {
                return true;
            }
        }
        return false;
    }

    public static void copyToBoard(String str) {
        ((ClipboardManager) ContextUtils.context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text", str));
    }
}
