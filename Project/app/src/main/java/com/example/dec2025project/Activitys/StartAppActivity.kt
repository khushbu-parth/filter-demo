package com.example.livetvnewapp.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.livetvnewapp.databinding.ActivityStartAppBinding

class StartAppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startapp.setOnClickListener {
            val mainIntent = Intent(this, HomeActivity::class.java)
            startActivity(mainIntent)
        }
        binding.privacyapp.setOnClickListener {

        }

        binding.shareapp.setOnClickListener {

        }

        binding.rateapp.setOnClickListener {

        }

    }

    override fun onBackPressed() {
        finish()
        finishAffinity()
    }
}