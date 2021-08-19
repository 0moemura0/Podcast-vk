package com.example.podcast_vk

import com.google.gson.annotations.SerializedName

data class StatisticItem(
    @SerializedName("time") val time: Int,
    @SerializedName("reaction_id") val reactionId: Int,
    @SerializedName("sex") val sex: String,
    @SerializedName("age") val age: Int,
    @SerializedName("city_id") val cityId: Int
) {

}
