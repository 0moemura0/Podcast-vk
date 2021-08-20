package com.example.jean.jcplayer.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.podcast_vk.view.player.jcplayer.model.PlayerAudio
import com.example.podcast_vk.view.player.jcplayer.service.PlayerService
import com.example.podcast_vk.view.player.jcplayer.service.notification.NotificationPlayer
import java.io.Serializable

/**
 * This class is an [ServiceConnection] for the [PlayerService] class.
 * @author Jean Carlos (Github: @jeancsanchez)
 * @date 15/02/18.
 * Jesus loves you.
 */
class ServiceConnection(private val context: Context) :
    ServiceConnection {

    private var serviceBound = false
    private var onConnected: ((PlayerService.PlayerServiceBinder?) -> Unit)? = null
    private var onDisconnected: ((Unit) -> Unit)? = null

    override fun onServiceDisconnected(name: ComponentName?) {
        serviceBound = false
        onDisconnected?.invoke(Unit)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        serviceBound = true
        onConnected?.invoke(service as PlayerService.PlayerServiceBinder?)
    }

    /**
     * Connects with the [PlayerService].
     */
    fun connect(
        playlist: ArrayList<PlayerAudio>? = null,
        currentAudio: PlayerAudio? = null,
        onConnected: ((PlayerService.PlayerServiceBinder?) -> Unit)? = null,
        onDisconnected: ((Unit) -> Unit)? = null
    ) {
        this.onConnected = onConnected
        this.onDisconnected = onDisconnected

        if (serviceBound.not()) {
            val intent = Intent(context.applicationContext, PlayerService::class.java)
            intent.putExtra(NotificationPlayer.PLAYLIST, playlist as Serializable?)
            intent.putExtra(NotificationPlayer.CURRENT_AUDIO, currentAudio)
            context.applicationContext.bindService(intent, this, Context.BIND_AUTO_CREATE)
        }
    }

    /**
     * Disconnects from the [PlayerService].
     */
    fun disconnect() {
        if (serviceBound)
            try {
                context.unbindService(this)
            } catch (e: IllegalArgumentException) {
                //TODO: Add readable exception here
            }
    }
}
