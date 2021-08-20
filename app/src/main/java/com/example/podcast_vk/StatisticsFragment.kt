package com.example.podcast_vk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.podcast_vk.entity.reaction.ReactionResponse
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream

class StatisticsFragment : Fragment() {
    private val viewModel: PodcastViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myJson: String =
            inputStreamToString(
                resources.assets.open("podcast.json")
            )
        val gson = Gson()
        val respon: ReactionResponse = gson.fromJson(myJson, ReactionResponse::class.java)
        println(myJson)
        println(respon.toString())


        val byCity = mutableListOf<Double>()

        respon.episodes?.forEach {
            it.statistics.forEach {
                //it.
            }
        }


















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