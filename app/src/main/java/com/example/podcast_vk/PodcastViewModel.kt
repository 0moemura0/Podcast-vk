package com.example.podcast_vk

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.podcast_vk.entity.podcast.PodcastItem
import com.example.podcast_vk.entity.reaction.ReactionResponse
import com.example.podcast_vk.network.ApiRepositoryProvider
import com.example.podcast_vk.network.ApiService
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.io.InputStream

class PodcastViewModel : ViewModel() {

    val podcastList = MutableLiveData<List<PodcastItem>>()

    private var isAuth = false


    private val podcastTitle = MutableLiveData<String>()
    private val repository = ApiRepositoryProvider.providePodcastRepository(ApiService.create())

    fun getLogin(): Boolean{
        return isAuth
    }

    fun setLogin(){
        isAuth = true
    }

    fun getReactionsFromAssets(context: Context): ReactionResponse? {
        val myJson: String =
            inputStreamToString(
                context.assets.open("podcast.json")
            )
        val gson = Gson()
        val respon: ReactionResponse? = gson.fromJson(myJson, ReactionResponse::class.java)
        return respon

    }

    @SuppressLint("CheckResult")
    fun getPodcast() {
        repository.getPodcasts()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ result ->
                podcastTitle.postValue(result.channel.title)
                podcastList.postValue(result.channel.podcastItems)
            }, { error ->
                error.printStackTrace()
            })
    }

    private fun inputStreamToString(inputStream: InputStream): String {
        return try {
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            String(bytes)
        } catch (e: IOException) {
            "null"
        }

    }
}