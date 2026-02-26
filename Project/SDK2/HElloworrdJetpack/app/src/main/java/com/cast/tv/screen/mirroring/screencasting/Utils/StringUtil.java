package com.cast.tv.screen.mirroring.screencasting.Utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {
    public static String obtainStrByList(List<String> list) {
        String str = "";
        if (list != null && list.size() > 0) {
            for (String str2 : list) {
                if (!TextUtils.isEmpty(str)) {
                    str = str + ";";
                }
                str = str + str2;
            }
        }
        return str;
    }

    public static List<String> obtainListByStr(String str) {
        String[] split;
        ArrayList arrayList = new ArrayList();
        if (str != null && !TextUtils.isEmpty(str)) {
            for (String str2 : str.split(";")) {
                if (!TextUtils.isEmpty(str2)) {
                    arrayList.add(str2);
                }
            }
        }
        return arrayList;
    }

    public static String formatMPByYP(String str) {
        if (str != null && !str.isEmpty()) {
            try {
                if (str.contains("$")) {
                    String[] split = str.split("\\$");
                    float floatValue = new BigDecimal(Float.parseFloat(split[1]) / 12.0f).setScale(2, 1).floatValue();
                    L.d("XXX", "mpValue = " + floatValue);
                    return "(" + split[0] + "$" + floatValue + "/M)";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
