package com.example.podcast_vk.view.player.jcplayer

import android.content.Context
import com.example.jean.jcplayer.general.errors.JcpServiceDisconnectedError
import com.example.podcast_vk.view.player.jcplayer.model.PlayerAudio
import com.example.podcast_vk.view.player.jcplayer.service.PlayerService
import com.example.podcast_vk.view.player.jcplayer.service.PlayerServiceListener
import com.example.jean.jcplayer.service.ServiceConnection
import com.example.podcast_vk.view.player.jcplayer.service.notification.NotificationPlayer
import com.example.podcast_vk.view.player.jcplayer.general.Status
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * This class is the player manager. Handles all interactions and communicates with [PlayerService].
 * @author Jean Carlos (Github: @jeancsanchez)
 * @date 12/07/16.
 * Jesus loves you.
 */
class PlayerManager
private constructor(private val serviceConnection: ServiceConnection) : PlayerServiceListener {

    lateinit var context: Context
    private var jcNotificationPlayer: NotificationPlayer? = null
    private var jcPlayerService: PlayerService? = null
    private var serviceBound = false
    var playlist: ArrayList<PlayerAudio> = ArrayList()
    private var currentPositionList: Int = 0
    private val managerListeners: CopyOnWriteArrayList<PlayerManagerListener> = CopyOnWriteArrayList()

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

    var onShuffleMode: Boolean = false

    var repeatPlaylist: Boolean = false
        private set

    var repeatCurrAudio: Boolean = false
        private set

    private var repeatCount = 0

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

    /**
     * Connects with audio service.
     */
    private fun initService(connectionListener: ((service: PlayerService?) -> Unit)? = null) =
            serviceConnection.connect(
                    playlist = playlist,
                    onConnected = { binder ->
                        jcPlayerService = binder?.service.also { service ->
                            serviceBound = true
                            connectionListener?.invoke(service)
                        } ?: throw JcpServiceDisconnectedError
                    },
                    onDisconnected = {
                        serviceBound = false
                        throw  JcpServiceDisconnectedError
                    }
            )

    /**
     * Plays the given [PlayerAudio].
     * @param jcAudio The audio to be played.
     */
    fun playAudio(jcAudio: PlayerAudio) {
        if (playlist.isEmpty()) {
            notifyError(Exception())
        } else {
            jcPlayerService?.let { service ->
                service.serviceListener = this
                service.play(jcAudio)
            } ?: initService { service ->
                jcPlayerService = service
                playAudio(jcAudio)
            }
        }
    }


    /**
     * Goes to next audio.
     */

    fun nextAudio() {
        if (playlist.isEmpty()) {
            throw Exception()
        } else {
            jcPlayerService?.let { service ->
                if (repeatCurrAudio) {
                    currentAudio?.let {
                        service.seekTo(0)
                        service.onPrepared(service.getMediaPlayer()!!)
                    }
                } else {
                    service.stop()
                    getNextAudio()?.let { service.play(it) } ?: service.finalize()
                }
            }
        }
    }

    /**
     * Goes to previous audio.
     */
    fun previousAudio() {
        if (playlist.isEmpty()) {
            throw Exception()
        } else {
            jcPlayerService?.let { service ->
                if (repeatCurrAudio) {
                    currentAudio?.let {
                        service.seekTo(0)
                        service.onPrepared(service.getMediaPlayer()!!)
                    }
                } else {
                    service.stop()
                    getPreviousAudio().let { service.play(it) }
                }
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

    /**
     * Creates a new notification with icon resource.
     * @param iconResource The icon resource path.
     */
    fun createNewNotification(iconResource: Int) {
        jcNotificationPlayer
                ?.createNotificationPlayer(currentAudio?.title, iconResource)
                ?: let {
                    jcNotificationPlayer = NotificationPlayer
                            .getInstance(context)
                            .get()
                            .also { notification ->
                                jcPlayerManagerListener = notification
                            }

                    createNewNotification(iconResource)
                }
    }

    /**
     * Updates the current notification
     */
    fun updateNotification() {
        jcNotificationPlayer
                ?.updateNotification()
                ?: let {
                    jcNotificationPlayer = NotificationPlayer
                            .getInstance(context)
                            .get()
                            .also { jcPlayerManagerListener = it }

                    updateNotification()
                }
    }

    /**
     * Jumps audio to the specific time.
     */
    fun seekTo(time: Int) {
        jcPlayerService?.seekTo(time)
    }


    private fun getNextAudio(): PlayerAudio? {
        return if (onShuffleMode) {
            playlist[Random().nextInt(playlist.size)]
        } else {
            try {
                playlist[currentPositionList.inc()]
            } catch (e: IndexOutOfBoundsException) {
                if (repeatPlaylist) {
                    return playlist.first()
                }

                null
            }
        }
    }

    private fun getPreviousAudio(): PlayerAudio {
        return if (onShuffleMode) {
            playlist[Random().nextInt(playlist.size)]
        } else {
            try {
                playlist[currentPositionList.dec()]

            } catch (e: IndexOutOfBoundsException) {
                return playlist.first()
            }
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

            if (status.currentPosition in 1..2 /* Not to call this every second */) {
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

    override fun onError(exception: Exception) {
        notifyError(exception)
    }

    /**
     * Notifies errors for the service listeners
     */
    private fun notifyError(throwable: Throwable) {
        for (listener in managerListeners) {
            listener.onJcpError(throwable)
        }
    }

    /**
     * Handles the repeat mode.
     */
    fun activeRepeat() {
        if (repeatCount == 0) {
            repeatPlaylist = true
            repeatCurrAudio = false
            repeatCount++
            return
        }

        if (repeatCount == 1) {
            repeatCurrAudio = true
            repeatPlaylist = false
            repeatCount++
            return
        }

        if (repeatCount == 2) {
            repeatCurrAudio = false
            repeatPlaylist = false
            repeatCount = 0
        }
    }

    /**
     * Updates the current position of the audio list.
     */
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

    /**
     * Kills the JcPlayer, including Notification and service.
     */
    fun kill() {
        jcPlayerService?.let {
            it.stop()
            it.stopSelf()
            it.stopForeground(true)
        }

        serviceConnection.disconnect()
        jcNotificationPlayer?.destroyNotificationIfExists()
        managerListeners.clear()
        INSTANCE = null
    }
}
