package com.library.info;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @FormUrlEncoded
    @POST("app_setting.php")
    Call<JsonObject> getData(
            @Field("application_id") String application_id,
            @Field("package") String packageName,
            @Field("install") String install,
            @Field("mode") String mode
    );

}

