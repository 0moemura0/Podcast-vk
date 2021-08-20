package com.example.podcast_vk.view.player.jcplayer.general;

import com.example.podcast_vk.view.player.jcplayer.model.PlayerAudio;

/**
 * Created by rio on 02 January 2017.
 */
public class Status {
    private PlayerAudio jcAudio;
    private long duration;
    private long currentPosition;
    private PlayState playState;

    public Status() {
        this(null, 0, 0, PlayState.PREPARING);
    }

    public Status(PlayerAudio jcAudio, long duration, long currentPosition, PlayState playState) {
        this.jcAudio = jcAudio;
        this.duration = duration;
        this.currentPosition = currentPosition;
        this.playState = playState;
    }

    public PlayerAudio getPlayerAudio() {
        return jcAudio;
    }

    public void setPlayerAudio(PlayerAudio jcAudio) {
        this.jcAudio = jcAudio;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(long currentPosition) {
        this.currentPosition = currentPosition;
    }

    public PlayState getPlayState() {
        return playState;
    }

    public void setPlayState(PlayState playState) {
        this.playState = playState;
    }

    public enum PlayState {
        PLAY, PAUSE, STOP, CONTINUE, PREPARING, PLAYING
    }
}
