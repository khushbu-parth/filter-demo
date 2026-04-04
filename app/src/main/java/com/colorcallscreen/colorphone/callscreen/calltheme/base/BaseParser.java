package com.colorcallscreen.colorphone.callscreen.calltheme.base;

import androidx.core.app.NotificationCompat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;


public class BaseParser {
    protected Gson gson;

    private void initGson() {
        this.gson = new GsonBuilder().create();
    }

    public BaseModel parseJson(JSONObject jSONObject) {
        try {
            if (jSONObject.has(NotificationCompat.CATEGORY_STATUS) && jSONObject.getBoolean(NotificationCompat.CATEGORY_STATUS)) {
                initGson();
                return parseResponse(jSONObject);
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BaseModel parseResponse(JSONObject jSONObject) {
        return new BaseModel();
    }
}
