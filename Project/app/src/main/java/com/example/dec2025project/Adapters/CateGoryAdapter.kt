package com.example.livetvnewapp.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.livetvnewapp.Activitys.ChannelActivity
import com.example.livetvnewapp.Models.CategoryList
import com.example.livetvnewapp.Models.Chennel
import com.example.livetvnewapp.databinding.CategoryItemsBinding

class CateGoryAdapter : RecyclerView.Adapter<CateGoryAdapter.ViewHolder>() {

    companion object {
        lateinit var listofchannel: List<Chennel>

    }


    private var catelist = ArrayList<CategoryList>()
    private lateinit var requireContext: Context

    fun setcatlist(movieList: List<CategoryList>, requireContext: Context) {
        this.catelist = movieList as ArrayList<CategoryList>
        this.requireContext = requireContext
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: CategoryItemsBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryItemsBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return catelist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        Glide.with(holder.itemView)
            .load("https://tvguide.andromenia.com/smart_tv_guide/assets/live_all_guide_k/" + catelist[position].cat_icon)
            .into(holder.binding.imgcat)


        holder.binding.imgcat.setOnClickListener {
            listofchannel = catelist[position].chennel_list
            val intent = Intent(requireContext, ChannelActivity::class.java)
            requireContext.startActivity(intent)
        }
    }


}

