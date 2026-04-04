package com.colorcallscreen.colorphone.callscreen.calltheme.base;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;


public class BaseService {
    public void jsonGetReq(Context context, final String str, final BaseParser baseParser, final ResponseInterface responseInterface) {
        RequestQueue newRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(0, str, new Response.Listener<String>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseService.1
            @Override // com.android.volley.Response.Listener
            public void onResponse(String str2) {
                try {
                    responseInterface.onResponse(baseParser.parseJson(new JSONObject(str2)), str2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("ResponseData:::", " " + str2.toString());
            }
        }, new Response.ErrorListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseService.2
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                responseInterface.onResponse(null, null);
                StringBuilder sb = new StringBuilder();
                sb.append("Error ");
                sb.append(str);
                sb.append(" ");
                sb.append(volleyError.networkResponse != null ? "StatusCode : " + volleyError.networkResponse : "");
                Log.e("Response", sb.toString());
            }
        });
        stringRequest.setShouldCache(false);
        newRequestQueue.add(stringRequest);
    }
}
