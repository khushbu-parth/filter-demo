package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.google.android.gms.cast.CredentialsData;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.filter.HttpCacheFilter;
import com.yanzhenjie.andserver.website.AssetsWebsite;

import org.greenrobot.eventbus.EventBus;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WebServer extends Service {
    public static String localIpText;
    public static int portWS = 8695;
    private AssetManager mAssetManager;
    private Server mServer;
    private ServerServiceListener serverServiceListener;
    private WsServer wsServer;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public int port = 6699;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        try {
            localIpText = NetUtils.getWifiIp(getApplication());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 26) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }
        this.compositeDisposable.add(RxBus.getDefault().toObservable(String.class).subscribeOn(Schedulers.io()).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String str) throws Exception {
                Log.e("###TAG", "accept: " + str);
                WebServer.this.wsServer.broadcast(str);
            }
        }).subscribe());
        this.mAssetManager = getAssets();
    }

    @SuppressLint({"NewApi", "LocalSuppress", "WrongConstant"})
    private void startMyOwnForeground() {
        NotificationChannel notificationChannel = new NotificationChannel("com.example.simpleapp", "My Background Service", 0);
        notificationChannel.setLightColor(-16776961);
        notificationChannel.setLockscreenVisibility(0);
        ((NotificationManager) getSystemService("notification")).createNotificationChannel(notificationChannel);
        startForeground(2, new NotificationCompat.Builder(this, "com.example.simpleapp").setOngoing(true).setSmallIcon(R.drawable.ic_icon_channel_on).setContentTitle("App is running in background").setPriority(1).setCategory("service").build());
    }

    @SuppressLint("WrongConstant")
    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        startServer();
        return 1;
    }

    @Override // android.app.Service
    public void onDestroy() {
        stopServer();
        super.onDestroy();
    }

    private void startServer() {
        try {
            createWebServer();
            this.mServer.startup();
            createWsServer();
            this.wsServer.start();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void createWebServer() {
        try {
            this.mServer = AndServer.serverBuilder().inetAddress(NetUtils.getLocalIPAddress()).port(this.port).timeout(10, TimeUnit.SECONDS).website(new AssetsWebsite(this.mAssetManager, CredentialsData.CREDENTIALS_TYPE_WEB)).registerHandler("/wsinfo", new RequestWsInfoHandler()).filter(new HttpCacheFilter()).listener(new Server.ServerListener() {
                @Override
                public void onStarted() {
                    Log.e("###TAG", "WebServer onStarted: " );
                    if (WebServer.this.serverServiceListener != null) {
                        WebServer.this.serverServiceListener.onServerStatusChanged(true);
                    }
                    InetAddress localIPAddress = NetUtils.getLocalIPAddress();
                    if (localIPAddress != null) {
                        WebBroadCast.onServerStart(WebServer.this, localIPAddress.getHostAddress(), WebServer.this.port);
                    }
                }

                @Override
                public void onStopped() {
                    Log.e("###TAG", "WebServer onStopped: " );
                    if (WebServer.this.serverServiceListener != null) {
                        WebServer.this.serverServiceListener.onServerStatusChanged(false);
                    }
                }

                @Override
                public void onError(Exception exc) {
                    exc.printStackTrace();
                    Log.e("###TAG", "WebServer onError: " );
                    if (exc.getMessage() == null || !exc.getMessage().contains("Address already in use")) {
                        if (WebServer.this.serverServiceListener == null) {
                            return;
                        }
                        WebServer.this.serverServiceListener.onWebServerError(0);
                        return;
                    }
                    WebServer.this.port = NetUtils.getRandomPort();
                    WebServer.this.createWebServer();
                    StringBuilder sb = new StringBuilder();
                    sb.append("onWebServerError: already change random port ");
                    sb.append(WebServer.this.port);
                    WebServer.this.mServer.startup();
                    if (WebServer.this.serverServiceListener == null) {
                        return;
                    }
                    WebServer.this.serverServiceListener.onWebServerError(1);
                }
            }).build();
            StringBuilder sb = new StringBuilder();
            sb.append("createWebServer: ");
            sb.append(this.port);
        } catch (Exception e2) {
            Log.e("###TAG", "WebServer Exception: " );
            e2.printStackTrace();
        }
    }

    public void stopServer() {
        try {
            EventBus.getDefault().post(new MessageEvent("KEY_DISCONNECTED_WEB"));
            Server server = this.mServer;
            if (server == null) {
                return;
            }
            server.shutdown();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void createWsServer() {
        try {
            WsServer init = WsServer.init("0.0.0.0", portWS);
            this.wsServer = init;
            init.setListener(new WsServerListener() {
                @Override
                public void onWsServerStatusChanged(boolean z) {

                }

                @Override
                public void onWsServerError(int i) {
                    if (i != 1) {
                        if (WebServer.this.serverServiceListener != null) {
                            WebServer.this.serverServiceListener.onWsServerError(i);
                        }
                        EventBus.getDefault().post(new MessageEvent("KEY_CONNECT_ERROR"));
                        return;
                    }
                    WebServer.portWS = NetUtils.getRandomPort();
                    WebServer.this.createWsServer();
                    StringBuilder sb = new StringBuilder();
                    sb.append("onWsServerError: already change random port ");
                    sb.append(WebServer.portWS);
                    WebServer.this.wsServer.start();
                }

                @Override
                public void onWsServerConnChanged(List<String> list) {
                    if (WebServer.this.serverServiceListener != null) {
                        WebServer.this.serverServiceListener.onWsServerConnChanged(list);
                    }
                    EventBus.getDefault().post(new MessageEvent("KEY_CONNECTED_WEB"));
                }
            });
        } catch (Exception e2) {
            Log.e("###TAG", "createWsServer: "+e2.getMessage() );
            e2.printStackTrace();
        }
    }
}