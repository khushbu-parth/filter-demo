package com.example.livetvnewapp.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.livetvnewapp.Adapters.CateGoryAdapter
import com.example.livetvnewapp.MovieViewModel
import com.example.livetvnewapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var viewmodel: MovieViewModel
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var categoryadapter: CateGoryAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PreparedRecyclerview()

        viewmodel = ViewModelProvider(this)[MovieViewModel::class.java]
        viewmodel.getCategorydata()
        viewmodel.observeMovieLiveData().observe(this, Observer { catlist ->
            categoryadapter.setcatlist(catlist, this)
        })


    }

    private fun PreparedRecyclerview() {
        categoryadapter = CateGoryAdapter()
        binding.recyclelist.apply {
            layoutManager = GridLayoutManager(applicationContext, 3)
            adapter = categoryadapter
        }
    }

}