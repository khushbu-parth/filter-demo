package com.cast.tv.screen.mirroring.screencasting.Utils;

import android.text.TextUtils;

import java.util.Calendar;

public class TimeUtil {
    private TimeUtil() {
    }

    public static int string2Int(String str) {
        int parseInt;
        int i;
        if (str != null && !TextUtils.isEmpty(str)) {
            try {
                if (str.contains(":")) {
                    String[] split = str.split(":");
                    if (split.length <= 0) {
                        return 0;
                    }
                    if (split.length == 1) {
                        return Integer.parseInt(checkNumber(split[0]));
                    }
                    if (split.length == 2) {
                        int parseInt2 = Integer.parseInt(checkNumber(split[0]));
                        parseInt = Integer.parseInt(checkNumber(split[1]));
                        i = parseInt2 * 60;
                    } else {
                        int parseInt3 = Integer.parseInt(checkNumber(split[0]));
                        int parseInt4 = Integer.parseInt(checkNumber(split[1]));
                        parseInt = Integer.parseInt(checkNumber(split[2]));
                        i = (parseInt3 * 60 * 60) + (parseInt4 * 60);
                    }
                    return i + parseInt;
                }
            } catch (Exception unused) {
                L.e("TimeUtil", "string2Int exception");
            }
        }
        return 0;
    }

    private static String checkNumber(String str) {
        if (str == null || TextUtils.isEmpty(str) || str.equals("NaN")) {
            return "0";
        }
        if (str.contains(".")) {
            return str.split("\\.")[0];
        }
        return str.contains("-") ? str.split("-")[0] : str;
    }

    public static String int2String(int i) {
        String str;
        String str2;
        String str3;
        int i2 = (i / 60) / 60;
        if (i2 < 10) {
            str = "0" + i2;
        } else {
            str = "" + i2;
        }
        String str4 = str + ":";
        int i3 = (i - ((i2 * 60) * 60)) / 60;
        if (i3 < 10) {
            str2 = str4 + "0" + i3;
        } else {
            str2 = str4 + i3;
        }
        String str5 = str2 + ":";
        int i4 = i % 60;
        if (i4 < 10) {
            str3 = str5 + "0" + i4;
        } else {
            str3 = str5 + i4;
        }
        return TextUtils.isEmpty(str3) ? "00:00:00" : str3;
    }

    public static String formatStrTime(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return "00:00";
        }
        String[] split = str.split(":");
        if (split.length > 0 && split[0].length() == 1) {
            str = "0" + str;
        }
        if (split.length <= 2) {
            return str;
        }
        String str2 = split[0];
        if (!str2.equals("0") && !str2.equals("00")) {
            return str;
        }
        return split[1] + ":" + split[2];
    }

    public static int obtainCurDateInt() {
        String str;
        String str2;
        try {
            Calendar calendar = Calendar.getInstance();
            int i = calendar.get(1);
            int i2 = calendar.get(2) + 1;
            int i3 = calendar.get(5);
            String valueOf = String.valueOf(i);
            if (i2 < 10) {
                str = valueOf + "0" + i2;
            } else {
                str = valueOf + i2;
            }
            if (i3 < 10) {
                str2 = str + "0" + i3;
            } else {
                str2 = str + i3;
            }
            return Integer.parseInt(str2);
        } catch (NumberFormatException unused) {
            return 0;
        }
    }
}
