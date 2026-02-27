package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.sony;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Base64;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ironsource.mediationsdk.utils.IronSourceConstants;
import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote.ChannelFragment;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.util.AsyncTask;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.http.cookie.SM;
import org.json.JSONArray;
import org.json.JSONObject;

public class RemoteSonyManager {
    public static String TEST_IP = "http://10.0.0.24:80";
    public static String cookie = "";
    private static final RemoteSonyManager remoteSonyManager = new RemoteSonyManager();

    public interface CommandListener {
        void onCommandSucceeded();

        void onUnauthorizedError();
    }

    public interface NetworkListener {
        void onDevicePincodeGenerated(boolean z);

        void onDeviceRegistrationCompleted(boolean z);

        void onFailedToConnect();
    }

    public static RemoteSonyManager getInstance() {
        return remoteSonyManager;
    }

    public void remoteButton(Context context, final String str, final CommandListener commandListener) {
        final String str2 = cookie;
        String commandPath = getCommandPath();
        if (commandPath == null || commandPath.isEmpty()) {
            commandListener.onUnauthorizedError();
            return;
        }
        StringRequest r10 = new StringRequest(1, commandPath, new Response.Listener<String>() {
            public void onResponse(String str) {
                if (str != null) {
                    commandListener.onCommandSucceeded();
                }
            }
        }, new Response.ErrorListener() {
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                StringBuilder sb = new StringBuilder();
                sb.append("runSonyCommandVolley --- VolleyError: ");
                sb.append(volleyError.toString());
            }
        }) {
            @Override // com.android.volley.Request
            public String getBodyContentType() {
                return "application/xml";
            }

            @Override // com.android.volley.Request
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                if (headers == null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }
                headers.put("SOAPACTION", "\"urn:schemas-sony-com:service:IRCC:1#X_SendIRCC\"");
                headers.put("X-Auth-PSK", "1234");
                headers.put(SM.COOKIE, str2);
                headers.put("Content-Type", "application/xml");
                return headers;
            }

            @Override // com.android.volley.Request
            public byte[] getBody() throws AuthFailureError {
                try {
                    return ("<s:Envelope\n    xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"\n    s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n    <s:Body>\n        <u:X_SendIRCC xmlns:u=\"urn:schemas-sony-com:service:IRCC:1\">\n            <IRCCCode>" + str + "</IRCCCode>\n        </u:X_SendIRCC>\n    </s:Body>\n</s:Envelope>").getBytes("UTF-8");
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                    return super.getBody();
                }
            }

