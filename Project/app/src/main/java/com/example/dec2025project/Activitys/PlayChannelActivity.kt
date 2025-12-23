package com.example.livetvnewapp.Activitys

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.livetvnewapp.databinding.ActivityPlayChannelBinding


class PlayChannelActivity : AppCompatActivity() {
    private lateinit var Stringlink: String
    private lateinit var binding: ActivityPlayChannelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityPlayChannelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newString = intent.extras!!.getString("channelname", "defaultKey")

        Stringlink =
            "https://tvguide.andromenia.com/smart_tv_guide/channel/" + newString

        Log.e("channel","https://tvguide.andromenia.com/smart_tv_guide/channel/" + newString)

        binding.channelplay.webViewClient = WebViewClient()

        binding.channelplay.loadUrl(Stringlink)

        binding.channelplay.settings.javaScriptEnabled = true

        binding.channelplay.settings.setSupportZoom(true)
    }

    override fun onBackPressed() {
        if (binding.channelplay.canGoBack())
            binding.channelplay.goBack()
        else
            super.onBackPressed()
    }
}