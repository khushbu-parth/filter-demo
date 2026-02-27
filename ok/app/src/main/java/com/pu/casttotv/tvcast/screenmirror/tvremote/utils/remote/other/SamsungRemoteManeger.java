package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.connectsdk.service.NetcastTVService;
import com.connectsdk.service.airplay.PListParser;
import com.ironsource.mediationsdk.utils.IronSourceConstants;
import java.io.PrintStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.json.JSONException;
import org.json.JSONObject;

public class SamsungRemoteManeger {
    private static Map<String, String> keyMappings;
    private Context context;
    private GetAllChannelListener getAllChannelListener;
    private String ip;
    private boolean isConnected = false;
    public String name = "SamsungRemoteTV";
    private OkHttpClient okHttpClient;
    private int port;
    private SamsungConnectListener samsungConnectListener;
    private SamsungSocketListener samsungSocketListener;
    private SharedPreferences sharedPreferences;
    private String token = "";
    private WebSocket webSocket;

    public interface GetAllChannelListener {
        void onFail(String str);

        void onSuccess(String str);
    }

    public interface SamsungConnectListener {
        void onFailure(String str);

        void onSuccess();
    }

    static {
        HashMap hashMap = new HashMap();
        keyMappings = hashMap;
        hashMap.put(ButtonKeyCode.POWER.getValue(), "KEY_POWER");
        keyMappings.put(ButtonKeyCode.DPAD_UP.getValue(), "KEY_UP");
        keyMappings.put(ButtonKeyCode.DPAD_DOWN.getValue(), "KEY_DOWN");
        keyMappings.put(ButtonKeyCode.DPAD_LEFT.getValue(), "KEY_LEFT");
        keyMappings.put(ButtonKeyCode.DPAD_RIGHT.getValue(), "KEY_RIGHT");
        keyMappings.put(ButtonKeyCode.CHANNEL_UP.getValue(), "KEY_CHUP");
        keyMappings.put(ButtonKeyCode.CHANNEL_DOWN.getValue(), "KEY_CHDOWN");
        keyMappings.put(ButtonKeyCode.ENTER.getValue(), "KEY_ENTER");
        keyMappings.put(ButtonKeyCode.BACK.getValue(), "KEY_RETURN");
        keyMappings.put(ButtonKeyCode.TV_CONTENTS_MENU.getValue(), "KEY_CH_LIST");
        keyMappings.put(ButtonKeyCode.MENU.getValue(), "KEY_MENU");
        keyMappings.put(ButtonKeyCode.TV.getValue(), "KEY_SOURCE");
        keyMappings.put(ButtonKeyCode.GUIDE.getValue(), "KEY_GUIDE");
        keyMappings.put(ButtonKeyCode.SETTINGS.getValue(), "KEY_TOOLS");
        keyMappings.put(ButtonKeyCode.INFO.getValue(), "KEY_INFO");
        keyMappings.put(ButtonKeyCode.PROG_RED.getValue(), "KEY_RED");
        keyMappings.put(ButtonKeyCode.PROG_GREEN.getValue(), "KEY_GREEN");
        keyMappings.put(ButtonKeyCode.PROG_YELLOW.getValue(), "KEY_YELLOW");
        keyMappings.put(ButtonKeyCode.PROG_BLUE.getValue(), "KEY_BLUE");
        keyMappings.put(ButtonKeyCode.MEDIA_NEXT.getValue(), "KEY_PANNEL_CHDOWN");
        keyMappings.put(ButtonKeyCode.VOLUME_UP.getValue(), "KEY_VOLUP");
        keyMappings.put(ButtonKeyCode.VOLUME_DOWN.getValue(), "KEY_VOLDOWN");
        keyMappings.put(ButtonKeyCode.VOLUME_MUTE.getValue(), "KEY_MUTE");
        keyMappings.put(ButtonKeyCode.KEYCODE_0.getValue(), "KEY_0");
        keyMappings.put(ButtonKeyCode.KEYCODE_1.getValue(), "KEY_1");
        keyMappings.put(ButtonKeyCode.KEYCODE_2.getValue(), "KEY_2");
        keyMappings.put(ButtonKeyCode.KEYCODE_3.getValue(), "KEY_3");
        keyMappings.put(ButtonKeyCode.KEYCODE_4.getValue(), "KEY_4");
        keyMappings.put(ButtonKeyCode.KEYCODE_5.getValue(), "KEY_5");
        keyMappings.put(ButtonKeyCode.KEYCODE_6.getValue(), "KEY_6");
        keyMappings.put(ButtonKeyCode.KEYCODE_7.getValue(), "KEY_7");
        keyMappings.put(ButtonKeyCode.KEYCODE_8.getValue(), "KEY_8");
        keyMappings.put(ButtonKeyCode.KEYCODE_9.getValue(), "KEY_9");
        keyMappings.put(ButtonKeyCode.LAST_CHANNEL.getValue(), "KEY_PRECH");
        keyMappings.put(ButtonKeyCode.MEDIA_REWIND.getValue(), "KEY_REWIND");
        keyMappings.put(ButtonKeyCode.MEDIA_RECORD.getValue(), "KEY_REC");
        keyMappings.put(ButtonKeyCode.HOME.getValue(), "KEY_HOME");
        keyMappings.put(ButtonKeyCode.APP_LIST.getValue(), "KEYCODE_ALL_APPS");
        keyMappings.put(ButtonKeyCode.CC.getValue(), "KEY_CAPTION");
        keyMappings.put(ButtonKeyCode.CLEAR.getValue(), "KEY_CLEAR");
        keyMappings.put(ButtonKeyCode.ESCAPE.getValue(), "KEY_EXIT");
        keyMappings.put(ButtonKeyCode.MEDIA_FAST_FORWARD.getValue(), "KEY_FF");
        keyMappings.put(ButtonKeyCode.MEDIA_PLAY.getValue(), "KEY_PLAY");
        keyMappings.put(ButtonKeyCode.MEDIA_PLAY_PAUSE.getValue(), "KEY_PAUSE");
        keyMappings.put(ButtonKeyCode.SLEEP.getValue(), "KEY_SLEEP");
        keyMappings.put(ButtonKeyCode.MUSIC.getValue(), "KEY_SOUND_MODE");
        keyMappings.put(ButtonKeyCode.MEDIA_STOP.getValue(), "KEY_STOP");
        keyMappings.put(ButtonKeyCode.AVR_INPUT.getValue(), "KEY_VCR_MODE");
        keyMappings.put(ButtonKeyCode.DISPLAY.getValue(), "KEYCODE_DISPLAY");
        keyMappings.put(ButtonKeyCode.KEYCODE_3D_MODE.getValue(), "KEYCODE_3D_MODE");
        keyMappings.put(ButtonKeyCode.NETFLIX.getValue(), "KEYCODE_NETFLIX");
        keyMappings.put(ButtonKeyCode.YOUTUBE.getValue(), "KEYCODE_YOUTUBE");
        keyMappings.put(ButtonKeyCode.KEY_SOURCE.getValue(), "KEY_SOURCE");
        keyMappings.put(ButtonKeyCode.KEY_ID_SETUP.getValue(), "KEY_ID_SETUP");
        keyMappings.put(ButtonKeyCode.KEY_INFO.getValue(), "KEY_INFO");
        keyMappings.put(ButtonKeyCode.KEY_SETTINGS.getValue(), "KEY_SETTINGS");
        keyMappings.put(ButtonKeyCode.KEY_MENU.getValue(), "KEY_MENU");
        keyMappings.put(ButtonKeyCode.KEY_CH_LIST.getValue(), "KEY_CH_LIST");
        keyMappings.put(ButtonKeyCode.APPLE_TV.getValue(), "APPLE_TV");
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public SamsungRemoteManeger(Context context2, String str, int i, SamsungConnectListener samsungConnectListener2, SharedPreferences sharedPreferences2) {
        this.context = context2;
        this.ip = str;
        this.port = i;
        this.sharedPreferences = sharedPreferences2;
        this.samsungConnectListener = samsungConnectListener2;
        this.okHttpClient = getUnsafeOkHttpClient().build();
        this.samsungSocketListener = new SamsungSocketListener(this.samsungConnectListener);
    }

    public void getAllChanel(GetAllChannelListener getAllChannelListener2) {
        this.getAllChannelListener = getAllChannelListener2;
        this.webSocket.send("{\"method\":\"ms.channel.emit\",\"params\":{\"event\": \"ed.installedApp.get\", \"to\":\"host\"}}");
    }

    public void connect() {
        setIP(this.ip);
        String generateUrl = generateUrl(isSsl());
        StringBuilder sb = new StringBuilder();
        sb.append("connect: 2-- port: ");
        sb.append(this.port);
        sb.append(" -- link: ");
        sb.append(generateUrl);
        this.webSocket = this.okHttpClient.newWebSocket(new Request.Builder().url(generateUrl).build(), this.samsungSocketListener);
    }

    public void sendKeyEvent(String str) {
        Context context2 = this.context;
        if (context2 != null) {
            ViewUtils.provideHapticFeedback(context2, 100);
        }
        String str2 = keyMappings.get(str);
        StringBuilder sb = new StringBuilder();
        sb.append("sendKeyEvent11: ");
        sb.append(str2);
        if (this.isConnected) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("sendKeyEvent22: ");
            sb2.append(str2);
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put("Cmd", "Click");
                jSONObject2.put("DataOfCmd", str2);
                jSONObject2.put("Option", PListParser.TAG_FALSE);
                jSONObject2.put("TypeOfRemote", "SendRemoteKey");
                jSONObject.put("method", "ms.remote.control");
                jSONObject.put("params", jSONObject2);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            sendCommand(jSONObject);
        }
    }

