package com.pu.casttotv.tvcast.screenmirror.tvremote.api;

import com.pu.casttotv.tvcast.screenmirror.tvremote.model.YoutubeModel;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface YoutubeService {
    @GET("search?part=snippet&order=relevance&q={key}")
    Call<List<YoutubeModel>> getAllMatches();
}
