package com.example.podcast_vk

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.podcast_vk.view.player.model.PlayerAudio
import com.example.podcast_vk.view.view.PlayerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_podcast.*
import java.util.*


class PodcastFragment : Fragment(R.layout.fragment_podcast) {

    private val viewModel: PodcastViewModel by activityViewModels()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPodcast()

        initBottomSheet()
        initPlayer(view)

        iv_back.setOnClickListener {
            activity?.onBackPressed()
        }
        iv_stat.setOnClickListener {
            findNavController().navigate(R.id.action_podcastFragment_to_statisticsFragment)
        }
    }

    private fun initPlayer(view: View) {
        val playerView: PlayerView = view.findViewById(R.id.player)
        val audios: ArrayList<PlayerAudio> = ArrayList<PlayerAudio>()


        viewModel.podcastList.observe(
            viewLifecycleOwner, { result ->
                result.forEach {
                    audios.add(PlayerAudio.createFromURL(it.title, it.enclosure.url))
                    playerView.initPlaylist(audios, null)
                    playerView.playAudio(audios[0])
                }

            }
        )


    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        tvSubtitle.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        tvSubtitle.adapter = ReactionAdapter()
    }
}