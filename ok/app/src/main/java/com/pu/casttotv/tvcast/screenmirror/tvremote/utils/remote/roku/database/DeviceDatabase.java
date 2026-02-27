package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.connectsdk.device.DefaultConnectableDeviceStore;

public class DeviceDatabase extends SQLiteOpenHelper {
    private static final String[] DEVICES_TABLE_ALTER_VERSION_FOUR = {"ALTER TABLE devices ADD COLUMN custom_user_device_name TEXT;"};
    private static final String[] DEVICES_TABLE_ALTER_VERSION_THREE = {"ALTER TABLE devices ADD COLUMN is_tv TEXT;", "ALTER TABLE devices ADD COLUMN is_stick TEXT;"};
    private static final String[] DEVICES_TABLE_ALTER_VERSION_TWO = {"ALTER TABLE devices ADD COLUMN supports_suspend TEXT;", "ALTER TABLE devices ADD COLUMN supports_find_remote TEXT;", "ALTER TABLE devices ADD COLUMN supports_audio_guide TEXT;", "ALTER TABLE devices ADD COLUMN supports_private_listening TEXT;"};

    public DeviceDatabase(Context context) {
        super(context, DefaultConnectableDeviceStore.KEY_DEVICES, (SQLiteDatabase.CursorFactory) null, 4);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE devices (host TEXT,udn TEXT,serial_number TEXT PRIMARY KEY,device_id TEXT,vendor_name TEXT,model_number TEXT,model_name TEXT,wifi_mac TEXT,ethernet_mac TEXT,network_type TEXT,user_device_name TEXT,software_version TEXT,software_build TEXT,secure_device TEXT,language TEXT,country TEXT,locale TEXT,time_zone TEXT,time_zone_offset TEXT,power_mode TEXT,supports_suspend TEXT,supports_find_remote TEXT,supports_audio_guide TEXT,developer_enabled TEXT,keyed_developer_id TEXT,search_enabled TEXT,voice_search_enabled TEXT,notifications_enabled TEXT,notifications_first_use TEXT,supports_private_listening TEXT,headphones_connected TEXT,is_tv TEXT,is_stick TEXT,custom_user_device_name TEXT);");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        int i3 = 0;
        if (i2 <= 2) {
            int i4 = 0;
            while (true) {
                String[] strArr = DEVICES_TABLE_ALTER_VERSION_TWO;
                if (i4 >= strArr.length) {
                    break;
                }
                sQLiteDatabase.execSQL(strArr[i4]);
                i4++;
            }
        }
        if (i2 <= 3) {
            int i5 = 0;
            while (true) {
                String[] strArr2 = DEVICES_TABLE_ALTER_VERSION_THREE;
                if (i5 >= strArr2.length) {
                    break;
                }
                sQLiteDatabase.execSQL(strArr2[i5]);
                i5++;
            }
        }
        if (i2 <= 4) {
            while (true) {
                String[] strArr3 = DEVICES_TABLE_ALTER_VERSION_FOUR;
                if (i3 < strArr3.length) {
                    sQLiteDatabase.execSQL(strArr3[i3]);
                    i3++;
                } else {
                    return;
                }
            }
        }
    }
}
