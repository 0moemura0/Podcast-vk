package com.example.podcast_vk.view.player.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PlayerAudio constructor(
        var title: String,
        var position: Int? = -1,
        val path: String,
) : Parcelable {

    companion object {

        fun createFromURL(title: String, url: String): PlayerAudio {
            return PlayerAudio(title = title, path = url)
        }

        fun createFromFilePath(title: String, filePath: String): PlayerAudio {
            return PlayerAudio(title = title, path = filePath)
        }
    }
}