package com.example.podcast_vk.view.wave

interface OnSamplingListener {
    fun onComplete()
}

interface OnProgressListener {
    fun onStartTracking(progress: Float)
    fun onStopTracking(progress: Float)
    fun onProgressChanged(progress: Float, byUser: Boolean)
}