package com.example.podcast_vk.entity.reaction

import com.example.podcast_vk.entity.reaction.Reaction
import com.google.gson.annotations.SerializedName

data class Reactions(@SerializedName("reactions") val reactionList: List<Reaction>) {
}