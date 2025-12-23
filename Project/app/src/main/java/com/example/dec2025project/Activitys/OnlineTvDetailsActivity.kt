package com.example.livetvnewapp.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.livetvnewapp.Extrass.Common
import com.example.livetvnewapp.R
import com.example.livetvnewapp.databinding.ActivityOnlineTvDetailsBinding

class OnlineTvDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnlineTvDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnlineTvDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Common.tvonline == 2) {

            binding.header.text = resources.getString(R.string.circle)
            binding.text.text = resources.getString(R.string.circled)
        } else if (Common.tvonline == 3) {
            binding.header.text =resources.getString( R.string.tvfirma)
            binding.text.text =resources.getString( R.string.tvfirmad)
        } else if (Common.tvonline == 4) {
            binding.header.text = resources.getString(R.string.reporter)
            binding.text.text =resources.getString( R.string.reporterd)
        } else if (Common.tvonline == 5) {
            binding.header.text =resources.getString( R.string.escanner)
            binding.text.text = resources.getString(R.string.escannerd)
        } else if (Common.tvonline == 6) {
            binding.header.text = resources.getString(R.string.racer)
            binding.text.text = resources.getString(R.string.racerd)
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}