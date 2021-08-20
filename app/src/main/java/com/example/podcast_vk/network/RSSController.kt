package com.example.podcast_vk.network

import com.example.podcast_vk.entity.podcast.RSSFeed
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.converter.simplexml.SimpleXmlConverterFactory

import retrofit2.Retrofit


class RSSController : Callback<RSSFeed> {
    companion object {
        const val BASE_URL = "https://vk.com/"
    }

    fun start() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient().newBuilder()
            .addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .client(client)
            .build()
        val vogellaAPI: ApiService = retrofit.create(ApiService::class.java)
        val call: Call<RSSFeed>? = vogellaAPI.loadRSSPodcast()
        call?.enqueue(this)
    }

    override fun onResponse(call: Call<RSSFeed?>, response: Response<RSSFeed?>) {
        if (response.isSuccessful) {
            val rss = response.body()

            println("Channel title: " + rss?.channel?.title)
            rss?.channel?.podcastItems?.forEach { article ->
                println(
                    "Title: ${article.title} \n" +
                            "url: ${article.enclosure.url} \n" +
                            "duration: ${article.duration} \n" +
                            "description: ${article.description?.description} \n" +
                            "image: ${article.image} \n"
                )
            }
        } else {
            println(response.errorBody())
        }
    }

    override fun onFailure(call: Call<RSSFeed?>, t: Throwable) {
        t.printStackTrace();
    }

}