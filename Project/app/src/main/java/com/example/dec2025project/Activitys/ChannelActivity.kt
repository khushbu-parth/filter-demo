package com.example.livetvnewapp.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.livetvnewapp.Adapters.CateGoryAdapter
import com.example.livetvnewapp.Adapters.ChannelAdapter
import com.example.livetvnewapp.MovieViewModel
import com.example.livetvnewapp.databinding.ActivityChannelBinding

class ChannelActivity : AppCompatActivity() {

    private lateinit var channeladapter: ChannelAdapter
    private lateinit var binding: ActivityChannelBinding
    private lateinit var viewmodel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChannelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PreparedRecyclerview()

        channeladapter.setchannellist(CateGoryAdapter.listofchannel,this)


    }


    private fun PreparedRecyclerview() {
        channeladapter = ChannelAdapter()
        binding.listchannelrecycle.apply {
            layoutManager = GridLayoutManager(applicationContext,2)
            adapter = channeladapter
        }
    }
}