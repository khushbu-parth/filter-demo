package com.ads.sdk.apiData;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {
    @FormUrlEncoded
    @POST("app_setting.php?")
    Call<JsonObject> doCall(@Field("application_id") String appId, @Field("package") String packageName, @Field("install") String installType, @Field("mode") String modeType);
}