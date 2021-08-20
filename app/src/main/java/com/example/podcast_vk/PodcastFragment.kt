package com.example.podcast_vk

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.podcast_vk.network.RSSController
import com.example.podcast_vk.view.player.jcplayer.model.PlayerAudio
import com.example.podcast_vk.view.player.jcplayer.view.PlayerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_podcast.*

import java.util.ArrayList


class PodcastFragment : Fragment(R.layout.fragment_podcast) {
    // This property is only valid between onCreateView and
// onDestroyVie
    private val viewModel: PodcastViewModel by activityViewModels()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.play.setOnClickListener {
//
//        }
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> Toast.makeText(
                        context,
                        "STATE_COLLAPSED",
                        Toast.LENGTH_SHORT
                    ).show()
                    BottomSheetBehavior.STATE_EXPANDED -> Toast.makeText(
                        context,
                        "STATE_EXPANDED",
                        Toast.LENGTH_SHORT
                    ).show()
                    BottomSheetBehavior.STATE_DRAGGING -> Toast.makeText(
                        context,
                        "STATE_DRAGGING",
                        Toast.LENGTH_SHORT
                    ).show()
                    BottomSheetBehavior.STATE_SETTLING -> Toast.makeText(
                        context,
                        "STATE_SETTLING",
                        Toast.LENGTH_SHORT
                    ).show()
                    BottomSheetBehavior.STATE_HIDDEN -> Toast.makeText(
                        context,
                        "STATE_HIDDEN",
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> Toast.makeText(context, "OTHER_STATE", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        tvSubtitle.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        tvSubtitle.adapter = ReactionAdapter()

//
        val jcplayerView: PlayerView = view.findViewById(R.id.player);


        val jcAudios: ArrayList<PlayerAudio> = ArrayList<PlayerAudio>()
        jcAudios.add(
            PlayerAudio.createFromURL(
                "url audio",
                "https://vk.com/podcast-147415323_456239591.mp3"
            )
        )

        jcplayerView.initPlaylist(jcAudios, null)

        jcAudios.add(
            PlayerAudio.createFromAssets("sample.wav")
        )



        iv_stat.setOnClickListener {
            findNavController().navigate(R.id.action_podcastFragment_to_statisticsFragment)
        }
        jcplayerView.playAudio(jcAudios[0])

//        ArrayList<PlayerAudio> jcAudios = new ArrayList<>();
//        jcAudios.add(PlayerAudio.createFromAssets("Asset audio 1", "49.v4.mid"));
//        jcAudios.add(PlayerAudio.createFromAssets("Asset audio 2", "56.mid"));
//        jcAudios.add(PlayerAudio.createFromAssets("Asset audio 3", "a_34.mp3"));
//        jcAudios.add(PlayerAudio.createFromRaw("Raw audio 1", R.raw.a_34));
        //        player.playAudio(player.getMyPlaylist().get(0));

//        ArrayList<PlayerAudio> jcAudios = new ArrayList<>();
//        jcAudios.add(PlayerAudio.createFromAssets("Asset audio 1", "49.v4.mid"));
//        jcAudios.add(PlayerAudio.createFromAssets("Asset audio 2", "56.mid"));
//        jcAudios.add(PlayerAudio.createFromAssets("Asset audio 3", "a_34.mp3"));
//        jcAudios.add(PlayerAudio.createFromRaw("Raw audio 1", R.raw.a_34));
//        jcAudios.add(PlayerAudio.createFromRaw("Raw audio 2", R.raw.a_203))
        //jcAudios.add(PlayerAudio.createFromFilePath("File directory audio", this.getFilesDir() + "/" + "CANTO DA GRAÚNA.mp3"));
        //jcAudios.add(PlayerAudio.createFromAssets("I am invalid audio", "aaa.mid")); // invalid assets file
//        player.initPlaylist(jcAudios);


//        jcAudios.add(PlayerAudio.createFromFilePath("test", this.getFilesDir() + "/" + "13.mid"));
//        jcAudios.add(PlayerAudio.createFromFilePath("test", this.getFilesDir() + "/" + "123123.mid")); // invalid file path
//        jcAudios.add(PlayerAudio.createFromAssets("49.v4.mid"));
//        jcAudios.add(PlayerAudio.createFromRaw(R.raw.a_203));
//        jcAudios.add(PlayerAudio.createFromRaw("a_34", R.raw.a_34));
//        player.initWithTitlePlaylist(jcAudios, "Awesome music");


//        jcAudios.add(PlayerAudio.createFromFilePath("test", this.getFilesDir() + "/" + "13.mid"));
//        jcAudios.add(PlayerAudio.createFromFilePath("test", this.getFilesDir() + "/" + "123123.mid")); // invalid file path
//        jcAudios.add(PlayerAudio.createFromAssets("49.v4.mid"));
//        jcAudios.add(PlayerAudio.createFromRaw(R.raw.a_203));
//        jcAudios.add(PlayerAudio.createFromRaw("a_34", R.raw.a_34));
//        player.initAnonPlaylist(jcAudios);

//        Adding new audios to playlist
//        player.addAudio(PlayerAudio.createFromURL("url audio","http://www.villopim.com.br/android/Music_01.mp3"));
//        player.addAudio(PlayerAudio.createFromAssets("49.v4.mid"));
//        player.addAudio(PlayerAudio.createFromRaw(R.raw.a_34));
//        player.addAudio(PlayerAudio.createFromFilePath(this.getFilesDir() + "/" + "121212.mmid"));

        //jcAudios.add(PlayerAudio.createFromFilePath("File directory audio", this.getFilesDir() + "/" + "CANTO DA GRAÚNA.mp3"));
        //jcAudios.add(PlayerAudio.createFromAssets("I am invalid audio", "aaa.mid")); // invalid assets file
//        player.initPlaylist(jcAudios);


//        jcAudios.add(PlayerAudio.createFromFilePath("test", this.getFilesDir() + "/" + "13.mid"));
//        jcAudios.add(PlayerAudio.createFromFilePath("test", this.getFilesDir() + "/" + "123123.mid")); // invalid file path
//        jcAudios.add(PlayerAudio.createFromAssets("49.v4.mid"));
//        jcAudios.add(PlayerAudio.createFromRaw(R.raw.a_203));
//        jcAudios.add(PlayerAudio.createFromRaw("a_34", R.raw.a_34));
//        player.initWithTitlePlaylist(jcAudios, "Awesome music");


//        jcAudios.add(PlayerAudio.createFromFilePath("test", this.getFilesDir() + "/" + "13.mid"));
//        jcAudios.add(PlayerAudio.createFromFilePath("test", this.getFilesDir() + "/" + "123123.mid")); // invalid file path
//        jcAudios.add(PlayerAudio.createFromAssets("49.v4.mid"));
//        jcAudios.add(PlayerAudio.createFromRaw(R.raw.a_203));
//        jcAudios.add(PlayerAudio.createFromRaw("a_34", R.raw.a_34));
//        player.initAnonPlaylist(jcAudios);

//        Adding new audios to playlist
//        player.addAudio(PlayerAudio.createFromURL("url audio","http://www.villopim.com.br/android/Music_01.mp3"));
//        player.addAudio(PlayerAudio.createFromAssets("49.v4.mid"));
//        player.addAudio(PlayerAudio.createFromRaw(R.raw.a_34));
//        player.addAudio(PlayerAudio.createFromFilePath(this.getFilesDir() + "/" + "121212.mmid"));
//        player.initPlaylist(jcAudios, this)


        val contreller = RSSController()
        contreller.start()

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
}