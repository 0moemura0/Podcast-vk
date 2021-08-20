package com.example.podcast_vk.entity.reaction

import com.google.gson.annotations.SerializedName

data class StatisticItem(
    @SerializedName("time") val time: Int?,
    @SerializedName("reaction_id") val reactionId: String?,
    @SerializedName("sex") val sex: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("city_id") val cityId: Int?,
)