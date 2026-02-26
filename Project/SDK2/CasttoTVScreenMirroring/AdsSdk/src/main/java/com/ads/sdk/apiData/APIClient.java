package com.ads.sdk.apiData;


import com.ads.sdk.configs.Config;
import com.ads.sdk.configs.PreferenceManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String packageName) {
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.rootApiBase(packageName))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}