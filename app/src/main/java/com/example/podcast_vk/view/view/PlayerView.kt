package com.example.podcast_vk.view.view

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.example.podcast_vk.R
import com.example.podcast_vk.view.player.PlayerManager
import com.example.podcast_vk.view.player.PlayerManagerListener
import com.example.podcast_vk.view.player.general.PlayerUtil.toTimeSongString
import com.example.podcast_vk.view.player.general.Status
import com.example.podcast_vk.view.player.model.PlayerAudio
import com.example.podcast_vk.view.wave.OnProgressListener
import kotlinx.android.synthetic.main.view_player.view.*


class PlayerView : LinearLayout, View.OnClickListener, OnProgressListener,
    PlayerManagerListener {

    private val playerManager: PlayerManager by lazy {
        PlayerManager.getInstance(context).get()!!
    }

    val myPlaylist: List<PlayerAudio>
        get() = playerManager.playlist

    val isPlaying: Boolean
        get() = playerManager.isPlaying()

    val isPaused: Boolean
        get() = playerManager.isPaused()

    val currentAudio: PlayerAudio?
        get() = playerManager.currentAudio

    var jcPlayerManagerListener: PlayerManagerListener? = null
        set(value) {
            field = value
            playerManager.jcPlayerManagerListener = value
        }


    companion object {
        private const val PULSE_ANIMATION_DURATION = 200L
        private const val TITLE_ANIMATION_DURATION = 600
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

        context.theme
            .obtainStyledAttributes(attrs, R.styleable.PlayerView, 0, 0)
            .also { setAttributes(it) }
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()

        context.theme
            .obtainStyledAttributes(attrs, R.styleable.PlayerView, defStyle, 0)
            .also { setAttributes(it) }
    }

    private fun init() {
        View.inflate(context, R.layout.view_player, this)

        btnNext?.setOnClickListener(this)
        btnPrev?.setOnClickListener(this)
        btnPlay?.setOnClickListener(this)
        btnPause?.setOnClickListener(this)
        wave.onProgressListener = this
    }

    private fun setAttributes(attrs: TypedArray) {
        val defaultColor = ResourcesCompat.getColor(resources, android.R.color.black, null)

        txtCurrentDuration?.setTextColor(
            attrs.getColor(
                R.styleable.PlayerView_text_audio_current_duration_color,
                defaultColor
            )
        )
        txtDuration?.setTextColor(
            attrs.getColor(
                R.styleable.PlayerView_text_audio_duration_color,
                defaultColor
            )
        )

        progressBarPlayer?.indeterminateDrawable?.setColorFilter(
            attrs.getColor(
                R.styleable.PlayerView_progress_color,
                defaultColor
            ), PorterDuff.Mode.SRC_ATOP
        )


        btnPlay?.setColorFilter(
            attrs.getColor(
                R.styleable.PlayerView_play_icon_color,
                defaultColor
            )
        )
        btnPlay?.setImageResource(R.drawable.ic_baseline_play_arrow_24)


        btnPause?.setImageResource(R.drawable.ic_pause)
        btnPause?.setColorFilter(
            attrs.getColor(
                R.styleable.PlayerView_pause_icon_color,
                defaultColor
            )
        )

        btnNext?.setColorFilter(
            attrs.getColor(
                R.styleable.PlayerView_next_icon_color,
                defaultColor
            )
        )
        btnNext?.setImageResource(
            R.drawable.ic_forward_15_36
        )

        btnPrev?.setColorFilter(
            attrs.getColor(
                R.styleable.PlayerView_previous_icon_color,
                defaultColor
            )
        )
        btnPrev?.setImageResource(
            R.drawable.ic_replay_15_36
        )
    }


    fun initPlaylist(
        playlist: List<PlayerAudio>,
        jcPlayerManagerListener: PlayerManagerListener? = null
    ) {
        playerManager.playlist = playlist as ArrayList<PlayerAudio>
        playerManager.jcPlayerManagerListener = jcPlayerManagerListener
        playerManager.jcPlayerManagerListener = this
    }

    fun playAudio(jcAudio: PlayerAudio) {
        showProgressBar()

        playerManager.playlist.let {
            if (it.contains(jcAudio).not()) {
                it.add(jcAudio)
            }

            wave.start()
            val progressAnim =
                ObjectAnimator.ofFloat(wave, "progress", 0F, 100F).apply {
                    interpolator = LinearInterpolator()
                    duration = wave.duration
                }
            progressAnim.start()
            playerManager.playAudio(jcAudio)
        }
    }

    private fun showPlayButton() {
        btnPlay?.postVisible()
        btnPause?.postInvisible()
    }

    private fun showPauseButton() {
        btnPlay?.postInvisible()
        btnPause?.postVisible()
    }

    fun next() {
        playerManager.let { player ->
            player.currentAudio?.let {
                resetPlayerInfo()
                showProgressBar()

                try {
                    player.nextAudio()
                } catch (e: Exception) {
                    dismissProgressBar()
                    e.printStackTrace()
                }
            }
        }
    }


    fun continueAudio() {
        showProgressBar()

        try {
            playerManager.continueAudio()
        } catch (e: Exception) {
            dismissProgressBar()
            e.printStackTrace()
        }
    }


    fun pause() {
        playerManager.pauseAudio()
        showPlayButton()
    }


    fun previous() {
        resetPlayerInfo()
        showProgressBar()

        try {
            playerManager.previousAudio()
        } catch (e: Exception) {
            dismissProgressBar()
            e.printStackTrace()
        }

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnPlay ->
                btnPlay?.let {
                    continueAudio()
                }

            R.id.btnPause -> {
                btnPause?.let {
                    pause()
                }
            }

            R.id.btnNext ->
                btnNext?.let {
                    next()
                }

            R.id.btnPrev ->
                btnPrev?.let {
                    previous()
                }
        }
    }

    override fun onPreparedAudio(status: Status) {
        dismissProgressBar()
        resetPlayerInfo()

        val duration = status.duration.toInt()
        wave?.post { wave?.duration = duration.toLong() }
        wave.setRawData(resources.assets.open("simple.wav").readBytes())
        txtDuration?.post { txtDuration?.text = toTimeSongString(duration) }
    }

    override fun onCompletedAudio() {
        resetPlayerInfo()

        try {
            playerManager.nextAudio()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onContinueAudio(status: Status) {
        dismissProgressBar()
    }

    override fun onPlaying(status: Status) {
        dismissProgressBar()
        showPauseButton()
    }

    override fun onTimeChanged(status: Status) {
        val currentPosition = status.currentPosition.toInt()
        wave?.post { wave?.progress = currentPosition.toFloat() }
        txtCurrentDuration?.post { txtCurrentDuration?.text = toTimeSongString(currentPosition) }
    }

    override fun onPaused(status: Status) {
    }

    override fun onStopped(status: Status) {
    }


    private fun showProgressBar() {
        progressBarPlayer?.postVisible()
        btnPlay?.postInvisible()
        btnPause?.postInvisible()
    }

    private fun dismissProgressBar() {
        progressBarPlayer?.postInvisible()
        showPauseButton()
    }


    private fun resetPlayerInfo() {

        wave?.post { wave?.progress = 0F }
        txtDuration?.post { txtDuration.text = context.getString(R.string.play_initial_time) }
        txtCurrentDuration?.post {
            txtCurrentDuration.text = context.getString(R.string.play_initial_time)
        }
    }


    /**
     * Check if playlist already sorted or not.
     * We need to check because there is a possibility that the user reload previous playlist
     * from persistence storage like sharedPreference or SQLite.
     *
     * @param playlist list of PlayerAudio
     * @return true if sorted, false if not.
     */
    private fun isAlreadySorted(playlist: List<PlayerAudio>?): Boolean {
        // If there is position in the first audio, then playlist is already sorted.
        return playlist?.let { it[0].position != -1 } == true
    }

    /**
     * Generates a default audio title for each audio on list.
     * @param playlist The audio list.
     * @param title The default title.
     */
    private fun generateTitleAudio(playlist: List<PlayerAudio>, title: String) {
        for (i in playlist.indices) {
            if (title == context.getString(R.string.track_number)) {
                playlist[i].title =
                    context.getString(R.string.track_number) + " " + (i + 1).toString()
            } else {
                playlist[i].title = title
            }
        }
    }


    fun kill() {
        playerManager.kill()
    }


    private fun View.postVisible() {
        post {
            visibility = View.VISIBLE
        }
    }

    private fun View.postInvisible() {
        post {
            visibility = View.GONE
        }
    }

    override fun onStartTracking(progress: Float) {
        if (playerManager.currentAudio != null) {
            showProgressBar()
        }
    }

    override fun onStopTracking(progress: Float) {
        dismissProgressBar()

        if (playerManager.isPaused()) {
            showPlayButton()
        }
    }

    override fun onProgressChanged(progress: Float, byUser: Boolean) {
        playerManager.let {
            if (byUser) {
                it.seekTo(progress.toInt())
            }
        }
    }
}
