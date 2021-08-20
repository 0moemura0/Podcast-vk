package com.example.podcast_vk.view.player

import com.example.podcast_vk.view.player.general.Status


interface PlayerManagerListener {

    fun onPreparedAudio(status: Status)
    fun onCompletedAudio()
    fun onPaused(status: Status)
    fun onContinueAudio(status: Status)
    fun onPlaying(status: Status)
    fun onTimeChanged(status: Status)
    fun onStopped(status: Status)
}