            @Override // com.android.volley.Request, com.magicapps.casttotv.tv.utils.remote.sony.StringRequest
            public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
                StringBuilder sb = new StringBuilder();
                sb.append("parseNetworkResponse --- response.statusCode: ");
                sb.append(networkResponse.statusCode);
                return super.parseNetworkResponse(networkResponse);
            }

            @Override // com.android.volley.Request
            public VolleyError parseNetworkError(VolleyError volleyError) {
                StringBuilder sb = new StringBuilder();
                sb.append("parseNetworkResponse: ");
                sb.append(volleyError);
                if (volleyError.networkResponse == null) {
                    return super.parseNetworkError(volleyError);
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("parseNetworkResponse --- response.statusCode: ");
                sb2.append(volleyError.networkResponse.statusCode);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("parseNetworkResponse --- response.statusCode: ");
                sb3.append(volleyError.toString());
                try {
                    int i = volleyError.networkResponse.statusCode;
                    if (i == 403 || i == 401) {
                        commandListener.onUnauthorizedError();
                    }
                } catch (Exception unused) {
                }
                return super.parseNetworkError(volleyError);
            }
        };
        r10.setRetryPolicy(new RetryPolicy() {
            /* class com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.AnonymousClass4 */

            @Override // com.android.volley.RetryPolicy
            public int getCurrentRetryCount() {
                return 10;
            }

            @Override // com.android.volley.RetryPolicy
            public int getCurrentTimeout() {
                return 1000;
            }

            @Override // com.android.volley.RetryPolicy
            public void retry(VolleyError volleyError) throws VolleyError {
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse != null) {
                    int i = networkResponse.statusCode;
                    if (i == 401 || i == 403) {
                        throw volleyError;
                    }
                    retry(volleyError);
                }
            }
        });
        r10.setTag("sonyCommand");
        MyApplication.getRequestQueue().add(r10);
    }

    public void accessControlWithPIN(final Activity activity, final String str, String str2, String str3, final NetworkListener networkListener) {
        final String str4 = "Sony Remote App (WiFi) - " + Build.MODEL;
        final String str5 = str4 + "-2021";
        final String str6 = "http://" + str3;
        StringBuilder sb = new StringBuilder();
        sb.append("accessControlWithPIN - device: ");
        sb.append(str6);
        AsyncTask.execute(new Runnable() {
            /* class com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.AnonymousClass5 */

            public final void run() {
                RemoteSonyManager.this.accessControlWithPIN$0$NetworkManager(str, str5, str4, str6, activity, networkListener);
            }
        });
    }

    public void accessControlWithPIN$0$NetworkManager(String str, String str2, String str3, String str4, Activity activity, final NetworkListener networkListener) {
        String str5;
        OkHttpClient build = new OkHttpClient().newBuilder().build();
        if (str != null) {
            str5 = ":" + str;
        } else {
            str5 = "";
        }
        try {
            Request.Builder addHeader = new Request.Builder().url(str4 + "/sony/accessControl").method("POST", RequestBody.create(MediaType.parse("application/json"), "{\"id\":13,\"method\":\"actRegister\",\"version\":\"1.0\",\"params\":[{\"clientid\":\"" + str2 + "\",\"nickname\":\"" + str3 + "\"},[{\"clientid\":\"" + str2 + "\",\"value\":\"yes\",\"nickname\":\"" + str3 + "\",\"function\":\"WOL\"}]]}")).addHeader("Content-Type", "application/json");
            StringBuilder sb = new StringBuilder();
            sb.append("Basic ");
            sb.append(Base64.encodeToString(str5.getBytes(), 2));
            final okhttp3.Response execute = build.newCall(addHeader.addHeader("Authorization", sb.toString()).build()).execute();
            activity.runOnUiThread(new Runnable() {
                /* class com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.AnonymousClass6 */

                public void run() {
                    String header = execute.header(SM.SET_COOKIE);
                    if (header != null && !header.isEmpty()) {
                        RemoteSonyManager.cookie = header;
                        RemoteSonyManager.this.getTVApps(new AppsListener() {
                            @Override // com.magicapps.casttotv.tv.utils.remote.sony.AppsListener
                            public void onError() {
                            }

                            @Override // com.magicapps.casttotv.tv.utils.remote.sony.AppsListener
                            public void onAppsFetched(ArrayList<TVApp> arrayList) {
                                ChannelFragment.channelSonyTVList.clear();
                                ChannelFragment.channelSonyTVList.addAll(arrayList);
                                ChannelFragment.channelSonyAdapter.setData(arrayList);
                                StringBuilder sb = new StringBuilder();
                                sb.append("onAppsFetched: ");
                                sb.append(String.valueOf(arrayList.size()));
                            }
                        });
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Response headers: ");
                    sb.append(execute.headers().toString());
                    try {
                        if (new JSONObject(execute.body().string()).has("error")) {
                            networkListener.onDevicePincodeGenerated(false);
                            return;
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    networkListener.onDeviceRegistrationCompleted(true);
                }
            });
        } catch (IOException e2) {
            e2.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                /* class com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.AnonymousClass7 */

                public void run() {
                    networkListener.onFailedToConnect();
                }
            });
        }
    }

    public void getTVApps(final AppsListener appsListener) {
        final String str = cookie;
        String tVAppsCommandPath = getTVAppsCommandPath();
        if (tVAppsCommandPath == null || tVAppsCommandPath.isEmpty()) {
            appsListener.onError();
            return;
        }
        JsonObjectRequest r9 = new JsonObjectRequest(1, tVAppsCommandPath, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray(IronSourceConstants.EVENTS_RESULT).getJSONArray(0);
                    ArrayList<TVApp> arrayList = new ArrayList<>();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        jSONArray.getJSONObject(i);
                        arrayList.add(new TVApp(jSONArray.getJSONObject(i).getString("title"), jSONArray.getJSONObject(i).getString("uri"), jSONArray.getJSONObject(i).getString("icon")));
                    }
                    appsListener.onAppsFetched(arrayList);
                } catch (Exception unused) {
                    appsListener.onError();
                }
            }
        }, new Response.ErrorListener() {
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                appsListener.onError();
            }
        }) {
            @Override // com.android.volley.Request
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                if (headers == null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }
                headers.put("SOAPACTION", "\"urn:schemas-sony-com:service:IRCC:1#X_SendIRCC\"");
                headers.put("X-Auth-PSK", "1234");
                headers.put(SM.COOKIE, str);
                headers.put("Content-Type", "application/xml");
                return headers;
            }

            @Override // com.android.volley.Request, com.android.volley.toolbox.JsonRequest
            public byte[] getBody() {
                try {
                    return "{\"method\":\"getApplicationList\", \"id\":60,\"params\":[],\"version\":\"1.0\"}".getBytes("UTF-8");
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                    return super.getBody();
                }
            }

            @Override // com.android.volley.toolbox.JsonObjectRequest, com.android.volley.Request
            public Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
                return super.parseNetworkResponse(networkResponse);
            }

            @Override // com.android.volley.Request
            public VolleyError parseNetworkError(VolleyError volleyError) {
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse == null) {
                    return super.parseNetworkError(volleyError);
                }
                try {
                    int i = networkResponse.statusCode;
                    if (i == 403 || i == 401) {
                        appsListener.onError();
                    }
                } catch (Exception unused) {
                }
                return super.parseNetworkError(volleyError);
            }
        };
        r9.setRetryPolicy(new RetryPolicy() {
            @Override // com.android.volley.RetryPolicy
            public int getCurrentRetryCount() {
                return 10;
            }

            @Override // com.android.volley.RetryPolicy
            public int getCurrentTimeout() {
                return 1000;
            }

            @Override // com.android.volley.RetryPolicy
            public void retry(VolleyError volleyError) throws VolleyError {
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse != null) {
                    int i = networkResponse.statusCode;
                    if (i == 401 || i == 403) {
                        throw volleyError;
                    }
                    retry(volleyError);
                }
            }
        });
        r9.setTag("sonyCommand");
        MyApplication.getRequestQueue().add(r9);
    }

    private String getCommandPath() {
        return "http:/" + TVConnectUtils.getInstance().getConnectableDevice().getIpAddress() + "/sony/IRCC";
    }

    private String getTVAppsCommandPath() {
        return "http:/" + TVConnectUtils.getInstance().getConnectableDevice().getIpAddress() + "/sony/appControl";
    }

    public void openTVApp(final TVApp tVApp, final SimpleNetworkListener simpleNetworkListener) {
        final String str = cookie;
        String tVAppsCommandPath = getTVAppsCommandPath();
        if (tVAppsCommandPath == null || tVAppsCommandPath.isEmpty()) {
            simpleNetworkListener.onError();
            return;
        }
        JsonObjectRequest r10 = new JsonObjectRequest(1, tVAppsCommandPath, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                simpleNetworkListener.onFinish(true);
            }
        }, new Response.ErrorListener() {
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                simpleNetworkListener.onError();
            }
        }) {
            @Override // com.android.volley.Request
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                if (headers == null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }
                headers.put("SOAPACTION", "\"urn:schemas-sony-com:service:IRCC:1#X_SendIRCC\"");
                headers.put("X-Auth-PSK", "1234");
                headers.put(SM.COOKIE, str);
                headers.put("Content-Type", "application/xml");
                return headers;
            }

            @Override // com.android.volley.Request, com.android.volley.toolbox.JsonRequest
            public byte[] getBody() {
                try {
                    return ("{\"method\":\"setActiveApp\",\"id\":601,\"params\":[{\"uri\":\"" + tVApp.getUri() + "\"}],\"version\":\"1.0\"}").getBytes("UTF-8");
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                    return super.getBody();
                }
            }

            /* access modifiers changed from: protected */
            @Override // com.android.volley.toolbox.JsonObjectRequest, com.android.volley.Request
            public Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
                return super.parseNetworkResponse(networkResponse);
            }

            /* access modifiers changed from: protected */
            @Override // com.android.volley.Request
            public VolleyError parseNetworkError(VolleyError volleyError) {
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse == null) {
                    return super.parseNetworkError(volleyError);
                }
                try {
                    int i = networkResponse.statusCode;
                    if (i == 403 || i == 401) {
                        simpleNetworkListener.onError();
                    }
                } catch (Exception unused) {
                }
                return super.parseNetworkError(volleyError);
            }
        };
        r10.setRetryPolicy(new RetryPolicy() {
            /* class com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.AnonymousClass15 */

            @Override // com.android.volley.RetryPolicy
            public int getCurrentRetryCount() {
                return 10;
            }

            @Override // com.android.volley.RetryPolicy
            public int getCurrentTimeout() {
                return 1000;
            }

            @Override // com.android.volley.RetryPolicy
            public void retry(VolleyError volleyError) throws VolleyError {
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse != null) {
                    int i = networkResponse.statusCode;
                    if (i == 401 || i == 403) {
                        throw volleyError;
                    }
                    retry(volleyError);
                }
            }
        });
        r10.setTag("sonyCommand");
        MyApplication.getRequestQueue().add(r10);
    }
}
