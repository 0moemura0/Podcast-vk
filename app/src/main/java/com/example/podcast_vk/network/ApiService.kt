package com.example.podcast_vk.network

import com.example.podcast_vk.entity.podcast.RSSFeed
import com.example.podcast_vk.entity.reaction.ReactionResponse
import retrofit2.Call

import retrofit2.http.GET


interface ApiService {

    @GET("podcasts-147415323_-1000000.rss")
    fun loadRSSPodcast(): Call<RSSFeed>?

    @GET(".")
    fun loadReaction(): Call<ReactionResponse>?
}