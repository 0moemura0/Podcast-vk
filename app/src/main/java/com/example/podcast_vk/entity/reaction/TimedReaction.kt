package com.example.podcast_vk.entity.reaction

import com.google.gson.annotations.SerializedName

data class TimedReaction(
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: String,
    @SerializedName("available_reactions") val availableReactions: List<Int>,
)