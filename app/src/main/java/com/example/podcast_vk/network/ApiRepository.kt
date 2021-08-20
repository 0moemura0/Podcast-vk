package com.example.podcast_vk.network

import com.example.podcast_vk.entity.podcast.RSSFeed
import io.reactivex.Observable

object ApiRepositoryProvider {
    fun providePodcastRepository(apiService: ApiService): ApiRepository {
        return ApiRepository(apiService)
    }
}

class ApiRepository(private val apiService: ApiService) {
    fun getPodcasts(): Observable<RSSFeed>? {
        return apiService.loadRSSPodcast()
    }
}