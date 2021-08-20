package com.example.podcast_vk.entity.reaction

import com.google.gson.annotations.SerializedName

data class ReactionResponse(
    @SerializedName("reactions")
    val reactions: List<Reaction>?,
    @SerializedName("episodes")
    val episodes: List<Episode>?

)