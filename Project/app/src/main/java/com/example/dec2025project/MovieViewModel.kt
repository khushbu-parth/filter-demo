package com.example.livetvnewapp
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.livetvnewapp.ApiClass.RetrofitInstance
import com.example.livetvnewapp.Models.CategoryList
import com.example.livetvnewapp.Models.CategoryMain
import com.example.livetvnewapp.Models.Chennel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class MovieViewModel : ViewModel() {
    private var catelivedata = MutableLiveData<List<CategoryList>>()
    private var channellivedata = MutableLiveData<List<Chennel>>()
    fun getCategorydata() {
        RetrofitInstance.api.getPopularMovies("debug")
            .enqueue(object : Callback<CategoryMain> {
                override fun onResponse(call: Call<CategoryMain>, response: Response<CategoryMain>) {
                    if (response.body() != null) {
                        catelivedata.value = response.body()!!.data
                    } else {
                        return
                    }
                }
                override fun onFailure(call: Call<CategoryMain>, t: Throwable) {
                    Log.d("TAG", t.message.toString())
                }
            })
    }
    fun observeMovieLiveData(): LiveData<List<CategoryList>> {
        return catelivedata
    }
    fun observeChannelLiveData(): LiveData<List<Chennel>> {
        return channellivedata
    }
} 
