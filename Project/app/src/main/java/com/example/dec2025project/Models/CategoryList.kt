package com.example.livetvnewapp.Models

data class CategoryList(
    val cat_icon: String,
    val cat_id: String,
    val cat_name: String,
    val chennel_list: List<Chennel>
)