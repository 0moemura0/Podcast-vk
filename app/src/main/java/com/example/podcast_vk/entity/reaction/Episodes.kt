package com.example.podcast_vk.entity.reaction

import com.example.podcast_vk.entity.reaction.Episode
import com.google.gson.annotations.SerializedName

data class Episodes(@SerializedName("episodes") val episodesList: List<Episode>) {
}