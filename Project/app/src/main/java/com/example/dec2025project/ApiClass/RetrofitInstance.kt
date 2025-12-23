package com.example.livetvnewapp.ApiClass

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
	val api : MovieApi by lazy {
		Retrofit.Builder()
			.baseUrl("https://tvguide.andromenia.com/smart_tv_guide/")
			.addConverterFactory(GsonConverterFactory.create())
			.build() 
			.create(MovieApi::class.java)
	} 
}
