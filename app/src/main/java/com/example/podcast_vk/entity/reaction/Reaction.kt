package com.example.podcast_vk.entity.reaction

import com.google.gson.annotations.SerializedName

data class Reaction(
    @SerializedName("reaction_id") val reactionId: Int,
    @SerializedName("emoji") val emoji: Char,
    @SerializedName("description") val description: String
) {
}