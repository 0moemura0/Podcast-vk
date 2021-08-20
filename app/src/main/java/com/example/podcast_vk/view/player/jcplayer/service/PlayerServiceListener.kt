package com.example.podcast_vk.view.player.jcplayer.service

import com.example.podcast_vk.view.player.jcplayer.general.Status


/**
 * @author Jean Carlos (Github: @jeancsanchez)
 * @date 04/08/18.
 * Jesus loves you.
 */
interface PlayerServiceListener {

    /**
     * Notifies on prepared audio for the service listeners
     */
    fun onPreparedListener(status: Status)

    /**
     * Notifies on time changed for the service listeners
     */
    fun onTimeChangedListener(status: Status)

    /**
     * Notifies on continue for the service listeners
     */
    fun onContinueListener(status: Status)

    /**
     * Notifies on completed audio for the service listeners
     */
    fun onCompletedListener()

    /**
     * Notifies on paused for the service listeners
     */
    fun onPausedListener(status: Status)

    /**
     * Notifies on stopped for the service listeners
     */
    fun onStoppedListener(status: Status)

    /**
     * Notifies an error for the service listeners
     */
    fun onError(exception: Exception)
}