package com.co.casttotv.screenmirroring.mirroring.cast.constans;

import com.co.casttotv.screenmirroring.mirroring.cast.models.ConnectModel;
import com.co.casttotv.screenmirroring.mirroring.cast.models.MediaModel;

import java.util.ArrayList;

public class Config {
    public static ArrayList<MediaModel> selectedImageFolderList = new ArrayList<>();
    public static int selectedPosition = 0;

    public static ArrayList<ConnectModel> mDeviceShow = new ArrayList<>();


    public static String parseLongToTime(long j) {
        long j2 = (j / 1000) % 60;
        long j3 = (j / 60000) % 60;
        long j4 = (j / 3600000) % 24;
        return j4 == 0 ? String.format("%02d:%02d", Long.valueOf(j3), Long.valueOf(j2)) : String.format("%02d:%02d:%02d", Long.valueOf(j4), Long.valueOf(j3), Long.valueOf(j2));
    }

}
