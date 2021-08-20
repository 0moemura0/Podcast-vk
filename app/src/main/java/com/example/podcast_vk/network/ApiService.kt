package com.example.podcast_vk.network

import com.example.podcast_vk.entity.podcast.RSSFeed
import com.example.podcast_vk.entity.reaction.ReactionResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface ApiService {

    @GET("podcasts-147415323_-1000000.rss")
    fun loadRSSPodcast(): Observable<RSSFeed>?

    @GET(".")
    fun loadReaction(): Observable<ReactionResponse>?

    companion object Factory {
        private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        private val client = OkHttpClient().newBuilder()
            .addInterceptor(interceptor).build()

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://vk.com/")
                .build()
            return retrofit.create(ApiService::class.java);
        }
    }
}