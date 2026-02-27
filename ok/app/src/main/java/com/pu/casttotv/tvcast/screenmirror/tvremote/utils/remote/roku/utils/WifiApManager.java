package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.model.ClientScanResult;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class WifiApManager {
    private Context context;
    private final WifiManager mWifiManager;

    @SuppressLint("WrongConstant")
    public WifiApManager(Context context2) {
        this.context = context2;
        this.mWifiManager = (WifiManager) context2.getSystemService("wifi");
    }

    public Constants$WIFI_AP_STATE getWifiApState() {
        try {
            int intValue = ((Integer) this.mWifiManager.getClass().getMethod("getWifiApState", new Class[0]).invoke(this.mWifiManager, new Object[0])).intValue();
            if (intValue >= 10) {
                intValue -= 10;
            }
            return ((Constants$WIFI_AP_STATE[]) Constants$WIFI_AP_STATE.class.getEnumConstants())[intValue];
        } catch (Exception unused) {
            WifiApManager.class.toString();
            return Constants$WIFI_AP_STATE.WIFI_AP_STATE_FAILED;
        }
    }

    public boolean isWifiApEnabled() {
        return getWifiApState() == Constants$WIFI_AP_STATE.WIFI_AP_STATE_ENABLED;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0073 A[SYNTHETIC, Splitter:B:32:0x0073] */
    public ArrayList<ClientScanResult> getClientList(boolean z, int i) throws Throwable {
        Throwable th;
        Exception e2;
        ArrayList<ClientScanResult> arrayList = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader("/proc/net/arp"));
            while (true) {
                try {
                    String readLine = bufferedReader2.readLine();
                    if (readLine != null) {
                        String[] split = readLine.split(" +");
                        if (split != null && split.length >= 4 && split[3].matches("..:..:..:..:..:..")) {
                            boolean isReachable = InetAddress.getByName(split[0]).isReachable(i);
                            if (!z || isReachable) {
                                arrayList.add(new ClientScanResult(split[0], split[3], split[5], isReachable));
                            }
                        }
                    } else {
                        break;
                    }
                } catch (Exception e4) {
                    e2 = e4;
                    bufferedReader = bufferedReader2;
                    try {
                        WifiApManager.class.toString();
                        e2.toString();
                        if (bufferedReader != null) {
                        }
                        return arrayList;
                    } catch (Throwable th2) {
                        th = th2;
                        if (bufferedReader != null) {
                        }
                        try {
                            throw th;
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    bufferedReader = bufferedReader2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e5) {
                            WifiApManager.class.toString();
                            e5.getMessage();
                        }
                    }
                    throw th;
                }
            }
            bufferedReader2.close();
        } catch (Exception e6) {
            e2 = e6;
            WifiApManager.class.toString();
            e2.toString();
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return arrayList;
        }
        return arrayList;
    }
}
