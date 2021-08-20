package com.example.podcast_vk.view.player.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.example.podcast_vk.view.player.general.Status
import com.example.podcast_vk.view.player.model.PlayerAudio
import java.io.IOException
import java.util.concurrent.TimeUnit

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

    private val status = Status()

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

        if (isAudioFileValid(jcAudio.path)) {
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
                        it.setDataSource(jcAudio.path)
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

    private fun updateStatus(audio: PlayerAudio? = null, status: Status.PlayState): Status {
        currentAudio = audio
        this.status.playerAudio = audio
        this.status.playState = status

        mediaPlayer?.let {
            this.status.duration = it.duration.toLong()
            this.status.currentPosition = it.currentPosition.toLong()
        }

        when (status) {
            Status.PlayState.PLAY -> {
                try {
                    mediaPlayer?.start()
                    isPlaying = true
                    isPaused = false

                } catch (ignore: Exception) {
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

        return this.status
    }

    private fun updateTime() {
        object : Thread() {
            override fun run() {
                while (isPlaying) {
                    try {
                        val status = updateStatus(currentAudio, Status.PlayState.PLAYING)
                        serviceListener?.onTimeChangedListener(status)
                        sleep(TimeUnit.SECONDS.toMillis(1))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    private fun isAudioFileValid(path: String): Boolean {
        return path.startsWith("http") || path.startsWith("https")
    }

    fun getMediaPlayer(): MediaPlayer? {
        return mediaPlayer
    }

    fun finalize() {
        onDestroy()
        stopSelf()
    }
}
