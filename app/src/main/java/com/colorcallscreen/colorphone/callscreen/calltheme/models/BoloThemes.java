package com.colorcallscreen.colorphone.callscreen.calltheme.models;

import android.content.Context;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;


public class BoloThemes {
    private Gson gson = new GsonBuilder().create();

    
    public interface ThemeLoaded {
        void onThemeLoaded(List<ThemeModel> list);
    }

    public void loadThemes(Context context, String str, ThemeLoaded themeLoaded) {
        themeLoaded.onThemeLoaded(((ThemeModelWrapper) this.gson.fromJson(Utility.loadJSONFromAsset(context, str), ThemeModelWrapper.class)).getData().getThemes());
    }
}
