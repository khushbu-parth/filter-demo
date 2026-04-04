package com.colorcallscreen.colorphone.callscreen.calltheme.service;

import android.content.Context;
import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseService;
import com.colorcallscreen.colorphone.callscreen.calltheme.base.ResponseInterface;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeModelParser;


public class ThemeWebService extends BaseService {
    public static final String BASE_URL = "https://theme.vaniassistant.com/theme/Json/";
    public static final String CALLER_IMAGES = "caller_images.json";
    public static final String CALLER_LIVEWALLPAPER = "caller_livewallpaper.json";
    public static final String CALLER_THEME = "caller_theme.json";
    public static final String CUSTOM = "custom.json";

    public void getThemes(Context context, String str, ResponseInterface responseInterface) {
        jsonGetReq(context, BASE_URL + str, new ThemeModelParser(), responseInterface);
    }
}
