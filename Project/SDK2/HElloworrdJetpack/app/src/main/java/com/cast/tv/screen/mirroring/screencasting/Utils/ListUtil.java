package com.cast.tv.screen.mirroring.screencasting.Utils;

import java.util.List;

public class ListUtil {
    private ListUtil() {
    }

    public static int getSize(List list) {
        if (list == null) {
            return 0;
        }
        return list.size();
    }
}
