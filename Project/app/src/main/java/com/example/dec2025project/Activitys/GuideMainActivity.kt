package com.example.livetvnewapp.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.livetvnewapp.Extrass.Common
import com.example.livetvnewapp.R
import com.example.livetvnewapp.databinding.ActivityGuideMainBinding
import com.example.livetvnewapp.databinding.ActivityTvGuideBinding

class GuideMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuideMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityGuideMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.guide1.setOnClickListener {
            val mainIntent = Intent(this, TvGuideActivity::class.java)
            startActivity(mainIntent)
        }
        binding.guide2.setOnClickListener {
            Common.tvonline = 2
            val mainIntent = Intent(this, OnlineTvDetailsActivity::class.java)
            startActivity(mainIntent)
        }
        binding.guide3.setOnClickListener {
            Common.tvonline = 3
            val mainIntent = Intent(this, OnlineTvDetailsActivity::class.java)
            startActivity(mainIntent)
        }
        binding.guide4.setOnClickListener {
            Common.tvonline = 4
            val mainIntent = Intent(this, OnlineTvDetailsActivity::class.java)
            startActivity(mainIntent)
        }
        binding.guide5.setOnClickListener {
            Common.tvonline = 5
            val mainIntent = Intent(this, OnlineTvDetailsActivity::class.java)
            startActivity(mainIntent)
        }
        binding.guide6.setOnClickListener {
            Common.tvonline = 6
            val mainIntent = Intent(this, OnlineTvDetailsActivity::class.java)
            startActivity(mainIntent)
        }


    }
}