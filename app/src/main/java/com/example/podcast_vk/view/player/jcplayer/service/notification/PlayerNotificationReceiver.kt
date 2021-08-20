package com.example.jean.jcplayer.service.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.podcast_vk.view.player.jcplayer.PlayerManager
import com.example.podcast_vk.view.player.jcplayer.service.notification.NotificationPlayer

class PlayerNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val jcPlayerManager = PlayerManager.getInstance(context)
        var action: String? = ""

        if (intent.hasExtra(NotificationPlayer.ACTION)) {
            action = intent.getStringExtra(NotificationPlayer.ACTION)
        }

        when (action) {
            NotificationPlayer.PLAY -> try {
                jcPlayerManager.get()?.continueAudio()
                jcPlayerManager.get()?.updateNotification()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            NotificationPlayer.PAUSE -> try {
                jcPlayerManager.get()?.pauseAudio()
                jcPlayerManager.get()?.updateNotification()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            NotificationPlayer.NEXT -> try {
                jcPlayerManager.get()?.nextAudio()
            } catch (e: Exception) {
                try {
                    jcPlayerManager.get()?.continueAudio()
                } catch (e1: Exception) {
                    e1.printStackTrace()
                }

            }

            NotificationPlayer.PREVIOUS -> try {
                jcPlayerManager.get()?.previousAudio()
            } catch (e: Exception) {
                try {
                    jcPlayerManager.get()?.continueAudio()
                } catch (e1: Exception) {
                    e1.printStackTrace()
                }
            }

        }
    }
}
