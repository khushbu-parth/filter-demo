package com.adsdemo.vdapps.adsload.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Ad_Apis {
    private final Context context;
    ProgressDialog progress;

    public Ad_Apis(Context context) {
        this.context = context;
    }

    private void showDialog() {
        progress = new ProgressDialog(context);
        progress.setMessage("Please Wait...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    private void cancelDialog() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    public void POST_WITH_FROM_DATA(String Url, HashMap<String, String> params, Ad_onApis onApis) throws JSONException {
        showDialog();
        StringRequest request = new StringRequest(Request.Method.POST, Url, response -> {
            try {
                onApis.onResponse(new JSONObject(response));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            cancelDialog();
        }, error -> {
            onApis.onErrorResponse(getErrorResponse(error));
            cancelDialog();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    private String getErrorResponse(VolleyError error) {
        String message = "";
        if (error instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        Log.d("getErrorResponse", message);
        return message;
    }
}
