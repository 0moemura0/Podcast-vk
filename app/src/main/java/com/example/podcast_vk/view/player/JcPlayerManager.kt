package com.example.podcast_vk.view.player

import android.content.Context
import com.example.podcast_vk.view.player.general.Status
import com.example.podcast_vk.view.player.model.PlayerAudio
import com.example.podcast_vk.view.player.service.PlayerService
import com.example.podcast_vk.view.player.service.PlayerServiceListener
import com.example.podcast_vk.view.player.service.ServiceConnection
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList


class PlayerManager
private constructor(private val serviceConnection: ServiceConnection) : PlayerServiceListener {

    lateinit var context: Context
    private var jcPlayerService: PlayerService? = null
    private var serviceBound = false
    var playlist: ArrayList<PlayerAudio> = ArrayList()
    private var currentPositionList: Int = 0
    private val managerListeners: CopyOnWriteArrayList<PlayerManagerListener> =
        CopyOnWriteArrayList()

    var jcPlayerManagerListener: PlayerManagerListener? = null
        set(value) {
            value?.let {
                if (managerListeners.contains(it).not()) {
                    managerListeners.add(it)
                }
            }
            field = value
        }

    val currentAudio: PlayerAudio?
        get() = jcPlayerService?.currentAudio


    init {
        initService()
    }

    companion object {

        @Volatile
        private var INSTANCE: WeakReference<PlayerManager>? = null

        @JvmStatic
        fun getInstance(
            context: Context,
            playlist: ArrayList<PlayerAudio>? = null,
            listener: PlayerManagerListener? = null
        ): WeakReference<PlayerManager> = INSTANCE ?: let {
            INSTANCE = WeakReference(
                PlayerManager(ServiceConnection(context)).also {
                    it.context = context
                    it.playlist = playlist ?: ArrayList()
                    it.jcPlayerManagerListener = listener
                }
            )
            INSTANCE!!
        }
    }


    private fun initService(connectionListener: ((service: PlayerService?) -> Unit)? = null) =
        serviceConnection.connect(
            onConnected = { binder ->
                jcPlayerService = binder?.service.also { service ->
                    serviceBound = true
                    connectionListener?.invoke(service)
                }
            }
        ) {
            serviceBound = false
        }


    fun playAudio(jcAudio: PlayerAudio) {
        if (playlist.isNotEmpty()) {
            jcPlayerService?.let { service ->
                service.serviceListener = this
                service.play(jcAudio)
            } ?: initService { service ->
                jcPlayerService = service
                playAudio(jcAudio)
            }
        }
    }


    fun nextAudio() {
        if (playlist.isEmpty()) {
            throw Exception()
        } else {
            jcPlayerService?.let { service ->
                service.stop()
                getNextAudio()?.let { service.play(it) } ?: service.finalize()

            }
        }
    }

    fun previousAudio() {
        if (playlist.isEmpty()) {
            throw Exception()
        } else {
            jcPlayerService?.let { service ->

                service.stop()
                getPreviousAudio().let { service.play(it) }

            }
        }
    }

    /**
     * Pauses the current audio.
     */
    fun pauseAudio() {
        jcPlayerService?.let { service -> currentAudio?.let { service.pause(it) } }
    }

    /**
     * Continues the stopped audio.
     */

    fun continueAudio() {
        if (playlist.isEmpty()) {
            throw Exception()
        } else {
            val audio = jcPlayerService?.currentAudio ?: let { playlist.first() }
            playAudio(audio)
        }
    }

    fun seekTo(time: Int) {
        jcPlayerService?.seekTo(time)
    }


    private fun getNextAudio(): PlayerAudio? {
        return try {
            playlist[currentPositionList.inc()]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }


    private fun getPreviousAudio(): PlayerAudio {
        return try {
            playlist[currentPositionList.dec()]

        } catch (e: IndexOutOfBoundsException) {
            return playlist.first()
        }

    }


    override fun onPreparedListener(status: Status) {
        updatePositionAudioList()

        for (listener in managerListeners) {
            listener.onPreparedAudio(status)
        }
    }

    override fun onTimeChangedListener(status: Status) {
        for (listener in managerListeners) {
            listener.onTimeChanged(status)

            if (status.currentPosition in 1..2) {
                listener.onPlaying(status)
            }
        }
    }

    override fun onContinueListener(status: Status) {
        for (listener in managerListeners) {
            listener.onContinueAudio(status)
        }
    }

    override fun onCompletedListener() {
        for (listener in managerListeners) {
            listener.onCompletedAudio()
        }
    }

    override fun onPausedListener(status: Status) {
        for (listener in managerListeners) {
            listener.onPaused(status)
        }
    }

    override fun onStoppedListener(status: Status) {
        for (listener in managerListeners) {
            listener.onStopped(status)
        }
    }

    private fun updatePositionAudioList() {
        playlist.indices
            .singleOrNull { playlist[it] == currentAudio }
            ?.let { this.currentPositionList = it }
            ?: let { this.currentPositionList = 0 }
    }

    fun isPlaying(): Boolean {
        return jcPlayerService?.isPlaying ?: false
    }

    fun isPaused(): Boolean {
        return jcPlayerService?.isPaused ?: true
    }


    fun kill() {
        jcPlayerService?.let {
            it.stop()
            it.stopSelf()
            it.stopForeground(true)
        }

        serviceConnection.disconnect()
        managerListeners.clear()
        INSTANCE = null
    }
}
