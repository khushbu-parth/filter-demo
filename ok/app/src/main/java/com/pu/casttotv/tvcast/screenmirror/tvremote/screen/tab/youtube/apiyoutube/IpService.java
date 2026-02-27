package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.youtube.apiyoutube;

import com.pu.casttotv.tvcast.screenmirror.tvremote.model.YoutubeRes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/* loaded from: classes4.dex */
public interface IpService {
    @GET("youtube/v3/search?part=snippet&order=relevance")
    Call<YoutubeRes> getListYoutube(@Query("q") String str, @Query("type") String str2, @Query("key") String str3, @Query("maxResults") String str4);
}
