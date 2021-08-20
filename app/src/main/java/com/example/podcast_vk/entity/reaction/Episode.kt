package com.example.podcast_vk.entity.reaction

import com.google.gson.annotations.SerializedName


data class Episode(
    @SerializedName("guid") val guid: String?,
    @SerializedName("default_reactions") val defaultReactions: List<Int?>?,
    @SerializedName("timed_reactions") val timedReactions: List<TimedReaction?>?,
    @SerializedName("statistics") val statistics: List<StatisticItem?>?,
)