package com.ads.sdk.configs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

public class PreferenceManager {
    private static final String TAG = PreferenceManager.class.getName();

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private static final String PREFERENCE_NAME = "ads_sdk";

    private static final String KEY_INSTALL_TYPE = "install_type";

    private static final String KEY_PRIVACY_LINK = "privacy_link";
    private static final String KEY_ADS_FLAG = "ads_flag";
    private static final String KEY_UPDATE_APP = "update_app";
    private static final String KEY_UPDATE_VERSION_CODE = "update_version_code";


    private static final String KEY_VPN_BASE = "vpn_base";
    private static final String KEY_VPN_CARRIER_ID = "vpn_carrier_id";
    private static final String KEY_VPN_SERVERS = "vpn_servers";
    private static final String KEY_VPN_FLAG = "vpn_flag";

    /*For ads Ids*/
    private static final String KEY_APPOPEN_AD_ID = "appopen_ad_id";
    private static final String KEY_BANNER_AD_ID = "banner_ad_id";
    private static final String KEY_INTERSTITIAL_AD_ID = "interstitial_ad_id";
    private static final String KEY_NATIVE_AD_ID = "native_ad_id";

    private static final String KEY_INTERSTITIAL_FREQUENCY = "interstitial_frequency";

    public static void init(Context mContext) {
        preferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void setAdsResponse(JsonObject object) {
        if (object != null) {
            try {
                Config.log(TAG, "setAdsResponse: " + object.toString());
                editor.putString(KEY_PRIVACY_LINK, object.get("app_privacyPolicyLink").getAsString())
                        .putString(KEY_ADS_FLAG, object.get("app_adShowStatus").getAsString())
                        .putString(KEY_UPDATE_APP, object.get("app_updateAppDialogStatus").getAsString())
                        .putString(KEY_UPDATE_VERSION_CODE, object.get("app_versionCode").getAsString())
                        .putString(KEY_INTERSTITIAL_FREQUENCY, object.get("app_mainClickPerAd").getAsString())

                        .putString(KEY_VPN_FLAG, object.get("app_VPNEnable").getAsString())
                        .putString(KEY_VPN_SERVERS, object.get("app_VPNServers").getAsString())
                        .putString(KEY_VPN_CARRIER_ID, object.get("app_VPNCarrierID").getAsString())
                        .putString(KEY_VPN_BASE, object.get("app_vpnurlbase").getAsString())

                        .putString(KEY_APPOPEN_AD_ID, object.get("AppOpen").getAsString())
                        .putString(KEY_BANNER_AD_ID, object.get("Banner").getAsString())
                        .putString(KEY_INTERSTITIAL_AD_ID, object.get("Interstitial").getAsString())
                        .putString(KEY_NATIVE_AD_ID, object.get("Native").getAsString())
                        .apply();
            } catch (JsonIOException e) {
                Config.log(TAG, e.toString());
            }
        }
    }


    /*For VPN*/
    public static String getVpnBase() {
        return preferences.getString(KEY_VPN_BASE, "");
    }

    public static String getVpnCarrierId() {
        return preferences.getString(KEY_VPN_CARRIER_ID, "");
    }

    public static String getVpnServers() {
        return preferences.getString(KEY_VPN_SERVERS, "");
    }

    public static Boolean getVpnFlag() {
        return preferences.getString(KEY_VPN_FLAG, "0").equals("1");
    }

    /*For Other*/
    public static String getPrivacyLink() {
        return preferences.getString(KEY_PRIVACY_LINK, "https/www.google.com/");
    }

    public static Boolean getAppUpdateFlag() {
        return preferences.getString(KEY_UPDATE_APP, "0").equals("1");
    }

    public static Integer getUpdateVersionCode() {
        return Integer.parseInt(preferences.getString(KEY_UPDATE_VERSION_CODE, "1"));
    }

    public static String getInstallType() {
        return preferences.getString(KEY_INSTALL_TYPE, "1");
    }

    public static void setInstallType() {
        editor.putString(KEY_INSTALL_TYPE, "2").apply();
    }


    /*For ads*/
    public static Boolean getAdsFlag() {
        return preferences.getString(KEY_ADS_FLAG, "0").equals("1");
    }

    public static Integer getInterstitialFrequency() {
        int value = 0;
        if (!preferences.getString(KEY_INTERSTITIAL_FREQUENCY, "0").isEmpty()) {
            value = Integer.parseInt(preferences.getString(KEY_INTERSTITIAL_FREQUENCY, "0"));
        }
        return value;
    }

    public static String getAppOpenID() {
        return preferences.getString(KEY_APPOPEN_AD_ID, "");
    }

    public static String getBannerID() {
        return preferences.getString(KEY_BANNER_AD_ID, "");
    }

    public static String getInterstitialID() {
        return preferences.getString(KEY_INTERSTITIAL_AD_ID, "");
    }

    public static String getNativeID() {
        return preferences.getString(KEY_NATIVE_AD_ID, "");
    }
}