    private void sendCommand(JSONObject jSONObject) {
        WebSocket webSocket2;
        Bundle bundle = new Bundle();
        StringBuilder sb = new StringBuilder();
        sb.append("sendCommand11: ");
        sb.append(jSONObject.toString());
        bundle.putString("try_v", String.valueOf(this.port));
        if (this.isConnected && (webSocket2 = this.webSocket) != null) {
            webSocket2.send(jSONObject.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("sendCommand22: ");
            sb2.append(jSONObject.toString());
        }
    }

    public boolean isSsl() {
        return this.port == 8002;
    }

    private String generateUrl(boolean z) {
        String str = this.name;
        if (z) {
            return "wss://" + this.ip + ":" + this.port + "/api/v2/channels/samsung.remote.control?name=" + str + "&token=" + getToken();
        }
        return "ws://" + this.ip + ":" + this.port + "/api/v2/channels/samsung.remote.control?name=" + str;
    }

    public String getToken() {
        return this.sharedPreferences.getString("TOKEN_SS_REMOTE", "");
    }

    public void setToken(String str) {
        this.sharedPreferences.edit().putString("TOKEN_SS_REMOTE", str).apply();
    }

    public String getIP() {
        return this.sharedPreferences.getString("IP_SAMSUNG_REMOTE", "");
    }

    public void setIP(String str) {
        this.sharedPreferences.edit().putString("IP_SAMSUNG_REMOTE", str).apply();
    }

    public void disconnect() {
        setToken("");
        WebSocket webSocket2 = this.webSocket;
        if (webSocket2 != null) {
            this.isConnected = false;
            webSocket2.close(1000, "disconnected");
        }
    }

    private static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            TrustManager[] trustManagerArr = {new X509TrustManager() {
                /* class com.magicapps.casttotv.tv.utils.remote.other.SamsungRemoteManeger.AnonymousClass1 */

                @Override // javax.net.ssl.X509TrustManager
                public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
                }

                @Override // javax.net.ssl.X509TrustManager
                public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};
            SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, trustManagerArr, new SecureRandom());
            SSLSocketFactory socketFactory = instance.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(socketFactory, (X509TrustManager) trustManagerArr[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                /* class com.magicapps.casttotv.tv.utils.remote.other.SamsungRemoteManeger.AnonymousClass2 */

                public boolean verify(String str, SSLSession sSLSession) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public void setConnectionStatus(boolean z) {
        this.isConnected = z;
    }

    public void saveTokenFromConnectMessage(String str, SamsungConnectListener samsungConnectListener2) {
        StringBuilder sb = new StringBuilder();
        sb.append(" : ----11111 ");
        sb.append(str);
        if (!"".equals(this.token)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("saveTokenFromConnectMessage: ----22222 ");
            sb2.append(str);
            setConnectionStatus(true);
            if (samsungConnectListener2 != null) {
                samsungConnectListener2.onSuccess();
                return;
            }
            return;
        }
        try {
            setToken(new JSONObject(String.valueOf(new JSONObject(str).get("data"))).getString(IronSourceConstants.IRONSOURCE_BIDDING_TOKEN_KEY));
            setConnectionStatus(true);
            if (samsungConnectListener2 != null) {
                samsungConnectListener2.onSuccess();
                return;
            }
        } catch (JSONException e2) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("saveTokenFromConnectMessage: ----55555 ");
            sb3.append(e2.toString());
            e2.printStackTrace();
        }
        if (samsungConnectListener2 != null) {
            System.out.println("error in token, disconnected");
        }
    }

    /* access modifiers changed from: private */
    public class SamsungSocketListener extends WebSocketListener {
        private SamsungConnectListener connectListener;

        @Override // okhttp3.WebSocketListener
        public void onClosing(WebSocket webSocket, int i, String str) {
        }

        private SamsungSocketListener(SamsungConnectListener samsungConnectListener) {
            this.connectListener = samsungConnectListener;
        }

        @Override // okhttp3.WebSocketListener
        public void onOpen(WebSocket webSocket, Response response) {
            PrintStream printStream = System.out;
            printStream.println("Receiving : " + response.message());
        }

        @Override // okhttp3.WebSocketListener
        public void onMessage(WebSocket webSocket, String str) {
            new Bundle();
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("connect: ----300000");
                sb.append(str);
                String string = new JSONObject(str).getString(NetcastTVService.UDAP_API_EVENT);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("connect: ----string ");
                sb2.append(string);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("connect: ----311111 ");
                sb3.append(str);
                if ("ms.channel.clientDisconnect".equals(string)) {
                    SamsungRemoteManeger.this.disconnect();
                    return;
                }
                if ("ms.channel.unauthorized".equals(string)) {
                    if (!SamsungRemoteManeger.this.isConnected) {
                        if (this.connectListener != null) {
                            this.connectListener.onFailure("data_null");
                            return;
                        }
                        return;
                    }
                }
                if (!"ms.channel.connect".equals(string) || SamsungRemoteManeger.this.isConnected) {
                    if (!"ed.installedApp.get".equals(string)) {
                        if (!"ms.channel.connect".equals(string)) {
                            if (SamsungRemoteManeger.this.getAllChannelListener != null) {
                                SamsungRemoteManeger.this.getAllChannelListener.onFail("error channel");
                                return;
                            }
                            return;
                        }
                    }
                    if (SamsungRemoteManeger.this.getAllChannelListener != null) {
                        SamsungRemoteManeger.this.getAllChannelListener.onSuccess(str);
                        return;
                    }
                    return;
                }
                if (SamsungRemoteManeger.this.isSsl()) {
                    SamsungRemoteManeger.this.saveTokenFromConnectMessage(str, this.connectListener);
                }
                SamsungRemoteManeger.this.setConnectionStatus(true);
                if (this.connectListener != null) {
                    this.connectListener.onSuccess();
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }

        @Override // okhttp3.WebSocketListener
        public void onMessage(WebSocket webSocket, ByteString byteString) {
            PrintStream printStream = System.out;
            printStream.println("Receiving bytes : " + byteString.hex());
        }

        @Override // okhttp3.WebSocketListener
        public void onFailure(WebSocket webSocket, Throwable th, Response response) {
            SamsungConnectListener samsungConnectListener;
            if (th != null) {
                PrintStream printStream = System.out;
                printStream.println("Error : " + th.getMessage());
                th.printStackTrace();
                if (th.getMessage().toLowerCase().contains("failed to connect to") && (samsungConnectListener = this.connectListener) != null) {
                    samsungConnectListener.onFailure("error");
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("connect: ----6 ");
            sb.append(th.toString());
        }
    }
}
