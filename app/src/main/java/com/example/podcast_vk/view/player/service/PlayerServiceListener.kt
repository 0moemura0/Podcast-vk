package com.example.podcast_vk.view.player.service

import com.example.podcast_vk.view.player.general.Status

interface PlayerServiceListener {
    fun onPreparedListener(status: Status)
    fun onTimeChangedListener(status: Status)
    fun onContinueListener(status: Status)
    fun onCompletedListener()
    fun onPausedListener(status: Status)
    fun onStoppedListener(status: Status)
}