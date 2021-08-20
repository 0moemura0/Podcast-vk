package com.example.podcast_vk.view.player.jcplayer.service

import android.app.Service
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import com.example.podcast_vk.view.player.jcplayer.general.Origin
import com.example.podcast_vk.view.player.jcplayer.general.Status
import com.example.podcast_vk.view.player.jcplayer.model.PlayerAudio
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * This class is an Android [Service] that handles all player changes on background.
 * @author Jean Carlos (Github: @jeancsanchez)
 * @date 02/07/16.
 * Jesus loves you.
 */
class PlayerService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener {

    private val binder = PlayerServiceBinder()

    private var mediaPlayer: MediaPlayer? = null

    var isPlaying: Boolean = false
        private set

    var isPaused: Boolean = true
        private set

    var currentAudio: PlayerAudio? = null
        private set

    private val jcStatus = Status()

    private var assetFileDescriptor: AssetFileDescriptor? = null // For Asset and Raw file.

    var serviceListener: PlayerServiceListener? = null


    inner class PlayerServiceBinder : Binder() {
        val service: PlayerService
            get() = this@PlayerService
    }

    override fun onBind(intent: Intent): IBinder = binder


    fun play(jcAudio: PlayerAudio): Status {
        val tempPlayerAudio = currentAudio
        currentAudio = jcAudio
        var status = Status()

        if (isAudioFileValid(jcAudio.path, jcAudio.origin)) {
            try {
                mediaPlayer?.let {
                    if (isPlaying) {
                        stop()
                        play(jcAudio)
                    } else {
                        if (tempPlayerAudio !== jcAudio) {
                            stop()
                            play(jcAudio)
                        } else {
                            status = updateStatus(jcAudio, Status.PlayState.CONTINUE)
                            updateTime()
                            serviceListener?.onContinueListener(status)
                        }
                    }
                } ?: let {
                    mediaPlayer = MediaPlayer().also {
                        when (jcAudio.origin) {
                            Origin.URL -> it.setDataSource(jcAudio.path)
                            Origin.RAW -> assetFileDescriptor =
                                    applicationContext.resources.openRawResourceFd(
                                            Integer.parseInt(jcAudio.path)
                                    ).also { descriptor ->
                                        it.setDataSource(
                                                descriptor.fileDescriptor,
                                                descriptor.startOffset,
                                                descriptor.length
                                        )
                                        descriptor.close()
                                        assetFileDescriptor = null
                                    }
                            Origin.ASSETS -> {
                                assetFileDescriptor = applicationContext.assets.openFd(jcAudio.path)
                                        .also { descriptor ->
                                            it.setDataSource(
                                                    descriptor.fileDescriptor,
                                                    descriptor.startOffset,
                                                    descriptor.length
                                            )

                                            descriptor.close()
                                            assetFileDescriptor = null
                                        }
                            }
                            Origin.FILE_PATH ->
                                it.setDataSource(applicationContext, Uri.parse(jcAudio.path))
                        }

                        it.prepareAsync()
                        it.setOnPreparedListener(this)
                        it.setOnBufferingUpdateListener(this)
                        it.setOnCompletionListener(this)
                        it.setOnErrorListener(this)

                        status = updateStatus(jcAudio, Status.PlayState.PREPARING)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            throwError(jcAudio.path, jcAudio.origin)
        }

        return status
    }

    fun pause(jcAudio: PlayerAudio): Status {
        val status = updateStatus(jcAudio, Status.PlayState.PAUSE)
        serviceListener?.onPausedListener(status)
        return status
    }

    fun stop(): Status {
        val status = updateStatus(status = Status.PlayState.STOP)
        serviceListener?.onStoppedListener(status)
        return status
    }


    fun seekTo(time: Int) {
        mediaPlayer?.seekTo(time)
    }

    override fun onBufferingUpdate(mediaPlayer: MediaPlayer, i: Int) {}

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        serviceListener?.onCompletedListener()
    }

    override fun onError(mediaPlayer: MediaPlayer, i: Int, i1: Int): Boolean {
        return false
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        this.mediaPlayer = mediaPlayer
        val status = updateStatus(currentAudio, Status.PlayState.PLAY)

        updateTime()
        serviceListener?.onPreparedListener(status)
    }

    private fun updateStatus(jcAudio: PlayerAudio? = null, status: Status.PlayState): Status {
        currentAudio = jcAudio
        jcStatus.playerAudio = jcAudio
        jcStatus.playState = status

        mediaPlayer?.let {
            jcStatus.duration = it.duration.toLong()
            jcStatus.currentPosition = it.currentPosition.toLong()
        }

        when (status) {
            Status.PlayState.PLAY -> {
                try {
                    mediaPlayer?.start()
                    isPlaying = true
                    isPaused = false

                } catch (exception: Exception) {
                    serviceListener?.onError(exception)
                }
            }

            Status.PlayState.STOP -> {
                mediaPlayer?.let {
                    it.stop()
                    it.reset()
                    it.release()
                    mediaPlayer = null
                }

                isPlaying = false
                isPaused = true
            }

            Status.PlayState.PAUSE -> {
                mediaPlayer?.pause()
                isPlaying = false
                isPaused = true
            }

            Status.PlayState.PREPARING -> {
                isPlaying = false
                isPaused = true
            }

            Status.PlayState.PLAYING -> {
                isPlaying = true
                isPaused = false
            }

            else -> { // CONTINUE case
                mediaPlayer?.start()
                isPlaying = true
                isPaused = false
            }
        }

        return jcStatus
    }

    private fun updateTime() {
        object : Thread() {
            override fun run() {
                while (isPlaying) {
                    try {
                        val status = updateStatus(currentAudio, Status.PlayState.PLAYING)
                        serviceListener?.onTimeChangedListener(status)
                        sleep(TimeUnit.SECONDS.toMillis(1))
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    private fun isAudioFileValid(path: String, origin: Origin): Boolean {
        when (origin) {
            Origin.URL -> return path.startsWith("http") || path.startsWith("https")

            Origin.RAW -> {
                assetFileDescriptor = null
                assetFileDescriptor =
                        applicationContext.resources.openRawResourceFd(Integer.parseInt(path))
                return assetFileDescriptor != null
            }

            Origin.ASSETS -> return try {
                assetFileDescriptor = null
                assetFileDescriptor = applicationContext.assets.openFd(path)
                assetFileDescriptor != null
            } catch (e: IOException) {
                e.printStackTrace() //TODO: need to give user more readable error.
                false
            }

            Origin.FILE_PATH -> {
                val file = File(path)
                //TODO: find an alternative to checking if file is exist, this code is slower on average.
                //read more: http://stackoverflow.com/a/8868140
                return file.exists()
            }

            else -> // We should never arrive here.
                return false // We don't know what the origin of the Audio File
        }
    }

    private fun throwError(path: String, origin: Origin) {
        when (origin) {
            Origin.URL -> throw Exception(path)

            Origin.RAW -> try {
                throw Exception(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            Origin.ASSETS -> try {
                throw Exception(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            Origin.FILE_PATH -> try {
                throw Exception(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getMediaPlayer(): MediaPlayer? {
        return mediaPlayer
    }

    fun finalize() {
        onDestroy()
        stopSelf()
    }
}
