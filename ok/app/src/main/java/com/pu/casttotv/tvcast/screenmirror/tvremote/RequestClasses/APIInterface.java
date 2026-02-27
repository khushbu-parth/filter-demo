package com.pu.casttotv.tvcast.screenmirror.tvremote.RequestClasses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface APIInterface {
    @GET()
    Call<JsonResponse> keyRequestCall(@Url String url);
}
