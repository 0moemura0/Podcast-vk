package com.example.podcast_vk.view.player.jcplayer.model

import android.os.Parcelable
import androidx.annotation.RawRes
import com.example.podcast_vk.view.player.jcplayer.general.Origin
import kotlinx.android.parcel.Parcelize


/**
 * This class is an type of audio model .
 * @author Jean Carlos (Github: @jeancsanchez)
 * @date 27/06/16.
 * Jesus loves you.
 */
@Parcelize
data class PlayerAudio constructor(
        var title: String,
        var position: Int? = -1,
        val path: String,
        val origin: Origin
) : Parcelable {

    companion object {

        @JvmStatic
        fun createFromRaw(@RawRes rawId: Int): PlayerAudio {
            return PlayerAudio(title = rawId.toString(), path = rawId.toString(), origin = Origin.RAW)
        }

        @JvmStatic
        fun createFromRaw(title: String, @RawRes rawId: Int): PlayerAudio {
            return PlayerAudio(title = title, path = rawId.toString(), origin = Origin.RAW)
        }

        @JvmStatic
        fun createFromAssets(assetName: String): PlayerAudio {
            return PlayerAudio(title = assetName, path = assetName, origin = Origin.ASSETS)
        }

        @JvmStatic
        fun createFromAssets(title: String, assetName: String): PlayerAudio {
            return PlayerAudio(title = title, path = assetName, origin = Origin.ASSETS)
        }

        @JvmStatic
        fun createFromURL(url: String): PlayerAudio {
            return PlayerAudio(title = url, path = url, origin = Origin.URL)
        }

        @JvmStatic
        fun createFromURL(title: String, url: String): PlayerAudio {
            return PlayerAudio(title = title, path = url, origin = Origin.URL)
        }

        @JvmStatic
        fun createFromFilePath(filePath: String): PlayerAudio {
            return PlayerAudio(title = filePath, path = filePath, origin = Origin.FILE_PATH)
        }

        @JvmStatic
        fun createFromFilePath(title: String, filePath: String): PlayerAudio {
            return PlayerAudio(title = title, path = filePath, origin = Origin.FILE_PATH)
        }
    }
}