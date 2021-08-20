package com.example.podcast_vk.view.player.general

import com.example.podcast_vk.view.player.model.PlayerAudio

class Status @JvmOverloads constructor(
    var playerAudio: PlayerAudio? = null,
    var duration: Long = 0,
    var currentPosition: Long = 0,
    var playState: PlayState = PlayState.PREPARING
) {
    enum class PlayState {
        PLAY, PAUSE, STOP, CONTINUE, PREPARING, PLAYING
    }
}