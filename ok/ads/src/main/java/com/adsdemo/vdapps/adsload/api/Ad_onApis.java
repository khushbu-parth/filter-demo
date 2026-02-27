package com.adsdemo.vdapps.adsload.api;

import org.json.JSONObject;

public interface Ad_onApis {
    public void onResponse(JSONObject response);

    public void onErrorResponse(String error);
}
