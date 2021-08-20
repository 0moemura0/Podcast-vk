package com.example.podcast_vk.view.player.jcplayer

import com.example.podcast_vk.view.player.jcplayer.general.Status


/**
 * This class represents all the [PlayerService] callbacks.
 * @author Jean Carlos (Github: @jeancsanchez)
 * @date 18/12/17.
 * Jesus loves you.
 */
interface PlayerManagerListener {

    /**
     * Prepares the new audio.
     * @param audioName The audio name.
     * @param duration The audio duration.
     */
    fun onPreparedAudio(status: Status)

    /**
     * The audio ends.
     */
    fun onCompletedAudio()

    /**
     * The audio is paused.
     */
    fun onPaused(status: Status)

    /**
     * The audio was paused and user hits play again.
     */
    fun onContinueAudio(status: Status)

    /**
     *  Called when there is an audio playing.
     */
    fun onPlaying(status: Status)

    /**
     * Called when the time of the audio changed.
     */
    fun onTimeChanged(status: Status)


    /**
     * Called when the player stops.
     */
    fun onStopped(status: Status)

    /**
     * Notifies some error.
     * @param throwable The error.
     */
    fun onJcpError(throwable: Throwable)
}