package com.lib.screening;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.view.ContextThemeWrapper;

import com.lib.nginxserver.nginx.NginxHelper;
import com.lib.screening.listener.DLNARegistryListener;
import com.lib.screening.listener.DLNAStateCallback;
import com.lib.screening.log.AndroidLoggingHandler;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import fi.iki.elonen.SimpleWebServer;

public final class DLNAManager {
    private static final String LOCAL_HTTP_SERVER_PORT = "9578";
    private static final String TAG = "DLNAManager";
    private static boolean isDebugMode;
    private BroadcastReceiver mBroadcastReceiver;
    private Context mContext;
    private Handler mHandler;
    private RegistryListener mRegistryListener;
    private ServiceConnection mServiceConnection;
    private DLNAStateCallback mStateCallback;
    private AndroidUpnpService mUpnpService;
    private List<DLNARegistryListener> registryListenerList;

    DLNAManager(AnonymousClass1 anonymousClass1) {
        this();
    }

    private DLNAManager() {
        AndroidLoggingHandler.injectJavaLogger();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.registryListenerList = new ArrayList();
        this.mRegistryListener = new AnonymousClass1();
        this.mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NetworkInfo networkInfo;
                if (intent == null || !TextUtils.equals(intent.getAction(), "android.net.conn.CONNECTIVITY_CHANGE") || (networkInfo = DLNAManager.getNetworkInfo(context)) == null || networkInfo.getType() != 1) {
                    return;
                }
                DLNAManager.this.initLocalMediaServer();
            }
        };
    }

    public static DLNAManager getInstance() {
        return DLNAManagerCreator.getMLNAManager();
    }

    public static String getLocalIpStr(Context context) {
        WifiInfo connectionInfo = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        return connectionInfo == null ? "" : intToIpAddress(connectionInfo.getIpAddress());
    }

    public static String intToIpAddress(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return null;
        }
        return connectivityManager.getActiveNetworkInfo();
    }

    public static String tryTransformLocalMediaAddressToLocalHttpServerAddress(Context context, String str) {
        logD("tryTransformLocalMediaAddressToLocalHttpServerAddress ,sourceUrl : " + str);
        if (!TextUtils.isEmpty(str) && isLocalMediaAddress(str)) {
            String str2 = getLocalHttpServerAddress(context) + str.replace(Environment.getExternalStorageDirectory().getAbsolutePath(), "");
            logD("tryTransformLocalMediaAddressToLocalHttpServerAddress ,newSourceUrl : " + str2);
            try {
                String[] str3 = str2.split("/");
                String lastOne = str3[str3.length - 1];
                str2 = str2.replace(lastOne, URLEncoder.encode(lastOne, "UTF-8"));
                str2 = str2.replaceAll("\\s", "%20");
                str2 = str2.replace("[!@#$%^&*()_-+={}?]*", "");
                logD("tryTransformLocalMediaAddressToLocalHttpServerAddress ,encodeNewSourceUrl : " + str2);
                return str2;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return str2;
            }
        }
        return str;
    }

    private static boolean isLocalMediaAddress(String str) {
        return !TextUtils.isEmpty(str) && !str.startsWith("http://") && !str.startsWith("https://") && str.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public static String getLocalHttpServerAddress(Context context) {
        return "http://" + getLocalIpStr(context) + ":" + LOCAL_HTTP_SERVER_PORT;
    }

    public static void setIsDebugMode(boolean z) {
        isDebugMode = z;
    }

    static void logV(String str) {
        logV(TAG, str);
    }

    public static void logV(String str, String str2) {
        Log.v(str, str2);
    }

    static void logD(String str) {
        logD(TAG, str);
    }

    public static void logD(String str, String str2) {
        Log.d(str, str2);
    }

    static void logI(String str) {
        logI(TAG, str);
    }

    public static void logI(String str, String str2) {
        Log.i(str, str2);
    }

    static void logW(String str) {
        logW(TAG, str);
    }

    public static void logW(String str, String str2) {
        Log.w(str, str2);
    }

    public static void logE(String str) {
        logE(TAG, str);
    }

    public static void logE(String str, String str2) {
        logE(str, str2, null);
    }

    public static void logE(String str, Throwable th) {
        logE(TAG, str, th);
    }

    public static void logE(String str, String str2, Throwable th) {
        if (th != null) {
            Log.e(str, str2, th);
        } else {
            Log.e(str, str2);
        }
    }

    public void init(Context context) {
        init(context, null);
    }

    public void init(Context context, DLNAStateCallback dLNAStateCallback) {
        if (this.mContext != null) {
            logW("ReInit DLNAManager");
            return;
        }
        if ((context instanceof ContextThemeWrapper) || (context instanceof android.view.ContextThemeWrapper)) {
            this.mContext = context.getApplicationContext();
        } else {
            this.mContext = context;
        }
        this.mStateCallback = dLNAStateCallback;
        NginxHelper.installNginxServer(this.mContext);
        initConnection();
        registerBroadcastReceiver();
    }

    private void initConnection() {
        this.mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                DLNAManager.this.mUpnpService = (AndroidUpnpService) iBinder;
                DLNAManager.this.mUpnpService.getRegistry().addListener(DLNAManager.this.mRegistryListener);
                DLNAManager.this.mUpnpService.getControlPoint().search();
                if (DLNAManager.this.mStateCallback != null) {
                    DLNAManager.this.mStateCallback.onConnected();
                }
                DLNAManager.logD("onServiceConnected");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                DLNAManager.this.mUpnpService = null;
                if (DLNAManager.this.mStateCallback != null) {
                    DLNAManager.this.mStateCallback.onDisconnected();
                }
                DLNAManager.logD("onServiceDisconnected");
            }
        };
        this.mContext.bindService(new Intent(this.mContext, DLNABrowserService.class), this.mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void initLocalMediaServer() {
        checkConfig();
        try {
            NginxHelper.stopNginxServer();
            NginxHelper.startNginxServer();
            System.setIn(new PipedInputStream(new PipedOutputStream()));
            new Thread(new Runnable() {
                @Override
                public final void run() {
                    DLNAManager.this.initLocalMediaServer$0$DLNAManager();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
            logE("initLocalLinkService failure", e);
        }
    }

    public void initLocalMediaServer$0$DLNAManager() {
        String localIpStr = getLocalIpStr(this.mContext);
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        SimpleWebServer.main(new String[]{"--host", localIpStr, "--port", LOCAL_HTTP_SERVER_PORT, "--dir", absolutePath});
        logD("initLocalLinkService success,localIpAddress : " + localIpStr + ",localVideoRootPath : " + absolutePath);
    }

    private void registerBroadcastReceiver() {
        checkConfig();
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterBroadcastReceiver() {
        checkConfig();
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
    }

    public void refreshDevice() {
        AndroidUpnpService androidUpnpService;
        if (this.mContext == null || (androidUpnpService = this.mUpnpService) == null) {
            return;
        }
        androidUpnpService.getRegistry().removeAllLocalDevices();
        this.mUpnpService.getRegistry().removeAllRemoteDevices();
        this.mUpnpService.getControlPoint().search();
    }

    public void registerListener(DLNARegistryListener dLNARegistryListener) {
        checkConfig();
        checkPrepared();
        if (dLNARegistryListener == null) {
            return;
        }
        this.registryListenerList.add(dLNARegistryListener);
        dLNARegistryListener.onDeviceChanged(this.mUpnpService.getRegistry().getDevices());
    }

    public void unregisterListener(DLNARegistryListener dLNARegistryListener) {
        checkConfig();
        checkPrepared();
        if (dLNARegistryListener == null) {
            return;
        }
        this.mUpnpService.getRegistry().removeListener(dLNARegistryListener);
        this.registryListenerList.remove(dLNARegistryListener);
    }

    public void startBrowser() {
        checkConfig();
        checkPrepared();
        this.mUpnpService.getRegistry().addListener(this.mRegistryListener);
        this.mUpnpService.getControlPoint().search();
    }

    public void stopBrowser() {
        checkConfig();
        checkPrepared();
        this.mUpnpService.getRegistry().removeListener(this.mRegistryListener);
    }

    public void destroy() {
        if (this.mContext == null) {
            return;
        }
        this.registryListenerList.clear();
        unregisterBroadcastReceiver();
//        SimpleWebServer.stopServer();
        stopBrowser();
        AndroidUpnpService androidUpnpService = this.mUpnpService;
        if (androidUpnpService != null) {
            androidUpnpService.getRegistry().removeListener(this.mRegistryListener);
            this.mUpnpService.getRegistry().shutdown();
        }
        ServiceConnection serviceConnection = this.mServiceConnection;
        if (serviceConnection != null) {
            this.mContext.unbindService(serviceConnection);
            this.mServiceConnection = null;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        this.registryListenerList = null;
        this.mRegistryListener = null;
        this.mBroadcastReceiver = null;
        this.mStateCallback = null;
        this.mContext = null;
        DLNAManager unused = DLNAManagerCreator.manager = null;
    }

    private void checkConfig() {
        if (this.mContext != null) {
            return;
        }
        throw new IllegalStateException("Must call init(Context context) at first");
    }

    private void checkPrepared() {
        if (this.mUpnpService != null) {
            return;
        }
        throw new IllegalStateException("Invalid AndroidUpnpService");
    }

    public static class DLNAManagerCreator {
        private static DLNAManager manager;

        private DLNAManagerCreator() {
        }

        public static DLNAManager getMLNAManager() {
            if (manager == null) {
                synchronized (DLNAManagerCreator.class) {
                    if (manager == null) {
                        manager = new DLNAManager(null);
                    }
                }
            }
            return manager;
        }
    }

    public class AnonymousClass1 implements RegistryListener {
        AnonymousClass1() {
        }

        @Override
        public void remoteDeviceDiscoveryStarted(final Registry registry, final RemoteDevice remoteDevice) {
            DLNAManager.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass1.this.remoteDeviceDiscoveryStarted$0$DLNAManager$1(registry, remoteDevice);
                }
            });
        }

        public void remoteDeviceDiscoveryStarted$0$DLNAManager$1(Registry registry, RemoteDevice remoteDevice) {
            synchronized (DLNAManager.class) {
                for (DLNARegistryListener dLNARegistryListener : DLNAManager.this.registryListenerList) {
                    dLNARegistryListener.remoteDeviceDiscoveryStarted(registry, remoteDevice);
                }
            }
        }

        @Override
        public void remoteDeviceDiscoveryFailed(final Registry registry, final RemoteDevice remoteDevice, final Exception exc) {
            DLNAManager.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass1.this.remoteDeviceDiscoveryFailed$1$DLNAManager$1(registry, remoteDevice, exc);
                }
            });
        }

        public void remoteDeviceDiscoveryFailed$1$DLNAManager$1(Registry registry, RemoteDevice remoteDevice, Exception exc) {
            synchronized (DLNAManager.class) {
                for (DLNARegistryListener dLNARegistryListener : DLNAManager.this.registryListenerList) {
                    dLNARegistryListener.remoteDeviceDiscoveryFailed(registry, remoteDevice, exc);
                }
            }
        }

        @Override
        public void remoteDeviceAdded(final Registry registry, final RemoteDevice remoteDevice) {
            DLNAManager.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass1.this.remoteDeviceAdded$2$DLNAManager$1(registry, remoteDevice);
                }
            });
        }

        public void remoteDeviceAdded$2$DLNAManager$1(Registry registry, RemoteDevice remoteDevice) {
            synchronized (DLNAManager.class) {
                for (DLNARegistryListener dLNARegistryListener : DLNAManager.this.registryListenerList) {
                    dLNARegistryListener.remoteDeviceAdded(registry, remoteDevice);
                }
            }
        }

        @Override
        public void remoteDeviceUpdated(final Registry registry, final RemoteDevice remoteDevice) {
            DLNAManager.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass1.this.remoteDeviceUpdated$3$DLNAManager$1(registry, remoteDevice);
                }
            });
        }

        public void remoteDeviceUpdated$3$DLNAManager$1(Registry registry, RemoteDevice remoteDevice) {
            synchronized (DLNAManager.class) {
                for (DLNARegistryListener dLNARegistryListener : DLNAManager.this.registryListenerList) {
                    dLNARegistryListener.remoteDeviceUpdated(registry, remoteDevice);
                }
            }
        }

        @Override
        public void remoteDeviceRemoved(final Registry registry, final RemoteDevice remoteDevice) {
            DLNAManager.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass1.this.remoteDeviceRemoved$4$DLNAManager$1(registry, remoteDevice);
                }
            });
        }

        public void remoteDeviceRemoved$4$DLNAManager$1(Registry registry, RemoteDevice remoteDevice) {
            synchronized (DLNAManager.class) {
                for (DLNARegistryListener dLNARegistryListener : DLNAManager.this.registryListenerList) {
                    dLNARegistryListener.remoteDeviceRemoved(registry, remoteDevice);
                }
            }
        }

        @Override
        public void localDeviceAdded(final Registry registry, final LocalDevice localDevice) {
            DLNAManager.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    DeviceDisplay deviceDisplay = new DeviceDisplay(localDevice);
                    AnonymousClass1.this.localDeviceAdded$5$DLNAManager$1(registry, localDevice);
                }
            });
        }

        public void localDeviceAdded$5$DLNAManager$1(Registry registry, LocalDevice localDevice) {
            synchronized (DLNAManager.class) {
                for (DLNARegistryListener dLNARegistryListener : DLNAManager.this.registryListenerList) {
                    dLNARegistryListener.localDeviceAdded(registry, localDevice);
                }
            }
        }

        @Override
        public void localDeviceRemoved(final Registry registry, final LocalDevice localDevice) {
            DLNAManager.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass1.this.localDeviceRemoved$6$DLNAManager$1(registry, localDevice);
                }
            });
        }

        public void localDeviceRemoved$6$DLNAManager$1(Registry registry, LocalDevice localDevice) {
            synchronized (DLNAManager.class) {
                for (DLNARegistryListener dLNARegistryListener : DLNAManager.this.registryListenerList) {
                    dLNARegistryListener.localDeviceRemoved(registry, localDevice);
                }
            }
        }

        @Override
        public void beforeShutdown(final Registry registry) {
            DLNAManager.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass1.this.beforeShutdown$7$DLNAManager$1(registry);
                }
            });
        }

        public void beforeShutdown$7$DLNAManager$1(Registry registry) {
            synchronized (DLNAManager.class) {
                for (DLNARegistryListener dLNARegistryListener : DLNAManager.this.registryListenerList) {
                    dLNARegistryListener.beforeShutdown(registry);
                }
            }
        }

        @Override
        public void afterShutdown() {
            DLNAManager.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass1.this.afterShutdown$8$DLNAManager$1();
                }
            });
        }

        public void afterShutdown$8$DLNAManager$1() {
            synchronized (DLNAManager.class) {
                for (DLNARegistryListener dLNARegistryListener : DLNAManager.this.registryListenerList) {
                    dLNARegistryListener.afterShutdown();
                }
            }
        }
    }


}
