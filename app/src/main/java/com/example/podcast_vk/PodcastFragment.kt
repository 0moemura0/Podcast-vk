package com.example.podcast_vk

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.podcast_vk.databinding.FragmentPodcastBinding

class PodcastFragment : Fragment() {

    private var _binding: FragmentPodcastBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val progressAnim: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(binding.wave, "progress", 0F, 100F).apply {
            interpolator = LinearInterpolator()
            duration = 1000
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val s = inflater.inflate(R.layout.fragment_podcast, container, false)
        _binding = FragmentPodcastBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.play.setOnClickListener {
            inflateWave()
        }


        binding.wave.onProgressChanged = { progress, byUser ->
            Log.e("wave", "Progress set: $progress, and it's $byUser that user did this")

            if (progress == 100F && !byUser) {
                binding.wave.waveColor =
                    ContextCompat.getColor(requireContext(), R.color.accentBlue)
                binding.wave.isTouchable = true
            }
        }

        binding.wave.onStartTracking = {
            Log.e("wave", "Started tracking from: $it")
        }

        binding.wave.onStopTracking = {
            Log.e("wave", "Progress set: $it")
        }

//    wave.onProgressListener = object : OnProgressListener {
//      override fun onProgressChanged(progress: Float, byUser: Boolean) {
//        Log.e("wave", "Progress changed: $progress, and it's $byUser that user did this")
//      }
//
//      override fun onStartTracking(progress: Float) {
//        Log.e("wave", "Started tracking from: $progress")
//      }
//
//      override fun onStopTracking(progress: Float) {
//        Log.e("wave", "Stopped tracking at: $progress")
//      }
//    }
    }

    private fun inflateWave() {
        binding.wave.setRawData(
            resources.assets.open("sample.wav").readBytes()
        ) { progressAnim.start() }
    }
}