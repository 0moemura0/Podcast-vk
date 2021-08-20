package com.example.podcast_vk.view.player.general


object PlayerUtil {

    fun toTimeSongString(currentPosition: Int): String {
        val aux = currentPosition / 1000
        val minutes = (aux / 60)
        val seconds = (aux % 60)
        val sMinutes = if (minutes < 10) "0$minutes" else minutes.toString()
        val sSeconds = if (seconds < 10) "0$seconds" else seconds.toString()
        return "$sMinutes:$sSeconds"
    }
}