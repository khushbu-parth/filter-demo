package com.example.livetvnewapp.Activitys

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.livetvnewapp.Extrass.Common
import com.example.livetvnewapp.R
import com.example.livetvnewapp.databinding.ActivityTvGuideDetailsBinding

class TvGuideDetails : AppCompatActivity() {
    private lateinit var binding: ActivityTvGuideDetailsBinding
    var counter = 0
    var backcounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTvGuideDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        counter = Common.textnumber
        backcounter = Common.textnumber

        if (Common.textnumber == 1) {

            binding.header.text = "Guide #1 Live TV Online"
            binding.text.text =resources.getString(R.string.guia1d)


        } else if (Common.textnumber == 2) {

            binding.header.text = resources.getString(R.string.guia2)
            binding.text.text =resources.getString( R.string.guia2d)
        } else if (Common.textnumber == 3) {
            binding.header.text = resources.getString(R.string.guia3)
            binding.text.text = resources.getString(R.string.guia3d)
        } else if (Common.textnumber == 4) {
            binding.header.text =resources.getString( R.string.guia4)
            binding.text.text = resources.getString(R.string.guia4d)
        } else if (Common.textnumber == 5) {
            binding.header.text = resources.getString(R.string.guia5)
            binding.text.text = resources.getString(R.string.guia5d)
        } else if (Common.textnumber == 6) {
            binding.header.text = resources.getString(R.string.guia6)
            binding.text.text = resources.getString(R.string.guia6d)
        }


        binding.prev.setOnClickListener {

            PreviousClick()
        }
        binding.next.setOnClickListener {
            NextClick()
        }


    }

    private fun NextClick() {
        counter++
        if (counter == 1) {

            binding.header.text = resources.getString(R.string.guia1)
            binding.text.text =resources.getString( R.string.guia1d)

        } else if (counter == 2) {
            binding.header.text = resources.getString(R.string.guia2)
            binding.text.text = resources.getString(R.string.guia2d)
        } else if (counter== 3) {
            binding.header.text =resources.getString( R.string.guia3)
            binding.text.text =resources.getString( R.string.guia3d)
        } else if (counter== 4) {
            binding.header.text =resources.getString( R.string.guia4)
            binding.text.text = resources.getString(R.string.guia4d)
        } else if (counter == 5) {
            binding.header.text =resources.getString( R.string.guia5)
            binding.text.text =resources.getString( R.string.guia5d)
        } else if (counter == 6) {
            binding.header.text =resources.getString( R.string.guia6)
            binding.text.text =resources.getString( R.string.guia6d)
        } else {
            Toast.makeText(this, "No More Guidance", 0).show()
        }

    }

    private fun PreviousClick() {
        counter--
        if (counter  == 1) {

            binding.header.text =resources.getString( R.string.guia1)
            binding.text.text = resources.getString(R.string.guia1d)


        } else if (counter  == 2) {

            binding.header.text = resources.getString(R.string.guia2)
            binding.text.text = resources.getString(R.string.guia2d)
        } else if (counter == 3) {
            binding.header.text = resources.getString(R.string.guia3)
            binding.text.text = resources.getString(R.string.guia3d)
        } else if (counter == 4) {
            binding.header.text =resources.getString( R.string.guia4)
            binding.text.text = resources.getString(R.string.guia4d)
        } else if (counter== 5) {
            binding.header.text =resources.getString( R.string.guia5)
            binding.text.text = resources.getString(R.string.guia5d)
        } else if (counter== 6) {
            binding.header.text = resources.getString(R.string.guia6)
            binding.text.text = resources.getString(R.string.guia6d)
        }
        else {
            Toast.makeText(this, "No More Guidance", 0).show()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

}