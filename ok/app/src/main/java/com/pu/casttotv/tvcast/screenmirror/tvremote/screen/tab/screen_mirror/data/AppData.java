package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/* loaded from: classes4.dex */
public final class AppData {
    private volatile boolean isActivityRunning;
    private volatile boolean isStreamRunning;
    private final byte[] mIconBytes;
    private String mIndexHtmlPage;
    private final String mPinRequestErrorMsg;
    private final String mPinRequestHtmlPage;
    private final WifiManager mWifiManager;
    private final WindowManager mWindowManager;
    private final ConcurrentLinkedDeque<byte[]> mImageQueue = new ConcurrentLinkedDeque<>();
    private final ConcurrentLinkedQueue<Client> mClientQueue = new ConcurrentLinkedQueue<>();
    private final int mDensityDpi = getDensityDpi();

    @SuppressLint("WrongConstant")
    public AppData(Context context) {
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        this.mWifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
        getScale(context);
        this.mPinRequestHtmlPage = getPinRequestHtmlPage(context);
        this.mPinRequestErrorMsg = context.getString(R.string.html_wrong_pin);
        this.mIconBytes = getFavicon(context);
    }

    public void setActivityRunning(boolean z) {
        this.isActivityRunning = z;
    }

    public void setStreamRunning(boolean z) {
        this.isStreamRunning = z;
        MyApplication.getScreenMirrorViewModel().setStreaming(z);
    }

    public ConcurrentLinkedDeque<byte[]> getImageQueue() {
        return this.mImageQueue;
    }

    public ConcurrentLinkedQueue<Client> getClientQueue() {
        return this.mClientQueue;
    }

    public boolean isActivityRunning() {
        return this.isActivityRunning;
    }

    public boolean isStreamRunning() {
        return this.isStreamRunning;
    }

    public WindowManager getWindowsManager() {
        return this.mWindowManager;
    }

    public int getScreenDensity() {
        return this.mDensityDpi;
    }

    public Point getScreenSize() {
        Point point = new Point();
        this.mWindowManager.getDefaultDisplay().getRealSize(point);
        return point;
    }

    public void initIndexHtmlPage(Context context) {
        this.mIndexHtmlPage = getHtml(context, "mirror/index_mirror.html").replaceFirst("BACK_COLOR", String.format("#%06X", Integer.valueOf(MyApplication.getAppPreference().getHTMLBackColor() & 16777215))).replaceFirst("MSG_NO_MJPEG_SUPPORT", context.getString(R.string.html_no_mjpeg_support));
        if (MyApplication.getAppPreference().isDisableMJPEGCheck()) {
            this.mIndexHtmlPage = this.mIndexHtmlPage.replaceFirst("id=mj", "").replaceFirst("id=pmj", "");
        }
    }

    public String getIndexHtml(String str) {
        return this.mIndexHtmlPage.replaceFirst("SCREEN_STREAM_ADDRESS", str);
    }

    public String getPinRequestHtml(boolean z) {
        return this.mPinRequestHtmlPage.replaceFirst("wrong_pin", z ? this.mPinRequestErrorMsg : "&nbsp");
    }

    public byte[] getIcon() {
        return this.mIconBytes;
    }

    public InetAddress getIpAddress() {
        try {
            int ipAddress = this.mWifiManager.getConnectionInfo().getIpAddress();
            return InetAddress.getByAddress(new byte[]{(byte) (ipAddress & 255), (byte) ((ipAddress >> 8) & 255), (byte) ((ipAddress >> 16) & 255), (byte) ((ipAddress >> 24) & 255)});
        } catch (UnknownHostException unused) {
            return null;
        }
    }

    public String getServerAddress() {
        return "http:/" + getIpAddress() + ":" + MyApplication.getAppPreference().getSeverPort();
    }

    public boolean isWiFiConnected() {
        return this.mWifiManager.getConnectionInfo().getIpAddress() != 0;
    }

    private int getDensityDpi() {
        try {
            Display display = mWindowManager.getDefaultDisplay();
            if (display != null) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                this.mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
                return displayMetrics.densityDpi;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private float getScale(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    private String getPinRequestHtmlPage(Context context) {
        return getHtml(context, "mirror/pinrequest.html").replaceFirst("stream_require_pin", context.getString(R.string.html_stream_require_pin)).replaceFirst("enter_pin", context.getString(R.string.html_enter_pin)).replaceFirst("four_digits", context.getString(R.string.html_four_digits)).replaceFirst("submit_text", context.getString(R.string.html_submit_text));
    }

    private String getHtml(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str), StandardCharsets.UTF_8));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine.toCharArray());
            }
            bufferedReader.close();
        } catch (IOException unused) {
        }
        String sb2 = sb.toString();
        sb.setLength(0);
        return sb2;
    }

    private byte[] getFavicon(Context context) {
        try {
            InputStream open = context.getAssets().open("mirror/favicon.png");
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return bArr;
        } catch (IOException unused) {
            return null;
        }
    }
}
