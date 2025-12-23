package com.example.livetvnewapp.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.livetvnewapp.Extrass.Common
import com.example.livetvnewapp.databinding.ActivityTvGuideBinding

class TvGuideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTvGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTvGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ClickGuide()
    }

    private fun ClickGuide() {
        binding.guide1.setOnClickListener {
            Common.textnumber = 1
            val mainIntent = Intent(this, TvGuideDetails::class.java)
            startActivity(mainIntent)
        }
        binding.guide2.setOnClickListener {
            Common.textnumber = 2
            val mainIntent = Intent(this, TvGuideDetails::class.java)
            startActivity(mainIntent)
        }
        binding.guide3.setOnClickListener {
            Common.textnumber = 3
            val mainIntent = Intent(this, TvGuideDetails::class.java)
            startActivity(mainIntent)
        }
        binding.guide4.setOnClickListener {
            Common.textnumber = 4
            val mainIntent = Intent(this, TvGuideDetails::class.java)
            startActivity(mainIntent)
        }
        binding.guide5.setOnClickListener {
            Common.textnumber = 5
            val mainIntent = Intent(this, TvGuideDetails::class.java)
            startActivity(mainIntent)
        }
        binding.guide6.setOnClickListener {
            Common.textnumber = 6
            val mainIntent = Intent(this, TvGuideDetails::class.java)
            startActivity(mainIntent)
        }

    }
}