package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.connectsdk.device.DefaultConnectableDeviceStore;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.database.DeviceDatabase;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.model.Device;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    public static long updateDevice(Context context, Device device) {
        DeviceDatabase deviceDatabase = new DeviceDatabase(context);
        SQLiteDatabase writableDatabase = deviceDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("host", device.getHost());
        contentValues.put("is_tv", device.getIsTv());
        contentValues.put("is_stick", device.getIsStick());
        if (device.getCustomUserDeviceName() != null) {
            contentValues.put("custom_user_device_name", device.getCustomUserDeviceName());
        }
        long update = (long) writableDatabase.update(DefaultConnectableDeviceStore.KEY_DEVICES, contentValues, "serial_number = ?", new String[]{device.getSerialNumber()});
        writableDatabase.close();
        deviceDatabase.close();
        return update;
    }

    public static Device getDevice(Context context, String str) {
        Device device = null;
        if (str == null) {
            return null;
        }
        DeviceDatabase deviceDatabase = new DeviceDatabase(context);
        SQLiteDatabase writableDatabase = deviceDatabase.getWritableDatabase();
        Cursor query = writableDatabase.query(DefaultConnectableDeviceStore.KEY_DEVICES, null, "serial_number = ?", new String[]{str}, null, null, null);
        if (query.moveToNext()) {
            device = parseDevice(query);
        }
        query.close();
        writableDatabase.close();
        deviceDatabase.close();
        return device;
    }

    public static List<Device> getAllDevices(Context context) {
        ArrayList arrayList = new ArrayList();
        DeviceDatabase deviceDatabase = new DeviceDatabase(context);
        SQLiteDatabase writableDatabase = deviceDatabase.getWritableDatabase();
        Cursor query = writableDatabase.query(DefaultConnectableDeviceStore.KEY_DEVICES, null, null, null, null, null, null);
        while (query.moveToNext()) {
            arrayList.add(parseDevice(query));
        }
        query.close();
        writableDatabase.close();
        deviceDatabase.close();
        return arrayList;
    }

    @SuppressLint("Range")
    private static Device parseDevice(Cursor cursor) {
        Device device = new Device();
        device.setHost(cursor.getString(cursor.getColumnIndex("host")));
        device.setUdn(cursor.getString(cursor.getColumnIndex("udn")));
        device.setSerialNumber(cursor.getString(cursor.getColumnIndex("serial_number")));
        device.setDeviceId(cursor.getString(cursor.getColumnIndex("device_id")));
        device.setVendorName(cursor.getString(cursor.getColumnIndex("vendor_name")));
        device.setModelNumber(cursor.getString(cursor.getColumnIndex("model_number")));
        device.setModelName(cursor.getString(cursor.getColumnIndex("model_name")));
        device.setWifiMac(cursor.getString(cursor.getColumnIndex("wifi_mac")));
        device.setEthernetMac(cursor.getString(cursor.getColumnIndex("ethernet_mac")));
        device.setNetworkType(cursor.getString(cursor.getColumnIndex("network_type")));
        device.setUserDeviceName(cursor.getString(cursor.getColumnIndex("user_device_name")));
        device.setSoftwareVersion(cursor.getString(cursor.getColumnIndex("software_version")));
        device.setSoftwareBuild(cursor.getString(cursor.getColumnIndex("software_build")));
        device.setSecureDevice(cursor.getString(cursor.getColumnIndex("secure_device")));
        device.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
        device.setCountry(cursor.getString(cursor.getColumnIndex("country")));
        device.setLocale(cursor.getString(cursor.getColumnIndex("locale")));
        device.setTimeZone(cursor.getString(cursor.getColumnIndex("time_zone")));
        device.setTimeZoneOffset(cursor.getString(cursor.getColumnIndex("time_zone_offset")));
        device.setPowerMode(cursor.getString(cursor.getColumnIndex("power_mode")));
        device.setSupportsSuspend(cursor.getString(cursor.getColumnIndex("supports_suspend")));
        device.setSupportsFindRemote(cursor.getString(cursor.getColumnIndex("supports_find_remote")));
        device.setSupportsAudioGuide(cursor.getString(cursor.getColumnIndex("supports_audio_guide")));
        device.setDeveloperEnabled(cursor.getString(cursor.getColumnIndex("developer_enabled")));
        device.setKeyedDeveloperId(cursor.getString(cursor.getColumnIndex("keyed_developer_id")));
        device.setSearchEnabled(cursor.getString(cursor.getColumnIndex("search_enabled")));
        device.setVoiceSearchEnabled(cursor.getString(cursor.getColumnIndex("voice_search_enabled")));
        device.setNotificationsEnabled(cursor.getString(cursor.getColumnIndex("notifications_enabled")));
        device.setNotificationsFirstUse(cursor.getString(cursor.getColumnIndex("notifications_first_use")));
        device.setSupportsPrivateListening(cursor.getString(cursor.getColumnIndex("supports_private_listening")));
        device.setHeadphonesConnected(cursor.getString(cursor.getColumnIndex("headphones_connected")));
        device.setIsTv(cursor.getString(cursor.getColumnIndex("is_tv")));
        device.setIsStick(cursor.getString(cursor.getColumnIndex("is_stick")));
        device.setCustomUserDeviceName(cursor.getString(cursor.getColumnIndex("custom_user_device_name")));
        return device;
    }
}
