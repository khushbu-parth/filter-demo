package com.colorcallscreen.colorphone.callscreen.calltheme.models;

import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseParser;
import org.json.JSONObject;


public class ThemeModelParser extends BaseParser {
    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseParser
    public BaseModel parseResponse(JSONObject jSONObject) {
        return (ThemeModelWrapper) this.gson.fromJson(jSONObject.toString(), ThemeModelWrapper.class);
    }
}
