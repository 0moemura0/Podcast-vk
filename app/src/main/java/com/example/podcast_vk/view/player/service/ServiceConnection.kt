package com.example.podcast_vk.view.player.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

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

    fun connect(
        onConnected: ((PlayerService.PlayerServiceBinder?) -> Unit)? = null,
        onDisconnected: ((Unit) -> Unit)? = null
    ) {
        this.onConnected = onConnected
        this.onDisconnected = onDisconnected

        if (serviceBound.not()) {
            val intent = Intent(context.applicationContext, PlayerService::class.java)
            context.applicationContext.bindService(intent, this, Context.BIND_AUTO_CREATE)
        }
    }

    fun disconnect() {
        context.unbindService(this)
    }
}
