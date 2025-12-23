package com.example.livetvnewapp.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.livetvnewapp.Models.Chennel
import com.example.livetvnewapp.Activitys.PlayChannelActivity
import com.example.livetvnewapp.databinding.ChannelItemsBinding

class ChannelAdapter : RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {
    private var channellist = ArrayList<Chennel>()
    private lateinit var requireContext: Context

    fun setchannellist(channellist: List<Chennel>, requireContext: Context) {
        this.channellist = channellist as ArrayList<Chennel>
        this.requireContext=requireContext
        notifyDataSetChanged()
    }

    class ViewHolder (val binding: ChannelItemsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ChannelAdapter.ViewHolder(
            ChannelItemsBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return channellist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.itemView)
            .load("https://tvguide.andromenia.com/smart_tv_guide/assets/live_all_guide_k/"+channellist[position].chennel_icon)
            .into(holder.binding.imgchannel)


        holder.binding.imgchannel.setOnClickListener {


//            Log.e("channel","https://tvguide.andromenia.com/smart_tv_guide/assets/live_all_guide_k/" + channellist[position].chennel_name)


            val intent = Intent(requireContext, PlayChannelActivity::class.java)
            intent.putExtra("channelname",channellist[position].chennel_link)
            requireContext.startActivity(intent)
        }

    }
}