package com.pu.casttotv.tvcast.screenmirror.tvremote.RequestClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonResponse {

    @SerializedName("ads_flag")
    @Expose
    private String adsFlag;
    @SerializedName("Packagename")
    @Expose
    private String packagename;
    @SerializedName("forcefully_download")
    @Expose
    private String forcefullyDownload;
    @SerializedName("Appopen_Ads_Key")
    @Expose
    private String appopenAdsKey;
    @SerializedName("Banner_Ads_Key")
    @Expose
    private String bannerAdsKey;
    @SerializedName("Native_Ads_Keys")
    @Expose
    private String nativeAdsKeys;
    @SerializedName("Rewards_ads_key")
    @Expose
    private String rewardsadskey;
    @SerializedName("Rewarded_Interstitial_Keys")
    @Expose
    private String rewardedinterstitialkeys;
    @SerializedName("Interstitial_Ads_Keys")
    @Expose
    private String interstitialAdsKeys;
    @SerializedName("Interstitial_Front_Counter_Flag")
    @Expose
    private int interstitialfrontcounterflag;
    @SerializedName("Interstitial_Back_Counter_Flag")
    @Expose
    private int interstitialbackcounterflag;
    @SerializedName("Ads_Off_Version")
    @Expose
    private ArrayList<String> AdsOffVersion;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;


    public String getAdsFlag() {
        return adsFlag;
    }

    public void setAdsFlag(String adsFlag) {
        this.adsFlag = adsFlag;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getForcefullyDownload() {
        return forcefullyDownload;
    }

    public void setForcefullyDownload(String forcefullyDownload) {
        this.forcefullyDownload = forcefullyDownload;
    }

    public String getAppopenAdsKey() {
        return appopenAdsKey;
    }

    public void setAppopenAdsKey(String appopenAdsKey) {
        this.appopenAdsKey = appopenAdsKey;
    }

    public String getBannerAdsKey() {
        return bannerAdsKey;
    }

    public void setBannerAdsKey(String bannerAdsKey) {
        this.bannerAdsKey = bannerAdsKey;
    }

    public String getNativeAdsKeys() {
        return nativeAdsKeys;
    }

    public void setNativeAdsKeys(String nativeAdsKeys) {
        this.nativeAdsKeys = nativeAdsKeys;
    }

    public String getRewardsadskey() {
        return rewardsadskey;
    }

    public void setRewardsadskey(String rewardsadskey) {
        this.rewardsadskey = rewardsadskey;
    }

    public String getRewardedinterstitialkeys() {
        return rewardedinterstitialkeys;
    }

    public void setRewardedinterstitialkeys(String rewardedinterstitialkeys) {
        this.rewardedinterstitialkeys = rewardedinterstitialkeys;
    }

    public String getInterstitialAdsKeys() {
        return interstitialAdsKeys;
    }

    public void setInterstitialAdsKeys(String interstitialAdsKeys) {
        this.interstitialAdsKeys = interstitialAdsKeys;
    }

    public int getInterstitialfrontcounterflag() {
        return interstitialfrontcounterflag;
    }

    public void setInterstitialfrontcounterflag(int interstitialfrontcounterflag) {
        this.interstitialfrontcounterflag = interstitialfrontcounterflag;
    }

    public int getInterstitialbackcounterflag() {
        return interstitialbackcounterflag;
    }

    public void setInterstitialbackcounterflag(int interstitialbackcounterflag) {
        this.interstitialbackcounterflag = interstitialbackcounterflag;
    }

    public ArrayList<String> getAdsOffVersion() {
        return AdsOffVersion;
    }

    public void setAdsOffVersion(ArrayList<String> adsOffVersion) {
        AdsOffVersion = adsOffVersion;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
