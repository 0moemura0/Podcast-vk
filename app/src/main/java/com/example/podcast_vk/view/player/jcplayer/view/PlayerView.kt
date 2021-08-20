package com.example.podcast_vk.view.player.jcplayer.view

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.example.jean.jcplayer.general.PlayerUtil.toTimeSongString
import com.example.jean.jcplayer.general.errors.OnInvalidPathListener
import com.example.podcast_vk.R
import com.example.podcast_vk.view.player.jcplayer.PlayerManager
import com.example.podcast_vk.view.player.jcplayer.PlayerManagerListener
import com.example.podcast_vk.view.player.jcplayer.general.Status
import com.example.podcast_vk.view.player.jcplayer.model.PlayerAudio
import com.example.podcast_vk.view.wave.OnProgressListener
import kotlinx.android.synthetic.main.view_player.view.*


/**
 * This class is the PlayerAudio View. Handles user interactions and communicates events to [PlayerManager].
 * @author Jean Carlos (Github: @jeancsanchez)
 * @date 12/07/16.
 * Jesus loves you.
 */
class PlayerView : LinearLayout, View.OnClickListener, OnProgressListener,
    PlayerManagerListener {

    private val jcPlayerManager: PlayerManager by lazy {
        PlayerManager.getInstance(context).get()!!
    }

    val myPlaylist: List<PlayerAudio>
        get() = jcPlayerManager.playlist

    val isPlaying: Boolean
        get() = jcPlayerManager.isPlaying()

    val isPaused: Boolean
        get() = jcPlayerManager.isPaused()

    val currentAudio: PlayerAudio?
        get() = jcPlayerManager.currentAudio

    var onInvalidPathListener: OnInvalidPathListener? = null

    var jcPlayerManagerListener: PlayerManagerListener? = null
        set(value) {
            field = value
            jcPlayerManager.jcPlayerManagerListener = value
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

        txtCurrentMusic?.setTextColor(
            attrs.getColor(
                R.styleable.PlayerView_text_audio_title_color,
                defaultColor
            )
        )
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
        btnPlay?.setImageResource(
            attrs.getResourceId(
                R.styleable.PlayerView_play_icon,
                R.drawable.ic_baseline_play_arrow_24
            )
        )

        btnPause?.setImageResource(
            attrs.getResourceId(
                R.styleable.PlayerView_pause_icon,
                R.drawable.ic_pause
            )
        )
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
            attrs.getResourceId(
                R.styleable.PlayerView_next_icon,
                R.drawable.ic_forward_15_36
            )
        )

        btnPrev?.setColorFilter(
            attrs.getColor(
                R.styleable.PlayerView_previous_icon_color,
                defaultColor
            )
        )
        btnPrev?.setImageResource(
            attrs.getResourceId(
                R.styleable.PlayerView_previous_icon,
                R.drawable.ic_replay_15_36
            )
        )

        attrs.getBoolean(R.styleable.PlayerView_show_random_button, true).also { showButton ->

        }

        attrs.getBoolean(R.styleable.PlayerView_show_repeat_button, true).also { showButton ->

        }

    }

    /**
     * Initialize the playlist and controls.
     *
     * @param playlist List of PlayerAudio objects that you want play
     * @param jcPlayerManagerListener The view status jcPlayerManagerListener (optional)
     */
    fun initPlaylist(
        playlist: List<PlayerAudio>,
        jcPlayerManagerListener: PlayerManagerListener? = null
    ) {
        /*Don't sort if the playlist have position number.
        We need to do this because there is a possibility that the user reload previous playlist
        from persistence storage like sharedPreference or SQLite.*/
        if (isAlreadySorted(playlist).not()) {
            sortPlaylist(playlist)
        }

        jcPlayerManager.playlist = playlist as ArrayList<PlayerAudio>
        jcPlayerManager.jcPlayerManagerListener = jcPlayerManagerListener
        jcPlayerManager.jcPlayerManagerListener = this
    }

    /**
     * Initialize an anonymous playlist with a default JcPlayer title for all audios
     *
     * @param playlist List of urls strings
     */
    fun initAnonPlaylist(playlist: List<PlayerAudio>) {
        generateTitleAudio(playlist, context.getString(R.string.track_number))
        initPlaylist(playlist)
    }

    /**
     * Initialize an anonymous playlist, but with a custom title for all audios
     *
     * @param playlist List of PlayerAudio files.
     * @param title    Default title for all audios
     */
    fun initWithTitlePlaylist(playlist: List<PlayerAudio>, title: String) {
        generateTitleAudio(playlist, title)
        initPlaylist(playlist)
    }

    /**
     * Add an audio for the playlist. We can track the PlayerAudio by
     * its id. So here we returning its id after adding to list.
     *
     * @param jcAudio audio file generated from [PlayerAudio]
     * @return jcAudio position.
     */
    fun addAudio(jcAudio: PlayerAudio): Int {
        jcPlayerManager.playlist.let {
            val lastPosition = it.size
            jcAudio.position = lastPosition + 1

            if (it.contains(jcAudio).not()) {
                it.add(lastPosition, jcAudio)
            }

            return jcAudio.position!!
        }
    }

    /**
     * Remove an audio for the playlist
     *
     * @param jcAudio PlayerAudio object
     */
    fun removeAudio(jcAudio: PlayerAudio) {
        jcPlayerManager.playlist.let {
            if (it.contains(jcAudio)) {
                if (it.size > 1) {
                    // play next audio when currently played audio is removed.
                    if (jcPlayerManager.isPlaying()) {
                        if (jcPlayerManager.currentAudio == jcAudio) {
                            it.remove(jcAudio)
                            pause()
                            resetPlayerInfo()
                        } else {
                            it.remove(jcAudio)
                        }
                    } else {
                        it.remove(jcAudio)
                    }
                } else {
                    //TODO: Maybe we need jcPlayerManager.stopPlay() for stopping the player
                    it.remove(jcAudio)
                    pause()
                    resetPlayerInfo()
                }
            }
        }
    }

    /**
     * Plays the give audio.
     * @param jcAudio The audio to be played.
     */
    fun playAudio(jcAudio: PlayerAudio) {
        showProgressBar()

        jcPlayerManager.playlist.let {
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
            jcPlayerManager.playAudio(jcAudio)
        }
    }

    /**
     * Shows the play button on player.
     */
    private fun showPlayButton() {
        btnPlay?.makeVisible()
        btnPause?.makeInvisible()
    }

    /**
     * Shows the pause button on player.
     */
    private fun showPauseButton() {
        btnPlay?.makeInvisible()
        btnPause?.makeVisible()
    }

    /**
     * Goes to next audio.
     */
    fun next() {
        jcPlayerManager.let { player ->
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

    /**
     * Continues the current audio.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun continueAudio() {
        showProgressBar()

        try {
            jcPlayerManager.continueAudio()
        } catch (e: Exception) {
            dismissProgressBar()
            e.printStackTrace()
        }
    }

    /**
     * Pauses the current audio.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun pause() {
        jcPlayerManager.pauseAudio()
        showPlayButton()
    }


    /**
     * Goes to precious audio.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun previous() {
        resetPlayerInfo()
        showProgressBar()

        try {
            jcPlayerManager.previousAudio()
        } catch (e: Exception) {
            dismissProgressBar()
            e.printStackTrace()
        }

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnPlay ->
                btnPlay?.let {
                    applyPulseAnimation(it)
                    continueAudio()
                }

            R.id.btnPause -> {
                btnPause?.let {
                    applyPulseAnimation(it)
                    pause()
                }
            }

            R.id.btnNext ->
                btnNext?.let {
                    applyPulseAnimation(it)
                    next()
                }

            R.id.btnPrev ->
                btnPrev?.let {
                    applyPulseAnimation(it)
                    previous()
                }


            else -> { // Repeat case
                jcPlayerManager.activeRepeat()
                val active = jcPlayerManager.repeatPlaylist or jcPlayerManager.repeatCurrAudio


            }
        }
    }

    /**
     * Create a notification player with same playlist with a custom icon.
     *
     * @param iconResource icon path.
     */
    fun createNotification(@DrawableRes iconResource: Int) {
        jcPlayerManager.createNewNotification(iconResource)
    }

    /**
     * Create a notification player with same playlist with a default icon
     */
    fun createNotification() {
        //jcPlayerManager.createNewNotification(R.drawable.ic)
    }

    override fun onPreparedAudio(status: Status) {
        dismissProgressBar()
        resetPlayerInfo()
        onUpdateTitle(status.playerAudio.title)

        val duration = status.duration.toInt()
        wave?.post { wave?.duration = duration.toLong() }
        wave.setRawData(resources.assets.open("simple.wav").readBytes())
        txtDuration?.post { txtDuration?.text = toTimeSongString(duration) }
    }

    override fun onCompletedAudio() {
        resetPlayerInfo()

        try {
            jcPlayerManager.nextAudio()
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

    override fun onJcpError(throwable: Throwable) {
        // TODO
//        jcPlayerManager.currentAudio?.let {
//            onInvalidPathListener?.onPathError(it)
//        }
    }

    private fun showProgressBar() {
        progressBarPlayer?.makeVisible()
        btnPlay?.makeInvisible()
        btnPause?.makeInvisible()
    }

    private fun dismissProgressBar() {
        progressBarPlayer?.makeInvisible()
        showPauseButton()
    }

    private fun onUpdateTitle(title: String) {
        txtCurrentMusic?.let {
            it.makeVisible()

            it.post { it.text = title }
        }
    }

    private fun resetPlayerInfo() {
        txtCurrentMusic?.post { txtCurrentMusic.text = "" }
        wave?.post { wave?.progress = 0F }
        txtDuration?.post { txtDuration.text = context.getString(R.string.play_initial_time) }
        txtCurrentDuration?.post {
            txtCurrentDuration.text = context.getString(R.string.play_initial_time)
        }
    }

    /**
     * Sorts the playlist.
     */
    private fun sortPlaylist(playlist: List<PlayerAudio>) {
        for (i in playlist.indices) {
            val jcAudio = playlist[i]
            jcAudio.position = i
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

    private fun applyPulseAnimation(view: View?) {

    }

    /**
     * Kills the player
     */
    fun kill() {
        jcPlayerManager.kill()
    }


    /**
     * Makes view visible in UI Thread
     */
    private fun View.makeVisible() {
        post {
            visibility = View.VISIBLE
        }
    }

    /**
     * Makes view invisible in UI Thread
     */
    private fun View.makeInvisible() {
        post {
            visibility = View.GONE
        }
    }

    override fun onStartTracking(progress: Float) {
        if (jcPlayerManager.currentAudio != null) {
            showProgressBar()
        }
    }

    override fun onStopTracking(progress: Float) {
        dismissProgressBar()

        if (jcPlayerManager.isPaused()) {
            showPlayButton()
        }
    }

    override fun onProgressChanged(progress: Float, byUser: Boolean) {
        jcPlayerManager.let {
            if (byUser) {
                it.seekTo(progress.toInt())
            }
        }
    }
}
