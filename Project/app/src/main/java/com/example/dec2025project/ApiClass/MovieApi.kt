package com.example.livetvnewapp.ApiClass

import com.example.livetvnewapp.Models.CategoryMain
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
	@GET("live_all_guide_k.php?")
	fun getPopularMovies(@Query("mode") api_key : String) : Call<CategoryMain>

// in key is not equired
	@GET("live_all_guide_k.php?")
	fun getPopularData() : Call<CategoryMain>
}
