package com.example.podcast_vk.view.wave

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import com.example.podcast_vk.*
import com.example.podcast_vk.view.*
import kotlin.math.abs
import kotlin.math.max

class AudioWaveView : View {

    constructor(context: Context?) : super(context) {
        setWillNotDraw(false)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setWillNotDraw(false)
        inflateAttrs(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs,
        defStyleAttr
    ) {
        setWillNotDraw(false)
        inflateAttrs(attrs)
    }

    var onProgressListener: OnProgressListener? = null

    var onProgressChanged: (Float, Boolean) -> Unit = { _, _ -> }

    var onStartTracking: (Float) -> Unit = {}

    var onStopTracking: (Float) -> Unit = {}

    var onSamplingListener: OnSamplingListener? = null

    private var chunkHeight: Int = 0
        get() = if (field == 0) h else abs(field)
        set(value) {
            field = value
            redrawData()
        }

    private var chunkWidth: Int = dip(2)
        set(value) {
            field = abs(value)
            redrawData()
        }

    private var chunkSpacing: Int = dip(1)
        set(value) {
            field = abs(value)
            redrawData()
        }

    private var chunkRadius: Int = 0
        set(value) {
            field = abs(value)
            redrawData()
        }

    private var minChunkHeight: Int = dip(2)
        set(value) {
            field = abs(value)
            redrawData()
        }

    private var waveColor: Int = Color.BLACK
        set(value) {
            wavePaint = smoothPaint(value.withAlpha(0xAA))
            waveFilledPaint = filterPaint(value)
            redrawData()
        }

    var progress: Float = 0F
        set(value) {
//            require(value in 0.0..100.0) { "Progress must be in 0..100" }

            field = abs(value)

            onProgressListener?.onProgressChanged(field, isTouched)
            onProgressChanged(field, isTouched)

            postInvalidate()
        }

    private var scaledData: ByteArray = byteArrayOf()
        set(value) {
            field = if (value.size <= chunksCount) {
                ByteArray(chunksCount).paste(value)
            } else {
                value
            }

            redrawData()
        }

    var duration: Long = 0
        set(value) {
            field = max(400, value)
            expansionAnimator.duration = field
        }

    var isExpansionAnimated: Boolean = true

    var isTouchable = true

    var isTouched = false
        private set

    private val chunksCount: Int
        get() = w / chunkStep

    private val chunkStep: Int
        get() = chunkWidth + chunkSpacing

    private val centerY: Int
        get() = h

    private val progressFactor: Float
        get() = progress / duration

    private val initialDelay: Long = 50

    private val expansionAnimator = ValueAnimator.ofFloat(0.0F, 1.0F).apply {
        duration = duration
        interpolator = OvershootInterpolator()
        addUpdateListener {
            redrawData(factor = it.animatedFraction)
        }
    }

    private var wavePaint = smoothPaint(waveColor.withAlpha(0xAA))
    private var waveFilledPaint = filterPaint(waveColor)
    private var waveBitmap: Bitmap? = null

    private var w: Int = 0
    private var h: Int = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val cv = canvas ?: return

        cv.transform {
            clipRect(0, 0, w, h)
            waveBitmap?.let { drawBitmap(it, 0F, 0F, wavePaint) }
        }

        cv.transform {
            clipRect(0F, 0F, w * progressFactor, h.toFloat())
            waveBitmap?.let { drawBitmap(it, 0F, 0F, waveFilledPaint) }
        }
    }

    // suppressed here since we allocate only once,
    // when the wave bounds have been just calculated(it happens once)
    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        w = right - left
        h = bottom - top

        if (waveBitmap.fits(w, h)) {
            return
        }

        if (changed) {
            waveBitmap?.recycle()
            waveBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

            // absolutely ridiculous hack to draw wave in RecyclerView items
            scaledData = when (scaledData.size) {
                0 -> byteArrayOf()
                else -> scaledData
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)

        if (!isTouchable || !isEnabled) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouched = true
                progress = event.toProgress()

                // these paired calls look ugly, but we need them for Java
                onProgressListener?.onStartTracking(progress)
                onStartTracking(progress)

                return true
            }
            MotionEvent.ACTION_MOVE -> {
                isTouched = true
                progress = event.toProgress()
                return true
            }
            MotionEvent.ACTION_UP -> {
                isTouched = false
                onProgressListener?.onStopTracking(progress)
                onStopTracking(progress)
                return false
            }
            else -> {
                isTouched = false
                return super.onTouchEvent(event)
            }
        }
    }


    @JvmOverloads
    fun setRawData(
        raw: ByteArray
        //               ,callback: () -> Unit = {}
    ) {
        MAIN_THREAD.postDelayed({
            Sampler.downSampleAsync(raw, chunksCount) {
                scaledData = it
                //callback()
            }
        }, initialDelay)
    }

    private fun MotionEvent.toProgress() = this@toProgress.x.clamp(0F, w.toFloat()) / w * 100F

    private fun redrawData(canvas: Canvas? = waveBitmap?.inCanvas(), factor: Float = 1.0F) {
        if (waveBitmap == null || canvas == null) return

        waveBitmap.flush()

        scaledData.forEachIndexed { i, chunk ->
            val chunkHeight = ((chunk.abs.toFloat() / Byte.MAX_VALUE) * chunkHeight).toInt()
            val clampedHeight = max(chunkHeight, minChunkHeight)
            val heightDiff = (clampedHeight - minChunkHeight).toFloat()
            val animatedDiff = (heightDiff * factor).toInt()

            canvas.drawRoundRect(
                rectFOf(
                    left = chunkSpacing / 2 + i * chunkStep,
                    top = centerY - minChunkHeight - animatedDiff,
                    right = chunkSpacing / 2 + i * chunkStep + chunkWidth,
                    bottom = centerY + minChunkHeight + animatedDiff
                ),
                chunkRadius.toFloat(),
                chunkRadius.toFloat(),
                wavePaint
            )
        }

        postInvalidate()
    }

    private fun animateExpansion() {
        expansionAnimator.start()
    }

    private fun inflateAttrs(attrs: AttributeSet?) {
        val resAttrs = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AudioWaveView,
            0,
            0
        )

        with(resAttrs) {
            chunkHeight = getDimensionPixelSize(R.styleable.AudioWaveView_chunkHeight, chunkHeight)
            chunkWidth = getDimensionPixelSize(R.styleable.AudioWaveView_chunkWidth, chunkWidth)
            chunkSpacing = getDimensionPixelSize(
                R.styleable.AudioWaveView_chunkSpacing,
                chunkSpacing
            )
            minChunkHeight = getDimensionPixelSize(
                R.styleable.AudioWaveView_minChunkHeight,
                minChunkHeight
            )
            chunkRadius = getDimensionPixelSize(R.styleable.AudioWaveView_chunkRadius, chunkRadius)
            isTouchable = getBoolean(R.styleable.AudioWaveView_touchable, isTouchable)
            waveColor = getColor(R.styleable.AudioWaveView_waveColor, waveColor)
            progress = getFloat(R.styleable.AudioWaveView_progress, progress)
            isExpansionAnimated = getBoolean(
                R.styleable.AudioWaveView_animateExpansion,
                isExpansionAnimated
            )
            recycle()
        }
    }

    fun start() {
        if (isExpansionAnimated) {
            animateExpansion()
        }
    }
}